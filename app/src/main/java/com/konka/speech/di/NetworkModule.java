package com.konka.speech.di;

import android.content.Context;

import com.konka.speech.model.APIService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ZhangFei
 * @date 2017-11-23
 */
@Module
public class NetworkModule {
    private String mBaseUrl = "http://www.github.com";

    //    public NetworkModule(String baseUrl) {
//        this.mBaseUrl = baseUrl;
//    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(@ForApp Context context) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    public APIService provideAPIService(Retrofit retrofit) {
        return retrofit.create(APIService.class);
    }
}
