package com.konka.speech.sdk.scene;

import android.content.Context;

import java.util.List;

/**
 * 全程语音控制中心
 *
 * @author ZhangFei
 * @date 2017-11-10
 */

public class SceneSpeechManager {
    private Context mContext;
    private SceneSpeechMode mSceneSpeechMode;

    public SceneSpeechManager(Context context) {
        mContext = context;
        mSceneSpeechMode = new SceneSpeechModeImpl(context);
    }

    /**
     * 获取第一个匹配的关键词
     *
     * @param word 用户的说法
     * @return 第一个匹配的关键词
     */
    public SceneKeywords getSceneKeywords(String word) {
        Object keywords = mSceneSpeechMode.getKeywords();
        if (keywords instanceof SceneKeywords) {
            List<String> keywordsList = ((SceneKeywords) keywords).getKeywordsList();
            if (keywordsList != null && keywordsList.contains(word)) {
                return (SceneKeywords) keywords;
            }

        }
        return null;
    }

    /**
     * 获取包含某个关键词的列表
     *
     * @param word   关键词
     * @param result 包含某个关键词的列表
     */
    public void getSceneKeywords(String word, List<SceneKeywords> result) {
        if (result != null) {
            Object keywords = mSceneSpeechMode.getKeywords();
            if (keywords instanceof SceneKeywords) {
                List<String> keywordsList = ((SceneKeywords) keywords).getKeywordsList();
                if (keywordsList != null && keywordsList.contains(word)) {
                    result.add((SceneKeywords) keywords);
                }
                result.add((SceneKeywords) keywords);
            } else if (keywords instanceof List) {
                for (SceneKeywords sceneKeywords : ((List<SceneKeywords>) keywords)) {
                    if (sceneKeywords.getKeywordsList().contains(word)) {
                        result.add(sceneKeywords);
                    }
                }
            }
        }
    }

    /**
     * 关键词命中，反馈给第三方进程
     *
     * @param pkgName  第三方进程包名
     * @param keywords 关键词
     */
    public void hitKeywords(String pkgName, String keywords) {
        mSceneSpeechMode.feedback(pkgName, keywords);
    }

    /**
     * 对应进程是否注册过某关键词
     *
     * @param words 关键词
     * @return 是否注册过
     */
    public boolean containKeywords(String words) {
        Object keywords = mSceneSpeechMode.getKeywords();
        if (keywords instanceof SceneKeywords) {
            List<String> keywordsList = ((SceneKeywords) keywords).getKeywordsList();
            return keywordsList != null && keywordsList.contains(words);
        } else if (keywords instanceof List) {
            for (SceneKeywords sceneKeywords : ((List<SceneKeywords>) keywords)) {
                if (sceneKeywords.getKeywordsList().contains(words)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * reset关键词
     *
     * @param pkgName
     * @return
     */
    public boolean resetPkgSceneKeywords(String pkgName) {
        return mSceneSpeechMode.resetKeywords(pkgName);
    }

    /**
     * 清理操作
     */
    public void release() {
        mSceneSpeechMode.release();
    }
}
