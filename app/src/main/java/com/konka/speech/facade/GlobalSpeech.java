package com.konka.speech.facade;

import android.content.Context;

/**
 * 全局语音抽象接口
 *
 * @author ZhangFei
 * @date 2017-11-10
 */

public interface GlobalSpeech {
    /**
     * 更新全局语音业务逻辑
     *
     * @param context    {@link Context}
     * @param globalJson 全局语音json
     */
    void updateGlobal(Context context, String globalJson);
}
