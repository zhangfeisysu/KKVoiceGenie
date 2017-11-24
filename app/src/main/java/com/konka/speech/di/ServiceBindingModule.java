package com.konka.speech.di;

import com.konka.speech.mediator.SpeechService;
import com.konka.speech.mediator.VoiceGenieModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author ZhangFei
 * @date 2017-11-24
 */
@Module
public abstract class ServiceBindingModule {
    @ServiceScope
    @ContributesAndroidInjector(modules = {VoiceGenieModule.class})
    abstract SpeechService speechService();
}
