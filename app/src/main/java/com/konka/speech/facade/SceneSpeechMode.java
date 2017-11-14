package com.konka.speech.facade;

/**
 * 全程语音抽象接口
 *
 * @author ZhangFei
 * @date 2017-11-10
 */

public interface SceneSpeechMode {
    /**
     * 注册全程语音关键词
     *
     * @param keywords 全程语音关键词
     * @param listener 用户说法监听
     */
    void registerKeywords(String keywords, SceneSpeechListener listener);

    /**
     * 取消注册全程语音
     */
    void unregisterKeywords();

    /**
     * 上传全程语音关键词
     */
    void uploadKeywords();

    interface SceneSpeechListener {
        /**
         * 用户说了注册过的关键词
         *
         * @param keywords 关键词
         */
        void onKeywordsHit(String keywords);

        /**
         * 异常回调
         *
         * @param errorCode 错误码
         * @param msg       错误信息
         */
        void onError(int errorCode, String msg);
    }

}
