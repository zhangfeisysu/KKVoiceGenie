package com.konka.speech.mediator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.affy.zlogger.ZLogger;
import com.konka.speech.GlobalErrorCode;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * 对话分发器，负责将每次用户输入请求分发到队列中处理。采用单线程+异步队列的方式。
 *
 * @author ZhangFei
 * @date 2017-11-6
 */

class Dispatcher {
    static final int CONVERSATION_START = 1;
    static final int CONVERSATION_CANCEL = 2;
    static final int CONVERSATION_COMPLETE = 3;
    static final int CONVERSATION_RECORD_START = 4;
    static final int CONVERSATION_RECORD_VOLUME = 5;
    static final int CONVERSATION_RECORD_COMPLETE = 6;
    static final int CONVERSATION_RECOGNITION_START = 7;
    static final int CONVERSATION_RECOGNITION_COMPLETE = 8;
    static final int CONVERSATION_UNDERSTANDING_START = 9;
    static final int CONVERSATION_UNDERSTANDING_COMPLETE = 10;
    static final int CONVERSATION_TTS_START = 11;
    static final int CONVERSATION_TTS_COMPLETE = 12;
    static final int CONVERSATION_ERROR = 13;
    static final int NETWORK_STATE_CHANGE = 14;

    final Context mContext;
    final VoiceGenie mVoiceGenie;
    private final HandlerThread mDispatcherThread;
    private final Handler mMainHandler;
    private final Handler mHandler;
    private final NetworkBroadcastReceiver mNetworkBroadcastReceiver;
    private final Map<Integer, Conversation> mConversationMap;

    private static final String DISPATCHER_THREAD_NAME = "Dispatcher";

    Dispatcher(Context context, VoiceGenie voiceGenie, Handler mainHandler) {
        mContext = context;
        mMainHandler = mainHandler;
        mVoiceGenie = voiceGenie;
        mDispatcherThread = new HandlerThread(DISPATCHER_THREAD_NAME, THREAD_PRIORITY_BACKGROUND);
        mDispatcherThread.start();
        mHandler = new DispatcherHandler(mDispatcherThread.getLooper(), this);
        mConversationMap = new LinkedHashMap<>();
        mNetworkBroadcastReceiver = new NetworkBroadcastReceiver(this);
        mNetworkBroadcastReceiver.register();
    }

    void dispatchSubmit(Conversation conversation) {
        mHandler.sendMessage(mHandler.obtainMessage(CONVERSATION_START, conversation));
    }

    void dispatchComplete(Conversation conversation) {
        mHandler.sendMessage(mHandler.obtainMessage(CONVERSATION_COMPLETE, conversation));
    }

    void dispatchCancel(int sessionId) {
        ZLogger.t("session").d("取消sessionId为：" + sessionId + "的对话");
        Conversation conversation = mConversationMap.get(sessionId);
        if (conversation != null) {
            ZLogger.t("session").d("执行取消sessionId为：" + sessionId + "的对话");
            mHandler.sendMessage(mHandler.obtainMessage(CONVERSATION_CANCEL, conversation));
        }
    }

    void dispatchFailed() {
    }

    private void dispatchNetworkStateChange(NetworkInfo info) {
        mHandler.sendMessage(mHandler.obtainMessage(NETWORK_STATE_CHANGE, info));
    }

    void cancel(int sessionId) {
        ZLogger.t("session").d("取消sessionId为：" + sessionId + "的对话");
        Conversation conversation = mConversationMap.get(sessionId);
        if (conversation != null) {
            ZLogger.t("session").d("执行取消sessionId为：" + sessionId + "的对话");
            performCancel(conversation);
        }
    }

    void shutdown() {
        mDispatcherThread.quit();
        mNetworkBroadcastReceiver.unregister();
    }

    private void performSubmit(Conversation conversation) {
        if (conversation != null) {
            if (mConversationMap.containsValue(conversation)) {
                return;
            }
            ZLogger.d("在队列中提交对话");
            mConversationMap.put(conversation.getSessionId(), conversation);
            performRecord(conversation);
        }
    }

    private void performCancel(Conversation conversation) {
        if (conversation != null) {
            if (mConversationMap.containsValue(conversation)) {
                conversation.cancel();
                mConversationMap.remove(conversation.getSessionId());
            }
            mHandler.removeCallbacksAndMessages(conversation);
        }
    }

    private void performComplete(Conversation conversation) {
        if (conversation != null) {
            if (mConversationMap.containsValue(conversation)) {
                mConversationMap.remove(conversation.getSessionId());
            }
        }
    }

    private void performRecord(Conversation conversation) {
        if (conversation != null && !conversation.isCanceled()) {
            ZLogger.d("Dispatcher 执行录音操作");
            VoiceGenie voiceGenie = conversation.getVoiceGenie();
            mMainHandler.dispatchMessage(mMainHandler.obtainMessage(CONVERSATION_RECORD_START, conversation));
            //TODO voiceGenie->record(callback);在回调中执行下面音量变化，录音完成操作。然后调用performRecognize()开始语音识别
            if (voiceGenie.getSpeechRecognizer() != null) {
                voiceGenie.getSpeechRecognizer().startRecord();
                mMainHandler.dispatchMessage(mMainHandler.obtainMessage(CONVERSATION_RECORD_VOLUME, conversation));
                mMainHandler.dispatchMessage(mMainHandler.obtainMessage(CONVERSATION_RECORD_COMPLETE, conversation));
                performRecognize(conversation);
            } else {
                throw new IllegalStateException("语音识别引擎未正确初始化");
            }
        }
    }

    private void performRecognize(Conversation conversation) {
        if (conversation != null && !conversation.isCanceled()) {
            ZLogger.d("执行语音识别操作");
            VoiceGenie voiceGenie = conversation.getVoiceGenie();
            mMainHandler.dispatchMessage(mMainHandler.obtainMessage(CONVERSATION_RECOGNITION_START, conversation));
            /**TODO voiceGenie->recognize(callback);在回调中执行下面识别完成或识别出错操作。然后调用performTTS()播报，
             * performUnderstanding()开始语义理解
             */
            if (voiceGenie.getSpeechRecognizer() != null) {
                voiceGenie.getSpeechRecognizer().startRecognize();
                conversation.setWord("你叫什么名字");
                mMainHandler.dispatchMessage(mMainHandler.obtainMessage(CONVERSATION_RECOGNITION_COMPLETE, conversation));
                performTTS(conversation, true);
                performUnderstanding(conversation);
            } else {
                throw new IllegalStateException("语音识别引擎未正确初始化");
            }
        }
    }

    private void performUnderstanding(Conversation conversation) {
        if (conversation != null && !conversation.isCanceled()) {
            VoiceGenie voiceGenie = conversation.getVoiceGenie();
            mMainHandler.dispatchMessage(mMainHandler.obtainMessage(CONVERSATION_UNDERSTANDING_START, conversation));
            //TODO voiceGenie->parse(callback);在回调中执行下面解析完成或解析出错操作。然后调用performTTS()开始语义合成
            if (voiceGenie.getSemanticProcessor() != null) {
                voiceGenie.getSemanticProcessor().startParse(conversation.getWord());
                conversation.setReplyType(1);
                conversation.setReplyContent("我叫康佳语音精灵");
                mMainHandler.dispatchMessage(mMainHandler.obtainMessage(CONVERSATION_UNDERSTANDING_COMPLETE, conversation));
                performTTS(conversation, false);
                performComplete(conversation);
            } else {
                throw new IllegalStateException("语义理解引擎未正确初始化");
            }
        }
    }

    private void performTTS(Conversation conversation, boolean isQuestion) {
        if (conversation != null && !conversation.isCanceled()) {
            if (!isQuestion && !(conversation.getReplyContent() instanceof String)) {
                return;
            }

            VoiceGenie voiceGenie = conversation.getVoiceGenie();
            mMainHandler.dispatchMessage(mMainHandler.obtainMessage(CONVERSATION_TTS_START, conversation));
            if (voiceGenie.getTTSEngine() != null) {
                String word;
                if (isQuestion) {
                    word = conversation.getWord();
                } else {
                    word = (String) conversation.getReplyContent();
                }
                //TODO voiceGenie->tts(word,callback);在回调中执行下面播报成功或播报失败出错操作。isQuestion标记播报问句还是回复语句
                voiceGenie.getTTSEngine().playText(word, 0);
                mMainHandler.dispatchMessage(mMainHandler.obtainMessage(CONVERSATION_TTS_COMPLETE, conversation));
            } else {
                throw new IllegalStateException("TTS引擎未正确初始化");
            }
        }
    }

    private void performNetworkStateChange(NetworkInfo info) {
        if (info != null && !info.isConnected()) {
            Iterator<Conversation> iterator = mConversationMap.values().iterator();
            while (iterator.hasNext()) {
                Conversation conversation = iterator.next();
                performCancel(conversation);
            }
            mVoiceGenie.onError(GlobalErrorCode.NETWORK_DISCONNECTED, "网络异常");
        }
    }

    private static class DispatcherHandler extends Handler {

        private final Dispatcher mDispatcher;

        public DispatcherHandler(Looper looper, Dispatcher dispatcher) {
            super(looper);
            this.mDispatcher = dispatcher;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONVERSATION_START: {
                    Conversation conversation = (Conversation) msg.obj;
                    mDispatcher.performSubmit(conversation);
                    break;
                }
                case CONVERSATION_CANCEL: {
                    Conversation conversation = (Conversation) msg.obj;
                    mDispatcher.performCancel(conversation);
                    break;
                }
                case CONVERSATION_COMPLETE: {
                    Conversation conversation = (Conversation) msg.obj;
                    mDispatcher.performComplete(conversation);
                    break;
                }
                case NETWORK_STATE_CHANGE: {
                    NetworkInfo info = (NetworkInfo) msg.obj;
                    mDispatcher.performNetworkStateChange(info);
                    break;
                }
                default: {
                    throw new AssertionError("Unknown handler message received: " + msg.what);
                }
            }
        }

    }

    static class NetworkBroadcastReceiver extends BroadcastReceiver {
        private final Dispatcher mDispatcher;

        NetworkBroadcastReceiver(Dispatcher dispatcher) {
            this.mDispatcher = dispatcher;
        }

        void register() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(CONNECTIVITY_ACTION);
            mDispatcher.mContext.registerReceiver(this, filter);
        }

        void unregister() {
            mDispatcher.mContext.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // On some versions of Android this may be called with a null Intent,
            // also without extras (getExtras() == null), in such case we use defaults.
            if (intent == null) {
                return;
            }
            final String action = intent.getAction();
            if (CONNECTIVITY_ACTION.equals(action)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                mDispatcher.dispatchNetworkStateChange(connectivityManager.getActiveNetworkInfo());
            }
        }
    }
}
