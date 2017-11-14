package com.konka.speech.mediator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.affy.zlogger.ZLogger;
import com.konka.speech.mediator.dui.DuiSemanticProcessor;
import com.konka.speech.mediator.dui.DuiSpeechRecognizer;
import com.konka.speech.mediator.dui.DuiTTSEngine;

/**
 * 负责运行整个语音精灵的基础Service，同时也是供外部唤醒的入口Service
 *
 * @author ZhangFei
 * @date 2017-11-09
 */
public class SpeechService extends Service {
    private VoiceGenie mVoiceGenie;
    private boolean mInitSuccess;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ZLogger.init("Speech").methodCount(1).hideThreadInfo();
        mVoiceGenie = new VoiceGenie(this);
        chooseSpeechEngine();
    }

    /**
     * 选择和切换语音引擎
     */
    private void chooseSpeechEngine() {
        BaseSpeechRecognizer speechRecognizer = new DuiSpeechRecognizer(this, mVoiceGenie);
        BaseSemanticProcessor semanticProcessor = new DuiSemanticProcessor(this, mVoiceGenie);
        BaseTTSEngine ttsEngine = new DuiTTSEngine(this, mVoiceGenie);
        mVoiceGenie.setSpeechRecognizer(speechRecognizer);
        mVoiceGenie.setSemanticProcessor(semanticProcessor);
        mVoiceGenie.setTTSEngine(ttsEngine);

        mVoiceGenie.initEngine(new Callback<Boolean, String>() {
            @Override
            public void onCompleted(Boolean result) {
                mInitSuccess = result;
                if (result) {
                    ZLogger.d("引擎初始化成功");
                } else {
                    ZLogger.d("引擎初始化失败");
                }
            }

            @Override
            public void onError(String error) {
                ZLogger.d("引擎初始化发生异常:" + error);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        while (!mInitSuccess) {
            //暂时这么瞎搞
        }
        if (intent.getStringExtra("act").equals("cancel")) {
            mVoiceGenie.cancelRecognize();
        } else {
            mVoiceGenie.startRecognize(intent);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVoiceGenie.releaseEngine();
    }
}
