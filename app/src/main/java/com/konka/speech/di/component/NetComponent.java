package com.konka.speech.di.component;

import com.konka.speech.di.module.ApplicationModule;
import com.konka.speech.di.module.NetModule;
import com.konka.speech.model.APIService;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author ZhangFei
 * @date 2017-11-17
 */
@Component(modules = {ApplicationModule.class, NetModule.class})
@Singleton
public interface NetComponent {

//    void inject(SpeechApplication application);

    /**
     * 获取服务端接口对象
     *
     * @return
     */
    APIService getAPIService();

    /**
     * 获取客户端OkHttpClient
     *
     * @return
     */
    OkHttpClient getOKHttpClient();

    /**
     * 获取Retrofit
     *
     * @return
     */
    Retrofit getRetrofit();
}
