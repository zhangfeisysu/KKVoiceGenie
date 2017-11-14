package com.konka.speech.sdk.scene;

import android.content.Context;

/**
 * /**
 * 抽象出本地服务端全程语音基础方法
 *
 * @param <T> 存储数据结构
 * @author ZhangFei
 * @date 2017-11-13
 */

public abstract class SceneSpeechMode<T> {
    final Context mContext;

    SceneSpeechMode(Context context) {
        mContext = context;
        init();
    }

    void init() {
    }

    void release() {
    }

    /**
     * 接收第三方进程发送过来的全程语音指令列表
     *
     * @param pkgName      进程包名
     * @param keywordsJson 关键词Json
     */
    abstract void onReceiveKeywords(String pkgName, String keywordsJson);

    /**
     * 获取包名对应的全程语音关键字列表
     *
     * @return 关键字列表
     */
    abstract T getKeywords();

    /**
     * reset包名对应的全程语音关键字列表
     *
     * @param pkgName 包名,为空时清空列表
     * @return 是否清理成功
     */
    abstract boolean resetKeywords(String pkgName);

    /**
     * 向第三方进程反馈被命中的关键词
     *
     * @param pkgName
     * @param keywords
     */
    abstract void feedback(String pkgName, String keywords);
}
