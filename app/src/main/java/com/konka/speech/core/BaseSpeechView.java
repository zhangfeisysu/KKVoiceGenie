package com.konka.speech.core;

import android.content.Context;

import com.konka.speech.di.ServiceScope;

/**
 * 语音精灵界面UI交互控制逻辑
 * <ul>
 * <li>展示录音，识别动画，结果展示</li>
 * <li>RemoteView，H5页面展示</li>
 * </ul>
 *
 * @author ZhangFei
 * @date 2017-11-7
 */

public abstract class BaseSpeechView extends Colleague {
    private SpeechViewListener mSpeechViewListener;

    public BaseSpeechView(@ServiceScope Context context) {
        super(context);
    }

    public BaseSpeechView(@ServiceScope Context context, SpeechMediator mediator) {
        super(context, mediator);
    }

    /**
     * 展示界面
     */
    public abstract void show();

    /**
     * 隐藏界面
     */
    public abstract void dismiss();

    /**
     * 执行和录音相关的界面操作，例如播放录音动画
     */
    public abstract void performRecord();

    /**
     * 取消录音相关界面操作
     */
    public abstract void cancelRecord();

    /**
     * 处理音量变化
     *
     * @param volume
     */
    public void handleVolumeChanged(float volume) {
    }

    /**
     * 执行语音识别相关界面操作，如播放识别动画
     */
    public abstract void performRecognize();

    /**
     * 取消语音识别相关界面操作
     */
    public abstract void cancelRecognize();

    /**
     * 执行语义解析相关界面操作，如播放正在解析动画
     */
    public abstract void performParse();

    /**
     * 取消语义解析相关界面操作
     */
    public abstract void cancelParse();

    /**
     * 展示用户说法
     *
     * @param conversation 当轮对话
     */
    public abstract void showUserWord(Conversation conversation);

    /**
     * 展示对话回复
     *
     * @param conversation 当轮对话
     */
    public abstract void showReply(Conversation conversation);

    public SpeechViewListener getSpeechViewListener() {
        return mSpeechViewListener;
    }

    public void setSpeechViewListener(SpeechViewListener speechViewListener) {
        mSpeechViewListener = speechViewListener;
    }

    public interface SpeechViewListener {
        /**
         * 界面展示
         */
        void onShow();

        /**
         * 界面关闭
         */
        void onDismiss();
    }
}
