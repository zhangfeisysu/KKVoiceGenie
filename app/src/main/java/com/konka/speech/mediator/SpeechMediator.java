package com.konka.speech.mediator;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

/**
 * @author ZhangFei
 * @date 2017=11=3
 * 中介者模式中的中介者。用于封装语音识别，语义识别，语音合成模块。
 * 三者之间的交互通过中介者来进行解耦。
 * 在此定义交互的接口
 */

public abstract class SpeechMediator {
    protected Context mContext;
    @Inject
    protected BaseSpeechRecognizer mSpeechRecognizer;
    @Inject
    protected BaseSemanticProcessor mSemanticProcessor;
    @Inject
    protected BaseTTSEngine mTTSEngine;
    @Inject
    protected BaseSpeechView mSpeechView;

    public SpeechMediator(Context context) {
        mContext = context;
    }

    /**
     * 初始化各个模块
     *
     * @param callback 初始化结果回调
     */
    public abstract void initEngine(Callback<Boolean, String> callback);

    /**
     * 根据需要做释放资源操作
     */
    public abstract void releaseEngine();

    /**
     * 开始识别用户说法。对外暴露的是识别接口，内部的语义解析和语音合成对用户透明
     *
     * @param intent 调用方的意图
     */
    public abstract void startRecognize(Intent intent);

    /**
     * 取消当前正在执行的对话
     */
    public abstract void cancelRecognize();

    /**
     * 所有异常情况汇总处理，主要在code中通过错误码来反馈错误，附带信息可在msg中补充
     *
     * @param code 错误码
     * @param msg  补充错误信息
     */
    public abstract void onError(int code, String msg);
}