package com.konka.speech.sdk.scene;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import javax.inject.Inject;

import static com.konka.speech.constant.IntentConstants.EXTRA_PACKAGE_NAME;
import static com.konka.speech.sdk.constant.IntentConstants.ACTION_SCENE_SPEECH_FEEDBACK;
import static com.konka.speech.sdk.constant.IntentConstants.ACTION_SCENE_SPEECH_UPLOAD_KEYWORDS;
import static com.konka.speech.sdk.constant.IntentConstants.EXTRA_SCENE_SPEECH_HIT_KEYWORDS;
import static com.konka.speech.sdk.constant.IntentConstants.EXTRA_SCENE_SPEECH_KEYWORDS;

/**
 * 采用广播的方式实现全程语音
 *
 * @author ZhangFei
 * @date 2017-11-13
 */
class SceneSpeechModeImpl extends SceneSpeechMode<SceneKeywords> {
    SceneSpeechModeReceiver mSceneSpeechModeReceiver;
    private SceneKeywords mSceneKeywords;

    @Inject
    SceneSpeechModeImpl(Context context) {
        super(context);
        mSceneKeywords = new SceneKeywords();
    }

    @Override
    public void onReceiveKeywords(String pkgName, String keywordsJson) {
        //TODO 解析json并缓存关键字
    }

    @Override
    SceneKeywords getKeywords() {
        return mSceneKeywords;
    }

    @Override
    boolean resetKeywords(String pkgName) {
        mSceneKeywords.getKeywordsList().clear();
        return true;
    }

    @Override
    void feedback(String pkgName, String keywords) {
        Intent intent = new Intent(ACTION_SCENE_SPEECH_FEEDBACK);
        intent.putExtra(EXTRA_PACKAGE_NAME, pkgName);
        intent.putExtra(EXTRA_SCENE_SPEECH_HIT_KEYWORDS, keywords);
        mContext.sendBroadcast(intent);
    }

    @Override
    void init() {
        if (mSceneSpeechModeReceiver == null) {
            mSceneSpeechModeReceiver = new SceneSpeechModeReceiver(this);
        }
        mSceneSpeechModeReceiver.register();
    }

    @Override
    void release() {
        mSceneSpeechModeReceiver.unregister();
    }

    /**
     * 用广播的方式接收第三方进程发送的全程语音指令
     */
    static class SceneSpeechModeReceiver extends BroadcastReceiver {
        private SceneSpeechMode mSceneSpeechMode;

        SceneSpeechModeReceiver(SceneSpeechMode sceneSpeechMode) {
            mSceneSpeechMode = sceneSpeechMode;
        }

        void register() {
            IntentFilter intentFilter = new IntentFilter(ACTION_SCENE_SPEECH_UPLOAD_KEYWORDS);
            mSceneSpeechMode.mContext.registerReceiver(this, intentFilter);
        }

        void unregister() {
            mSceneSpeechMode.mContext.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                //接收到第三方进程注册的指令列表
                final String pkgName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
                final String keywords = intent.getStringExtra(EXTRA_SCENE_SPEECH_KEYWORDS);
                mSceneSpeechMode.onReceiveKeywords(pkgName, keywords);
            }
        }
    }
}
