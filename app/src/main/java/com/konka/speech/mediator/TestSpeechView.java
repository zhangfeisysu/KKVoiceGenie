package com.konka.speech.mediator;

import android.app.AlertDialog;
import android.content.Context;
import android.view.WindowManager;

/**
 * @author ZhangFei
 * @date 2017-11-9
 */

public class TestSpeechView extends BaseSpeechView {
    public TestSpeechView(Context context, SpeechMediator mediator) {
        super(context, mediator);
    }

    private void showToast(String text) {
//        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("线程名：" + Thread.currentThread().getName() + " and "+text);
        AlertDialog alert = builder.create();
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();

    }

    @Override
    public void show() {
        showToast("打开界面");
    }

    @Override
    public void dismiss() {
        showToast("关闭界面");
    }

    @Override
    public void performRecord() {
        showToast("播放录音动画");
    }

    @Override
    public void cancelRecord() {
        showToast("取消录音");
    }

    @Override
    public void performRecognize() {
        showToast("播放识别动画");
    }

    @Override
    public void cancelRecognize() {
        showToast("取消识别");
    }

    @Override
    public void performParse() {
        showToast("播放解析动画");
    }

    @Override
    public void cancelParse() {
        showToast("取消识别");
    }

    @Override
    public void showUserWord(Conversation conversation) {
        showToast("展示用户说法:" + conversation.getWord());
    }

    @Override
    public void showReply(Conversation conversation) {
        showToast("展示语音精灵回复：" + conversation.getReplyContent().toString());
    }
}
