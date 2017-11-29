package com.konka.speech.core;

import android.content.Context;

import com.konka.speech.di.ForService;
import com.konka.speech.global.Scanner;
import com.konka.speech.scene.SceneSpeechManager;

import javax.inject.Inject;

/**
 * 语义理解的基础类，后续替代语义理解方案时基于此类开发
 *
 * @author ZhangFei
 * @date 2017-11-6
 */

public abstract class BaseSemanticProcessor extends Colleague {
    @Inject
    protected SceneSpeechManager mSceneSpeechManager;
    @Inject
    protected Scanner mScanner;
    private SemanticProcessListener mSemanticProcessListener;

    public BaseSemanticProcessor(@ForService Context context) {
        super(context);
    }

    public BaseSemanticProcessor(@ForService Context context, SpeechMediator mediator) {
        super(context, mediator);
    }

    /**
     * 初始化
     */
    public abstract void init();

    /**
     * 开始解析
     *
     * @param recognizedWord 语音识别出来的字符串
     */
    public abstract void startParse(String recognizedWord);

    /**
     * 取消解析
     *
     * @param sessionId 对话id
     */
    public abstract void cancelParse(int sessionId);

    public interface SemanticProcessListener {
        /**
         * 开始语义理解
         */
        void onSemanticUnderstandingStart();

        /**
         * 完成语义理解
         *
         * @param conversation 当前对话，封装了语义结果列表
         */
        void onSemanticUnderstandingComplete(Conversation conversation);
    }

    public SemanticProcessListener getSemanticProcessListener() {
        return mSemanticProcessListener;
    }

    public void setSemanticProcessListener(SemanticProcessListener semanticProcessListener) {
        mSemanticProcessListener = semanticProcessListener;
    }
}
