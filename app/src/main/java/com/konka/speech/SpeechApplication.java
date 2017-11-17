package com.konka.speech;

import android.app.Application;

import com.konka.speech.di.component.DaggerNetComponent;
import com.konka.speech.di.component.NetComponent;
import com.konka.speech.di.module.ApplicationModule;
import com.konka.speech.di.module.NetModule;

/**
 * @author ZhangFei
 * @date 2017-11-17
 */

public class SpeechApplication extends Application {
    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mNetComponent = DaggerNetComponent.builder()
                .netModule(new NetModule())
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
