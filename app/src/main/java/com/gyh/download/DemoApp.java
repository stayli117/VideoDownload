package com.gyh.download;

import android.app.Application;

import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.config.DownloadConfig;
import com.gyh.download.bilibili.utils.Util;

import io.microshow.rxffmpeg.RxFFmpegInvoke;

/**
 * Created by  yahuigao
 * Date: 2019-12-24
 * Time: 15:29
 * Description:
 */
public class DemoApp extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
        Util.createFolderRootPath();
        RxFFmpegInvoke.getInstance().setDebug(false);
        Aria.init(this);
        DownloadConfig downloadConfig = Aria.get(this).getDownloadConfig();
        downloadConfig.setConvertSpeed(true);
        //参数初始化
        int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        //核心线程数量大小
        int corePoolSize = Math.max(2, Math.min(CPU_COUNT - 1, 4));
        downloadConfig.setThreadNum(corePoolSize);
    }
}
