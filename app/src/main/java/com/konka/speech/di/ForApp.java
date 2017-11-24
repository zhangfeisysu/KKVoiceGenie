package com.konka.speech.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 作用于整个App
 * Created by ZhangFei on 2017-11-24.
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface ForApp {
}
