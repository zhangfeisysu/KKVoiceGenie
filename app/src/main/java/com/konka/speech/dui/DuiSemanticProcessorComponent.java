package com.konka.speech.dui;

import com.konka.speech.di.ServiceScope;

import dagger.Component;

/**
 * @author ZhangFei
 * @date 2017-11-29
 */
@ServiceScope
@Component(modules = {DuiSemanticProcessorModule.class})
public interface DuiSemanticProcessorComponent {
    void inject(DuiSemanticProcessor duiSemanticProcessor);
}
