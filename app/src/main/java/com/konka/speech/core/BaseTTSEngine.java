package com.konka.speech.core;

import android.content.Context;

import com.konka.speech.di.ServiceScope;

/**
 * TTS引擎基础类，后续更换方案基于此类开发
 *
 * @author ZhangFei
 * @date 2017-11-6
 */

public abstract class BaseTTSEngine extends Colleague {
    private TTSListener mTTSListener;

    public BaseTTSEngine(@ServiceScope Context context) {
        super(context);
    }

    public BaseTTSEngine(@ServiceScope Context context, SpeechMediator mediator) {
        super(context, mediator);
    }

    /**
     * 播放一段文本
     *
     * @param text      要播放的文本
     * @param queueMode 排队的模式 : 1.排队播放;2.清空原有播放内容，播放当前 内容
     */
    public abstract void playText(String text, int queueMode);

    /**
     * 停止正在进行的播放
     */
    public abstract void stopPlaying();

    /**
     * 是否正在朗读
     *
     * @return true　or false
     */
    public abstract boolean isPlaying();

    /**
     * 释放被TextSpeech engine占用的资源，
     */
    public abstract void release();

    /**
     * 设置语速
     *
     * @param speechRate 数值0-9，5为普通语速
     */
    public abstract void setSpeechSpeed(int speechRate);

    /**
     * 设置语调
     *
     * @param speechPitch 数值0-9，5为普通语调，大于5尖锐，小于5低沉
     */
    public abstract void setSpeechPitch(int speechPitch);

    public interface TTSListener {
        /**
         * 开始TTS语音播报
         *
         * @param text 播报的文本
         */
        void onTTSStart(String text);

        /**
         * 完成TTS语音播报
         */
        void onTTSComplete();
    }

    public TTSListener getTTSListener() {
        return mTTSListener;
    }

    public void setTTSListener(TTSListener TTSListener) {
        mTTSListener = TTSListener;
    }
}
