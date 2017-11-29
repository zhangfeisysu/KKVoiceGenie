package com.konka.speech.dui;

import android.content.Context;

import com.affy.zlogger.ZLogger;
import com.konka.speech.core.BaseSemanticProcessor;
import com.konka.speech.core.SpeechMediator;
import com.konka.speech.di.ServiceScope;
import com.konka.speech.global.Scanner;
import com.konka.speech.scene.SceneKeywords;
import com.konka.speech.scene.SceneSpeechManager;

/**
 * @author ZhangFei
 * @date 2017-11-7
 */

public class DuiSemanticProcessor extends BaseSemanticProcessor {
    public DuiSemanticProcessor(@ServiceScope Context context) {
        super(context);
//        mSceneSpeechManager = new SceneSpeechManager(context);
//        mScanner = new Scanner(context);
        DaggerDuiSemanticProcessorComponent
                .builder()
                .duiSemanticProcessorModule(new DuiSemanticProcessorModule(context))
                .build().inject(this);
    }

    public DuiSemanticProcessor(@ServiceScope Context context, SpeechMediator mediator) {
        super(context, mediator);
        mSceneSpeechManager = new SceneSpeechManager(context);
        mScanner = new Scanner(context);
    }

    @Override
    public void init() {
        if (mScanner != null) {
            mScanner.init();
        }
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
