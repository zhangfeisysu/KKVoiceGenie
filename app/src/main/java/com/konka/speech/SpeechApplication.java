package com.konka.speech;

import android.app.Application;

import com.konka.speech.di.AppComponent;
import com.konka.speech.di.DaggerAppComponent;
import com.konka.speech.di.NetworkModule;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * We create a custom {@link Application} class that extends  {@link DaggerApplication}.
 * We then override applicationInjector() which tells Dagger how to make our @Singleton Component
 * We never have to call `component.inject(this)` as {@link DaggerApplication} will do that for us.
 *
 * @author ZhangFei
 * @date 2017-11-17
 */

public class SpeechApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder()
                .application(this)
                .network(new NetworkModule())
                .build();
        appComponent.inject(this);
        return appComponent;
    }


}
