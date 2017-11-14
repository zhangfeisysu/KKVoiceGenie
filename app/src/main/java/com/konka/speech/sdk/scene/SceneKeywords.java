package com.konka.speech.sdk.scene;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangFei
 * @date 2017-11-13
 */

public class SceneKeywords {
    private String mPackageName;
    private List<String> mKeywordsList;

    public SceneKeywords() {
        mKeywordsList = new ArrayList<>(10);
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    public List<String> getKeywordsList() {
        return mKeywordsList;
    }

    public void setKeywordsList(List<String> keywordsList) {
        mKeywordsList = keywordsList;
    }
}
