package com.gyh.download.douyin;

import java.io.File;

/**
 * Created by  yahuigao
 * Date: 2019-12-11
 * Time: 14:14
 * Description:
 */
public interface AndroidDownloadManagerListener {
    void onPrepare();

    void onSuccess(File file);

    void onFailed(Throwable throwable);

    void onProgress(int current, String cuFileSize, String toFileSize);
}
