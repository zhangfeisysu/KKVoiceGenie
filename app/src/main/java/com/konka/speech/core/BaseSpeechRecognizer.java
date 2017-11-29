package com.konka.speech.core;

import android.content.Context;

import com.konka.speech.di.ServiceScope;

/**
 * 语音识别过程的基础类，后续替代语音识别方案时基于此类开发
 *
 * @author ZhangFei
 * @date 2017-11-6
 */

public abstract class BaseSpeechRecognizer extends Colleague {
    private SpeechRecognitionListener mSpeechRecognitionListener;

    public BaseSpeechRecognizer(@ServiceScope Context context) {
        super(context);
    }

    public BaseSpeechRecognizer(@ServiceScope Context context, SpeechMediator mediator) {
        super(context, mediator);
    }

    /**
     * 初始化识别引擎
     *
     * @param callback 初始化结果回调
     * @return 初始化结果
     */
    public abstract void init(Callback<Boolean, String> callback);

    /**
     * 释放识别引擎资源
     */
    public abstract void release();

    /**
     * 开始录音
     */
    public abstract void startRecord();

    /**
     * 取消录音
     */
    public abstract void cancelRecord();

    /**
     * 开始语音识别
     */
    public abstract void startRecognize();

    /**
     * 取消语音识别
     */
    public abstract void cancelRecognize();

    public interface SpeechRecognitionListener {
        /**
         * 开始录音，对应于用户开始说话
         */
        void onRecordStart();

        /**
         * 识别到录音音量的反馈
         *
         * @param volume 值为0到100
         */
        void onRecordVolumeChanged(int volume);

        /**
         * 录音结束，对应于用户结束说话
         */
        void onRecordComplete();

        /**
         * 开始语音识别
         */
        void onSpeechRecognitionStart();

        /**
         * 完成语音识别
         *
         * @param conversation 当前对话，封装了识别结果字符串
         */
        void onSpeechRecognitionComplete(Conversation conversation);
    }

    public SpeechRecognitionListener getSpeechRecognitionListener() {
        return mSpeechRecognitionListener;
    }

    public void setSpeechRecognitionListener(SpeechRecognitionListener speechRecognitionListener) {
        mSpeechRecognitionListener = speechRecognitionListener;
    }
}
