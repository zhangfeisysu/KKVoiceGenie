package com.konka.speech.dui;

import android.content.Context;

import com.affy.zlogger.ZLogger;
import com.konka.speech.core.BaseTTSEngine;
import com.konka.speech.core.SpeechMediator;
import com.konka.speech.di.ServiceScope;

/**
 * @author ZhangFei
 * @date 2017-11-7
 */

public class DuiTTSEngine extends BaseTTSEngine {

    public DuiTTSEngine(@ServiceScope Context context) {
        super(context);
    }

    public DuiTTSEngine(@ServiceScope Context context, SpeechMediator mediator) {
        super(context, mediator);
    }

    @Override
    public void playText(String text, int queueMode) {
        ZLogger.d("DuiTTSEngine 开始播报文字：" + text);
    }

    @Override
    public void stopPlaying() {
        ZLogger.d("DuiTTSEngine 停止播报文字：");
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void release() {

    }

    @Override
    public void setSpeechSpeed(int speechRate) {

    }

    @Override
    public void setSpeechPitch(int speechPitch) {

    }
}
