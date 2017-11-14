package com.konka.speech.mediator;

import android.content.Context;

/**
 * @author ZhangFei
 * @date 2017-11-3
 * 中介者模式中的参与者，抽象出语音识别，语义理解，语音合成共有的特性
 */

public abstract class Colleague {
    protected Context mContext;
    protected SpeechMediator mMediator;

    public Colleague(Context context, SpeechMediator mediator) {
        mContext = context;
        mMediator = mediator;
    }
}
