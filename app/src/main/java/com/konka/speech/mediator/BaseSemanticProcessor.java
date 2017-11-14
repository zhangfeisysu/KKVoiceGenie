package com.konka.speech.mediator;

import android.content.Context;

import com.konka.speech.sdk.scene.SceneSpeechManager;

/**
 * 语义理解的基础类，后续替代语义理解方案时基于此类开发
 *
 * @author ZhangFei
 * @date 2017-11-6
 */

public abstract class BaseSemanticProcessor extends Colleague {
    private SemanticProcessListener mSemanticProcessListener;
    protected SceneSpeechManager mSceneSpeechManager;

    public BaseSemanticProcessor(Context context, SpeechMediator mediator) {
        super(context, mediator);
        mSceneSpeechManager = new SceneSpeechManager(context);
    }

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
