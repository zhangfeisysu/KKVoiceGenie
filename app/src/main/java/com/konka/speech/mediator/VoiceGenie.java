package com.konka.speech.mediator;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.affy.zlogger.ZLogger;
import com.konka.speech.sdk.scene.SceneSpeechManager;

import static com.konka.speech.mediator.Dispatcher.CONVERSATION_RECOGNITION_COMPLETE;
import static com.konka.speech.mediator.Dispatcher.CONVERSATION_RECOGNITION_START;
import static com.konka.speech.mediator.Dispatcher.CONVERSATION_RECORD_COMPLETE;
import static com.konka.speech.mediator.Dispatcher.CONVERSATION_RECORD_START;
import static com.konka.speech.mediator.Dispatcher.CONVERSATION_RECORD_VOLUME;
import static com.konka.speech.mediator.Dispatcher.CONVERSATION_TTS_COMPLETE;
import static com.konka.speech.mediator.Dispatcher.CONVERSATION_TTS_START;
import static com.konka.speech.mediator.Dispatcher.CONVERSATION_UNDERSTANDING_COMPLETE;
import static com.konka.speech.mediator.Dispatcher.CONVERSATION_UNDERSTANDING_START;

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

    private SceneSpeechManager mSceneSpeechManager;

    private Dispatcher mDispatcher;
    private int mSessionIdCounter;

    private static final Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONVERSATION_RECORD_START: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onRecordStart();
                    break;
                }
                case CONVERSATION_RECORD_VOLUME: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onRecordVolumeChanged(0);
                    break;
                }

                case CONVERSATION_RECORD_COMPLETE: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onRecordComplete();
                    break;
                }

                case CONVERSATION_RECOGNITION_START: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onSpeechRecognitionStart();
                    break;
                }
                case CONVERSATION_RECOGNITION_COMPLETE: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onSpeechRecognitionComplete(conversation);
                    break;
                }
                case CONVERSATION_UNDERSTANDING_START: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onSemanticUnderstandingStart();
                    break;
                }
                case CONVERSATION_UNDERSTANDING_COMPLETE: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onSemanticUnderstandingComplete(conversation);
                    break;
                }
                case CONVERSATION_TTS_START: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onTTSStart(conversation.getWord());
                    break;
                }
                case CONVERSATION_TTS_COMPLETE: {
                    Conversation conversation = (Conversation) msg.obj;
                    conversation.getVoiceGenie().onTTSComplete();
                    break;
                }
                default:
                    throw new AssertionError("Unknown handler message received: " + msg.what);
            }
        }
    };

    public VoiceGenie(Context context) {
        super(context);
        mSessionIdCounter = -1;
        mDispatcher = new Dispatcher(context, this, mMainHandler);
        mSpeechView = new TestSpeechView(context, this);
    }

    @Override
    public void initEngine(Callback<Boolean, String> callback) {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.init(callback);
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
        mSpeechView.show();
        mSpeechView.performRecord();
    }

    @Override
    public void onRecordVolumeChanged(int volume) {

    }

    @Override
    public void onRecordComplete() {
        //停止录音动画，准备播放识别动画
    }

    @Override
    public void onSpeechRecognitionStart() {
        mSpeechView.performRecognize();
    }

    @Override
    public void onSpeechRecognitionComplete(Conversation conversation) {
        //停止识别动画，准备播放解析动画
        mSpeechView.showUserWord(conversation);
    }

    @Override
    public void onSemanticUnderstandingStart() {
        mSpeechView.performParse();
    }

    @Override
    public void onSemanticUnderstandingComplete(Conversation conversation) {
        //停止理解动画，界面上展示用户说法
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

    public void setSpeechRecognizer(BaseSpeechRecognizer speechRecognizer) {
        mSpeechRecognizer = speechRecognizer;
        mSpeechRecognizer.setSpeechRecognitionListener(this);
    }

    public BaseSemanticProcessor getSemanticProcessor() {
        return mSemanticProcessor;
    }

    public void setSemanticProcessor(BaseSemanticProcessor semanticProcessor) {
        mSemanticProcessor = semanticProcessor;
        mSemanticProcessor.setSemanticProcessListener(this);
    }

    public BaseTTSEngine getTTSEngine() {
        return mTTSEngine;
    }

    public void setTTSEngine(BaseTTSEngine engine) {
        mTTSEngine = engine;
        mTTSEngine.setTTSListener(this);
    }

    public SceneSpeechManager getSceneSpeechManager() {
        return mSceneSpeechManager;
    }

    public void setSceneSpeechManager(SceneSpeechManager sceneSpeechManager) {
        mSceneSpeechManager = sceneSpeechManager;
    }
}