package com.konka.speech.core;

/**
 * VoiceGenie类的代理者，只暴露部分接口给第三方调用，属于保护代理。
 *
 * @author ZhangFei
 * @date 2017-11-27
 */

public class VoiceGenieProxy {
    private static volatile VoiceGenieProxy mInstance;
    private VoiceGenie mVoiceGenie;

    public static VoiceGenieProxy getInstance() {
        if (mInstance == null) {
            synchronized (VoiceGenieProxy.class) {
                if (mInstance == null) {
                    mInstance = new VoiceGenieProxy();
                }
            }
        }
        return mInstance;
    }

    public void feedback() {

    }

    void setVoiceGenie(VoiceGenie voiceGenie) {
        mVoiceGenie = voiceGenie;
    }
}
