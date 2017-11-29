package com.konka.speech.core;

import android.content.Context;

import com.konka.speech.di.ForService;
import com.konka.speech.di.ServiceScope;
import com.konka.speech.dui.DuiSemanticProcessor;
import com.konka.speech.dui.DuiSpeechRecognizer;
import com.konka.speech.dui.DuiTTSEngine;

import dagger.Module;
import dagger.Provides;

/**
 * @author ZhangFei
 * @date 2017-11-24
 */
@Module
public class VoiceGenieModule {

    @Provides
    @ServiceScope
    @ForService
    static Context provideContext(SpeechService speechService) {
        return speechService;
    }

//    @Provides
//    @ServiceScope
//    @ForService
//    static SpeechMediator speechMediator(@ForService Context context){
//        return new VoiceGenie(context);
//    }

    @Provides
    @ServiceScope
    static BaseSpeechRecognizer provideSpeechRecognizer(@ForService Context context) {
        return new DuiSpeechRecognizer(context);
    }

    @Provides
    @ServiceScope
    static BaseSemanticProcessor provideSemanticProcessor(@ForService Context context) {
        return new DuiSemanticProcessor(context);
    }

    @Provides
    @ServiceScope
    static BaseTTSEngine provideTtsEngine(@ForService Context context) {
        return new DuiTTSEngine(context);
    }

    @Provides
    @ServiceScope
    static BaseSpeechView provideSpeechView(@ForService Context context) {
        return new TestSpeechView(context);
    }
}
