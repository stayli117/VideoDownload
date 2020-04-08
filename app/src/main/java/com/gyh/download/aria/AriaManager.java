package com.gyh.download.aria;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.HttpOption;
import com.arialyy.aria.core.task.DownloadTask;

/**
 * Created by  yahuigao
 * Date: 2019-12-25
 * Time: 11:28
 * Description:
 */
public class AriaManager {
    private long mTaskId = -1;
    private TaskListener mTaskListener;

    interface HOLDER {
        AriaManager aria = new AriaManager();
    }

    public static AriaManager get() {
        return HOLDER.aria;
    }

    private AriaManager() {

    }

    @Download.onWait
    void onWait(DownloadTask task) {
        mTaskListener.onWait(task);
    }

    @Download.onPre
    void onPre(DownloadTask task) {
        mTaskListener.onPre(task);
    }

    @Download.onTaskStart
    void taskStart(DownloadTask task) {
        mTaskListener.taskStart(task);
    }

    @Download.onTaskRunning
    void running(DownloadTask task) {
        mTaskListener.running(task);
    }

    @Download.onTaskResume
    void taskResume(DownloadTask task) {
        mTaskListener.taskResume(task);
    }

    @Download.onTaskStop
    void taskStop(DownloadTask task) {
        mTaskListener.taskStop(task);
    }

    @Download.onTaskCancel
    void taskCancel(DownloadTask task) {
        mTaskListener.taskCancel(task);
    }

    @Download.onTaskFail
    void taskFail(DownloadTask task) {
        mTaskListener.taskFail(task);
        cancel(true);
    }

    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        mTaskListener.taskComplete(task);
        cancel(false);
    }


    public boolean isRunning() {
        return Aria.download(this).load(mTaskId).isRunning();
    }

    public boolean isExists() {
        return Aria.download(this).load(mTaskId).taskExists();
    }

    public void start(String url, String filePath,HttpOption httpOption) {
        mTaskId = Aria.download(this)
                .load(url)
                .option(httpOption)
                .setFilePath(filePath)
                .resetState()
                .ignoreFilePathOccupy()
                .create();
    }

    public void stop() { // 暂停
        Aria.download(this).load(mTaskId).stop();
    }

    public void resume() { // 恢复
        Aria.download(this).load(mTaskId).resume();
    }

    public void cancel(boolean removeFile) { // 取消 删除
        Aria.download(this).load(mTaskId).cancel(removeFile);
    }


    public void register(TaskListener taskListener) {
        mTaskListener = taskListener; // 注册
        Aria.download(this).register();
    }

    public void unRegister() { // 反注册
        Aria.download(this).unRegister();
    }

    public interface TaskListener<DownloadTask> {

        void onWait(com.arialyy.aria.core.task.DownloadTask task);

        void onPre(com.arialyy.aria.core.task.DownloadTask task);

        void taskStart(com.arialyy.aria.core.task.DownloadTask task);

        void running(com.arialyy.aria.core.task.DownloadTask task);

        void taskResume(com.arialyy.aria.core.task.DownloadTask task);

        void taskStop(com.arialyy.aria.core.task.DownloadTask task);

        void taskCancel(com.arialyy.aria.core.task.DownloadTask task);

        void taskFail(com.arialyy.aria.core.task.DownloadTask task);

        void taskComplete(com.arialyy.aria.core.task.DownloadTask task);
    }
}
