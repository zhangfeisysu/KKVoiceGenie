package com.konka.speech.scene.constant;

/**
 * 全程语音Intent常量，包括广播的action，extra，service的action等
 *
 * @author ZhangFei
 * @date 2017-11-13
 */

public class IntentConstants {
    /**
     * 上传全程语音Action
     */
    public static final String ACTION_SCENE_SPEECH_UPLOAD_KEYWORDS = "konka.speech.intent.action.scene_speech.UPLOAD_KEYWORDS";
    /**
     * 全程语音关键词Extra
     */
    public static final String EXTRA_SCENE_SPEECH_KEYWORDS = "konka.speech.intent.extra.scene_speech.KEYWORDS";
    /**
     * 全程语音反馈被命中的关键词Action
     */
    public static final String ACTION_SCENE_SPEECH_FEEDBACK = "konka.speech.intent.action.scene_speech.FEEDBACK";

    /**
     * 全程语音被命中的关键词Extra
     */
    public static final String EXTRA_SCENE_SPEECH_HIT_KEYWORDS = "konka.speech.extra.scene_speech.HIT_KEYWORDS";
}
