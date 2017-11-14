package com.konka.speech.facade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.konka.speech.constant.IntentConstants;

/**
 * 全程语音具体实现。实现原理:<br/>
 * 1.接收语音精灵启动发送的广播<br/>
 * 2.注册全程语音命令列表，并通过广播发送给语音精灵<br/>
 * 3.语音精灵优先匹配全程语音指令<br/>
 *
 * @author ZhangFei
 * @date 2017-11-10
 */

class SceneSpeechModeImpl implements SceneSpeechMode {
    /**
     * {@link com.konka.speech.sdk.constant.IntentConstants#EXTRA_SCENE_SPEECH_KEYWORDS}
     */

    static final String EXTRA_SCENE_SPEECH_KEYWORDS = "konka.speech.scene_speech.KEYWORDS";
    /**
     * {@link com.konka.speech.sdk.constant.IntentConstants#ACTION_SCENE_SPEECH_UPLOAD_KEYWORDS}
     */
    static final String ACTION_SCENE_SPEECH_UPLOAD_KEYWORDS = "konka.speech.scene_speech.UPLOAD_KEYWORDS";
    /**
     * {@link IntentConstants#EXTRA_PACKAGE_NAME}
     */
    static final String EXTRA_PACKAGE_NAME = "konka.speech.intent.extra.PACKAGE_NAME";

    private Context mContext;
    private String mKeywords;
    private VoiceGenieReceiver mVoiceGenieReceiver;
    private SceneSpeechListener mSceneSpeechListener;

    SceneSpeechModeImpl(Context context) {
        mContext = context;
    }

    @Override
    public void registerKeywords(String keywords, SceneSpeechListener listener) {
        mKeywords = keywords;
        mSceneSpeechListener = listener;
    }

    @Override
    public void unregisterKeywords() {
        mKeywords = null;
    }

    @Override
    public void uploadKeywords() {
        Intent intent = new Intent(ACTION_SCENE_SPEECH_UPLOAD_KEYWORDS);
        intent.putExtra(EXTRA_SCENE_SPEECH_KEYWORDS, mKeywords);
        intent.putExtra(EXTRA_PACKAGE_NAME, mContext.getPackageName());
        mContext.sendBroadcast(intent);
    }

    static class VoiceGenieReceiver extends BroadcastReceiver {
        /**
         * {@link IntentConstants#ACTION_VOICE_GENIE_START}
         */
        static final String ACTION_VOICE_GENIE_START = "konka.speech.intent.action.VOICE_GENIE_START";

        /**
         * {@link com.konka.speech.sdk.constant.IntentConstants#ACTION_SCENE_SPEECH_FEEDBACK}
         */
        static final String ACTION_SCENE_SPEECH_FEEDBACK = "konka.speech.intent.action.scene_speech.FEEDBACK";

        /**
         * {@link com.konka.speech.sdk.constant.IntentConstants#EXTRA_SCENE_SPEECH_HIT_KEYWORDS}
         */
        static final String EXTRA_SCENE_SPEECH_HIT_KEYWORDS = "konka.speech.extra.scene_speech.HIT_KEYWORDS";

        private SceneSpeechModeImpl mSceneSpeechImpl;

        VoiceGenieReceiver(SceneSpeechModeImpl sceneSpeechImpl) {
            mSceneSpeechImpl = sceneSpeechImpl;
        }

        void register() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_VOICE_GENIE_START);
            filter.addAction(ACTION_SCENE_SPEECH_FEEDBACK);
            mSceneSpeechImpl.mContext.registerReceiver(this, filter);
        }

        void unregister() {
            mSceneSpeechImpl.mContext.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            final String action = intent.getAction();
            //语音精灵界面唤起发送的广播
            if (ACTION_VOICE_GENIE_START.equals(action)) {
                mSceneSpeechImpl.uploadKeywords();
            } else if (ACTION_SCENE_SPEECH_FEEDBACK.equals(action)) {
                final String packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
                final String hitWords = intent.getStringExtra(EXTRA_SCENE_SPEECH_HIT_KEYWORDS);
                if (packageName.equals(mSceneSpeechImpl.mContext.getPackageName())) {
                    mSceneSpeechImpl.mSceneSpeechListener.onKeywordsHit(hitWords);
                }
            }
        }
    }
}
