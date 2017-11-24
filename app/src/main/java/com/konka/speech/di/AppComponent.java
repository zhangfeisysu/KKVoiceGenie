package com.konka.speech.di;

import android.app.Application;

import com.konka.speech.SpeechApplication;
import com.konka.speech.model.APIService;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * This is a Dagger component. Refer to {@link SpeechApplication} for the list of Dagger components
 * used in this application.
 * <p>
 * Even though Dagger allows annotating a {@link Component} as a singleton, the code
 * itself must ensure only one instance of the class is created. This is done in {@link
 * SpeechApplication}.
 * {@link AndroidSupportInjectionModule}
 * is the module from Dagger.Android that helps with the generation
 * and location of subcomponents.
 *
 * @author ZhangFei
 * @date 2017-11-23
 */
@Singleton
@Component(modules = {AppModule.class,
        NetworkModule.class,
        ServiceBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication> {
    void inject(SpeechApplication application);

    APIService getAPIService();

    @Override
    void inject(DaggerApplication instance);

    // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).build().inject(this);
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph now.
    @Component.Builder
    interface Builder {
        @BindsInstance
        AppComponent.Builder application(Application application);

        @BindsInstance
        AppComponent.Builder network(NetworkModule net);

        AppComponent build();
    }
}
