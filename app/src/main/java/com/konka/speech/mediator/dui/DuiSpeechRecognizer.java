package com.konka.speech.mediator.dui;

import android.content.Context;

import com.affy.zlogger.ZLogger;
import com.konka.speech.mediator.BaseSpeechRecognizer;
import com.konka.speech.mediator.Callback;
import com.konka.speech.mediator.SpeechMediator;

/**
 * 思必驰语音识别方案
 *
 * @author ZhangFei
 * @date 2017-11-6
 */

public class DuiSpeechRecognizer extends BaseSpeechRecognizer {
    public DuiSpeechRecognizer(Context context, SpeechMediator mediator) {
        super(context, mediator);
    }

    @Override
    public void init(final Callback<Boolean, String> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callback.onCompleted(true);
                ZLogger.d("DuiSpeechRecognizer init success");
            }
        }).start();
    }

    @Override
    public void release() {
        ZLogger.d("DuiSpeechRecognizer release success");
    }

    @Override
    public void startRecord() {
        ZLogger.d("DuiSpeechRecognizer 开始录音");
    }

    @Override
    public void cancelRecord() {
        ZLogger.d("DuiSpeechRecognizer 取消录音");
    }

    @Override
    public void startRecognize() {
        ZLogger.d("DuiSpeechRecognizer 开始识别");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelRecognize() {
        ZLogger.d("DuiSpeechRecognizer 取消识别");
    }
}
