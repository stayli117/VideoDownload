package com.gyh.download.bilibili.viewmodel.tasks;

/**
 * Created by  yahuigao
 * Date: 2019-12-24
 * Time: 14:05
 * Description:
 */
public interface IExecute<T> {

    T executeOnSub();

    void endOnMain(T t);
}
