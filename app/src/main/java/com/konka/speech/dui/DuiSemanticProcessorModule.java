package com.konka.speech.dui;

import android.content.Context;

import com.konka.speech.di.ForApp;
import com.konka.speech.di.ForService;
import com.konka.speech.di.ServiceScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author ZhangFei
 * @date 2017-11-29
 */
@ServiceScope
@Module
public class DuiSemanticProcessorModule {
    private final Context mContext;

    DuiSemanticProcessorModule(@ForService Context context) {
        mContext = context;
    }

    @Provides
    @ServiceScope
    @ForApp
    Context provideContext(){
        return mContext.getApplicationContext();
    }

//    @Provides
//    @ServiceScope
//      SceneSpeechManager provideSceneSpeechManager( ) {
//        return new SceneSpeechManager(mContext);
//    }
//
//    @Provides
//    @ServiceScope
//      Scanner provideScanner( ) {
//        return new Scanner(mContext);
//    }
}
