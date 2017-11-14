package com.konka.speech.mediator.dui;

import android.content.Context;

import com.affy.zlogger.ZLogger;
import com.konka.speech.mediator.BaseTTSEngine;
import com.konka.speech.mediator.SpeechMediator;

/**
 * @author ZhangFei
 * @date 2017-11-7
 */

public class DuiTTSEngine extends BaseTTSEngine {

    public DuiTTSEngine(Context context, SpeechMediator mediator) {
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
