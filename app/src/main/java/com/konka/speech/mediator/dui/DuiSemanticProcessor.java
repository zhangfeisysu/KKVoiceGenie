package com.konka.speech.mediator.dui;

import android.content.Context;

import com.affy.zlogger.ZLogger;
import com.konka.speech.mediator.BaseSemanticProcessor;
import com.konka.speech.mediator.SpeechMediator;
import com.konka.speech.sdk.scene.SceneKeywords;

/**
 * @author ZhangFei
 * @date 2017-11-7
 */

public class DuiSemanticProcessor extends BaseSemanticProcessor {
    public DuiSemanticProcessor(Context context, SpeechMediator mediator) {
        super(context, mediator);
    }

    @Override
    public void startParse(String recognizedWord) {
        ZLogger.d("DuiSemanticProcessor 开始语义理解");
        SceneKeywords sceneKeywords = mSceneSpeechManager.getSceneKeywords(recognizedWord);
        if (sceneKeywords != null) {
            //TODO 全程语音处理
            mSceneSpeechManager.hitKeywords(sceneKeywords.getPackageName(), recognizedWord);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelParse(int sessionId) {
        ZLogger.d("DuiSemanticProcessor 取消语义理解");
    }
}
