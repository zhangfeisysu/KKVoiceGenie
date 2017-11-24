package com.konka.speech.mediator;

import android.content.Context;

import com.konka.speech.di.ForService;
import com.konka.speech.di.ServiceScope;
import com.konka.speech.mediator.dui.DuiSemanticProcessor;
import com.konka.speech.mediator.dui.DuiSpeechRecognizer;
import com.konka.speech.mediator.dui.DuiTTSEngine;

import dagger.Module;
import dagger.Provides;

/**
 * @author ZhangFei
 * @date 2017-11-24
 */
@Module
public   class VoiceGenieModule {
    private VoiceGenie mVoiceGenie;
    private BaseSpeechRecognizer mSpeechRecognizer;
    private BaseSemanticProcessor mSemanticProcessor;
    private BaseTTSEngine mTTSEngine;
    private BaseSpeechView mSpeechView;

    @Provides
    @ServiceScope
    @ForService
    public Context provideContext(SpeechService speechService) {
        return speechService;
    }

    @Provides
    @ServiceScope
    public BaseSpeechRecognizer provideSpeechRecognizer(@ForService Context context) {
        mSpeechRecognizer = new DuiSpeechRecognizer(context, mVoiceGenie);
        return mSpeechRecognizer;
    }

    @Provides
    @ServiceScope
    public BaseSemanticProcessor provideSemanticProcessor(@ForService Context context) {
        mSemanticProcessor = new DuiSemanticProcessor(context, mVoiceGenie);
        return mSemanticProcessor;
    }

    @Provides
    @ServiceScope
    public BaseTTSEngine provideTtsEngine(@ForService Context context) {
        mTTSEngine = new DuiTTSEngine(context, mVoiceGenie);
        return mTTSEngine;
    }

    @Provides
    @ServiceScope
    public BaseSpeechView provideSpeechView(@ForService Context context) {
        mSpeechView = new TestSpeechView(context, mVoiceGenie);
        return mSpeechView;
    }
}
