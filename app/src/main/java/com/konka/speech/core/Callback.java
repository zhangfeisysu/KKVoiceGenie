package com.konka.speech.core;

/**
 * 通用回调方法
 *
 * @param <T> 完成回调后的结果类型
 * @param <S> 发生异常试的错误类型
 * @author ZhangFei
 * @date 2017-11-9
 */

public interface Callback<T, S> {
    /**
     * 操作完成时的回调。不管是成功还是失败。
     *
     * @param result
     */
    void onCompleted(T result);

    /**
     * 发生异常时才在此回调异常给调用者
     *
     * @param error
     */
    void onError(S error);
}
