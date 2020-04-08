package com.gyh.download.bilibili.viewmodel.tasks;

import android.os.Handler;
import android.os.Looper;


/**
 * Created by  yahuigao
 * Date: 2019-12-24
 * Time: 14:04
 * Description:
 */
public class BaseTask<T> implements Runnable {

    private final IExecute<T> mExecute;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public BaseTask(IExecute<T> execute) {
        mExecute = execute;
    }

    @Override
    public final void run() {
        end(execute());
    }

    private T execute() {
        return mExecute.executeOnSub();
    }

    private void end(final T t) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mExecute.endOnMain(t);
            }
        });

    }
}
