package com.konka.speech.core;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.affy.zlogger.ZLogger;
import com.konka.speech.di.ForService;

import javax.inject.Inject;

/**
 * 实现中介者的接口，负责处理语音理解，语义理解，TTS播报三者之间的具体交互
 *
 * @author ZhangFei
 * @date 2017-11-6
 */

public class VoiceGenie extends SpeechMediator implements BaseSpeechRecognizer.SpeechRecognitionListener,
        BaseSemanticProcessor.SemanticProcessListener,
        BaseTTSEngine.TTSListener,
        BaseSpeechView.SpeechViewListener {
    private Dispatcher mDispatcher;
    private int mSessionIdCounter = -1;

    private static final Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Log.d("zftest", "voiceGenie current thread " + Thread.currentThread().getName());
            switch (msg.what) {
                case Dispatcher.CONVERSATION_RECORD_START: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onRecordStart();
                    break;
                }
                case Dispatcher.CONVERSATION_RECORD_VOLUME: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onRecordVolumeChanged(0);
                    break;
                }

                case Dispatcher.CONVERSATION_RECORD_COMPLETE: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onRecordComplete();
                    break;
                }

                case Dispatcher.CONVERSATION_RECOGNITION_START: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onSpeechRecognitionStart();
                    break;
                }
                case Dispatcher.CONVERSATION_RECOGNITION_COMPLETE: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onSpeechRecognitionComplete(conversation);
                    break;
                }
                case Dispatcher.CONVERSATION_UNDERSTANDING_START: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onSemanticUnderstandingStart();
                    break;
                }
                case Dispatcher.CONVERSATION_UNDERSTANDING_COMPLETE: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onSemanticUnderstandingComplete(conversation);
                    break;
                }
                case Dispatcher.CONVERSATION_TTS_START: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onTTSStart(conversation.getWord());
                    break;
                }
                case Dispatcher.CONVERSATION_TTS_COMPLETE: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onTTSComplete();
                    break;
                }
                default:
                    throw new AssertionError("Unknown handler message received: " + msg.what);
            }
        }
    };

    @Inject
    public VoiceGenie(@ForService Context context) {
        super(context);
        mSessionIdCounter = -1;
        mDispatcher = new Dispatcher(context, this, mMainHandler);
        VoiceGenieProxy.getInstance().setVoiceGenie(this);
    }


    @Override
    public void initEngine(Callback<Boolean, String> callback) {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.init(callback);
        }
        if (mSemanticProcessor!=null){
            mSemanticProcessor.init();
        }
    }

    @Override
    public void releaseEngine() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.release();
        }
        mDispatcher.shutdown();
    }

    @Override
    public void startRecognize(Intent intent) {
        //TODO 从intent中提取关键信息，然后创建conversation
        Conversation conversation = new Conversation(++mSessionIdCounter, this);
        ZLogger.t("session").d("开启对话sessionID：" + mSessionIdCounter);
        mDispatcher.dispatchSubmit(conversation);
    }

    @Override
    public void cancelRecognize() {
        //取消上一段对话
        mDispatcher.cancel(mSessionIdCounter);
    }

    @Override
    public void onRecordStart() {
        ZLogger.d("========== onRecordStart ============");
        mSpeechView.show();
        mSpeechView.performRecord();
    }

    @Override
    public void onRecordVolumeChanged(int volume) {

    }

    @Override
    public void onRecordComplete() {
        //停止录音动画，准备播放识别动画
        ZLogger.d("========== onRecordComplete ============");
    }

    @Override
    public void onSpeechRecognitionStart() {
        ZLogger.d("========== onSpeechRecognitionStart ============");
        mSpeechView.performRecognize();
    }

    @Override
    public void onSpeechRecognitionComplete(Conversation conversation) {
        //停止识别动画，准备播放解析动画
        ZLogger.d("================== onSpeechRecognitionComplete" + conversation.getWord() + " ============");
        mSpeechView.showUserWord(conversation);
    }

    @Override
    public void onSemanticUnderstandingStart() {
        ZLogger.d("========== onSemanticUnderstandingStart ============");
        mSpeechView.performParse();
    }

    @Override
    public void onSemanticUnderstandingComplete(Conversation conversation) {
        //停止理解动画，界面上展示回复
        ZLogger.d("======== onSemanticUnderstandingComplete " + conversation.getReplyContent().toString() + " ==========");
        mSpeechView.showReply(conversation);
    }

    @Override
    public void onTTSStart(String text) {

    }

    @Override
    public void onTTSComplete() {

    }

    @Override
    public void onError(int code, String msg) {

    }

    @Override
    public void onShow() {

    }

    @Override
    public void onDismiss() {

    }

    public BaseSpeechRecognizer getSpeechRecognizer() {
        return mSpeechRecognizer;
    }

    @Inject
    public void setSpeechRecognizer(BaseSpeechRecognizer speechRecognizer) {
        mSpeechRecognizer = speechRecognizer;
        mSpeechRecognizer.setSpeechRecognitionListener(this);
        mSpeechRecognizer.setMediator(this);
    }

    public BaseSemanticProcessor getSemanticProcessor() {
        return mSemanticProcessor;
    }

    @Inject
    public void setSemanticProcessor(BaseSemanticProcessor semanticProcessor) {
        mSemanticProcessor = semanticProcessor;
        mSemanticProcessor.setSemanticProcessListener(this);
        mSemanticProcessor.setMediator(this);
    }

    public BaseTTSEngine getTTSEngine() {
        return mTTSEngine;
    }

    @Inject
    public void setTTSEngine(BaseTTSEngine engine) {
        mTTSEngine = engine;
        mTTSEngine.setTTSListener(this);
        mTTSEngine.setMediator(this);
    }

    @Inject
    public void setSpeechView(BaseSpeechView speechView) {
        mSpeechView = speechView;
    }
}
