package com.konka.speech.core;

/**
 * 抽象出每次对话请求，采用一问一答的模式，每段对话包含唯一的sessionId。
 *
 * @author ZhangFei
 * @date 2017-11-6
 */

class Conversation {
    private int mSessionId;
    /**
     * 用户的说法，包括问题，命令
     */
    private String mWord;
    /**
     * 用户的意图，表示用户的说法代表什么意思
     */
    private int mIntent;
    /**
     * 对话输出类型，包括文本回复，json数据资源，控件
     */
    private int mReplyType;
    /**
     * 对话输出具体内容，包括文本回复，json数据资源，控件
     */
    private Object mReplyContent;
    /**
     * 方案：标准，全程，全局，多轮
     */
    private int mScheme;
    private boolean mCanceled;
    private VoiceGenie mVoiceGenie;

    Conversation(int sessionId, VoiceGenie voiceGenie) {
        mSessionId = sessionId;
        mVoiceGenie = voiceGenie;
    }

    public boolean isCanceled() {
        return mCanceled;
    }

    public void cancel() {
        mCanceled = true;
    }

    public int getSessionId() {
        return mSessionId;
    }

    public VoiceGenie getVoiceGenie() {
        return mVoiceGenie;
    }

    public String getWord() {
        return mWord;
    }

    public void setWord(String word) {
        mWord = word;
    }

    public int getReplyType() {
        return mReplyType;
    }

    public void setReplyType(int replyType) {
        mReplyType = replyType;
    }


    public int getScheme() {
        return mScheme;
    }

    public void setScheme(int scheme) {
        mScheme = scheme;
    }

    public Object getReplyContent() {
        return mReplyContent;
    }

    public void setReplyContent(Object replyContent) {
        mReplyContent = replyContent;
    }
}
