package com.gyh.download.douyin;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaFileHelper;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.arialyy.aria.util.CommonUtil;

import java.io.File;

/**
 * Created by  yahuigao
 * Date: 2019-12-11
 * Time: 14:13
 * Description:
 */
public class AndroidDownloadManager {

    private DownloadManager downloadManager;
    private Context context;
    private long downloadId;
    private String url;
    private String filePath;

    private String path;

    private AndroidDownloadManagerListener listener;


    private Handler mHandlerProgress = new Handler(Looper.getMainLooper());
    private Runnable mRunnableProgress = new Runnable() {
        @Override
        public void run() {
            if (downloadId <= 0) return;
            int[] status = getBytesAndStatus(downloadId);
            int percent = switchPercent(status[0], status[1]);
            if (percent>=100){
                mHandlerProgress.removeCallbacksAndMessages(null);
                return;
            }
            mHandlerProgress.postDelayed(this, 16);
            String cuFileSize = CommonUtil.formatFileSize(status[0]);
            String toFileSize = CommonUtil.formatFileSize(status[1]);
            if (listener != null) {
                listener.onProgress(percent, cuFileSize,toFileSize);
            }
        }
    };

    public AndroidDownloadManager(Context context, String url) {
        this(context, url, getFileNameByUrl(url));
    }

    public AndroidDownloadManager(Context context, String url, String filePath) {
        this.context = context;
        this.url = url;
        this.filePath = filePath;
    }

    public AndroidDownloadManager setListener(AndroidDownloadManagerListener listener) {
        this.listener = listener;
        return this;
    }

    private static final String TAG = "AndroidDownloadManager";

    /**
     * 开始下载
     */
    public void download() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("douyin");
        request.setDescription("文件正在下载中......");
        request.setVisibleInDownloadsUi(true);

        //设置下载的路径
        File file = new File(filePath);
        Log.d(TAG, "download: filePath " + filePath);
        request.setDestinationUri(Uri.fromFile(file));
        path = file.getAbsolutePath();

        //获取DownloadManager
        if (downloadManager == null) {
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        }
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        if (downloadManager != null) {
            if (listener != null) {
                listener.onPrepare();
            }
            downloadId = downloadManager.enqueue(request);
            mHandlerProgress.post(mRunnableProgress);
        }

        //注册广播接收者，监听下载状态
        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{-1, -1, 0};
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return bytesAndStatus;
    }

    public int switchPercent(long progress, long max) {
        int rate = 0;
        if (progress <= 0 || max <= 0) {
            rate = 0;
        } else if (progress > max) {
            rate = 100;
        } else {
            rate = (int) ((double) progress / max * 100);
        }
        if (rate > 98) {
            return rate;
        } else {
            return rate + 1;
        }
    }

    //广播监听下载的各个状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager.Query query = new DownloadManager.Query();
            //通过下载的id查找
            query.setFilterById(downloadId);
            Cursor cursor = downloadManager.query(query);
            if (cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    //下载暂停
                    case DownloadManager.STATUS_PAUSED:
                        break;
                    //下载延迟
                    case DownloadManager.STATUS_PENDING:
                        break;
                    //正在下载
                    case DownloadManager.STATUS_RUNNING:
                        break;
                    //下载完成
                    case DownloadManager.STATUS_SUCCESSFUL:

                        String type = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                        String fileType = MediaFileHelper.getInstance().getFileType(type);

                        File file = new File(path);
                        String newName = path + "." + fileType;
                        File file1 = new File(newName);
                        boolean rename = file.renameTo(file1);
                        Log.d(TAG, "onReceive: type " + type + " fileType " + fileType);
                        if (listener != null) {
                            listener.onSuccess(file1);
                        }
                        cursor.close();
                        context.unregisterReceiver(receiver);
                        break;
                    //下载失败
                    case DownloadManager.STATUS_FAILED:
                        if (listener != null) {
                            listener.onFailed(new Exception("下载失败"));
                        }
                        cursor.close();
                        context.unregisterReceiver(receiver);
                        break;
                }
            }
        }
    };


    // ——————————————————————私有方法———————————————————————

    /**
     * 通过URL获取文件名
     *
     * @param url 下载地址
     * @return 文件名字
     */
    private static String getFileNameByUrl(String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        boolean b = !filename.contains("?");
        filename = filename.substring(0, b ? filename.length() : filename.indexOf("?"));
        return filename;
    }

}
