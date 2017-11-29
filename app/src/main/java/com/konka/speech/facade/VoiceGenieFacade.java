package com.konka.speech.facade;

import android.content.Context;

/**
 * 语音精灵对外暴露接口，使用外观模式设计。封装了全程语音接口。
 *
 * @author ZhangFei
 * @date 2017-11-10
 */

public class VoiceGenieFacade {
    private static volatile VoiceGenieFacade mInstance;
    private Context mContext;
    private final SceneSpeechMode mSceneSpeechMode;
    private SceneSpeechMode.SceneSpeechListener mSceneSpeechListener;

    private VoiceGenieFacade(Context context) {
        mContext = context;
        mSceneSpeechMode = new SceneSpeechModeImpl(context);
    }

    public static VoiceGenieFacade getInstance(Context context) {
        if (mInstance == null) {
            synchronized (VoiceGenieFacade.class) {
                if (mInstance == null) {
                    mInstance = new VoiceGenieFacade(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 注册全程语音
     *
     * @param sceneJson 全程语音关键词json
     * @param listener  用户说法监听
     */
    public void registerSceneSpeech(String sceneJson, SceneSpeechMode.SceneSpeechListener listener) {
        mSceneSpeechListener = listener;
        mSceneSpeechMode.registerKeywords(sceneJson, listener);
    }

    /**
     * 取消注册全程语音
     */
    public void unregisterSceneSpeech() {
        mSceneSpeechMode.unregisterKeywords();
    }

    /**
     * 更新全程语音
     *
     * @param sceneJson 全程语音关键词json
     */
    public void updateSceneSpeech(String sceneJson) {
        mSceneSpeechMode.registerKeywords(sceneJson, mSceneSpeechListener);
    }

    /**
     * 更新全局语音业务
     *
     * @param globalJson 全局语音业务关键词
     */
    public void updateGlobalSpeech(String globalJson) {
    }
}
