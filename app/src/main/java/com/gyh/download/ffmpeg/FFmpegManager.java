package com.gyh.download.ffmpeg;

import io.microshow.rxffmpeg.RxFFmpegCommandList;
import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegProgress;
import io.reactivex.Flowable;

/**
 * Created by  yahuigao
 * Date: 2020/4/9
 * Time: 1:46 PM
 * Description:
 */
public class FFmpegManager {

    // 转码
    public static String[] getTranscoding(String srcPath, String destPath) {
        RxFFmpegCommandList cmdlist = new RxFFmpegCommandList();
        cmdlist.append("-i");
        cmdlist.append(srcPath);
        cmdlist.append(destPath);
        return cmdlist.build();
    }

    public static Flowable<RxFFmpegProgress> invokeRxJava(final String[] command) {
        return RxFFmpegInvoke.getInstance()
                .runCommandRxJava(command);

    }

    public static void invokeAsync(final String[] command) {
        RxFFmpegInvoke.getInstance().runCommandAsync(command, new RxFFmpegInvoke.IFFmpegListener() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onProgress(int progress, long progressTime) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String message) {

            }
        });

    }


    public static String getMediaInfo(String filePath) {
        return RxFFmpegInvoke.getInstance().getMediaInfo(filePath);
    }
}
