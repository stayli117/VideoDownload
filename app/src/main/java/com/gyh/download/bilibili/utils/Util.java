package com.gyh.download.bilibili.utils;

import android.os.Environment;

import com.gyh.download.bilibili.bibili.BilibiliUtil;
import com.gyh.download.bilibili.bibili.VideoParser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by  yahuigao
 * Date: 2019-12-20
 * Time: 20:21
 * Description:
 */
public final class Util {
    private static final String TAG = "Util";

    private static final String SDCARD = Environment.getExternalStorageDirectory() + "/";
    private static final String ROOT_PATH = "Bilibili";
    private static final String COVER_PATH = ROOT_PATH + "/Cover";
    private static final String DOWNLOAD_PATH = ROOT_PATH + "/Download";
    private static final String DOUYIN_PATH = ROOT_PATH + "/Douyin";

    public static final String USER_AGENT="Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0";

    public static void createFolderRootPath() {
        existsFolder(new String[]{ROOT_PATH, COVER_PATH, DOWNLOAD_PATH});
    }

    private static void existsFolder(String[] paths) {
        for (String path : paths) {
            if (!new File(SDCARD + path).exists()) {
                new File(SDCARD + path).mkdirs();
            }
        }
    }

    public static String createDownFilePath(String parent, String fileName) {
        String downloadPath = Util.getDownloadPath() + "/" + parent;
        Util.existsFolder(new String[]{downloadPath});
        return downloadPath + "/" + fileName;
    }
    public static String createDouyinPath(String parent, String fileName) {
        String downloadPath = Util.getDouyinPath() + "/" + parent;
        Util.existsFolder(new String[]{downloadPath});
        return downloadPath + "/" + fileName;
    }

    public static String createCoverFilePath(String fileName) {
        String coverPath = Util.getCoverPath() + "/";
        Util.existsFolder(new String[]{coverPath});
        return coverPath + "/" + fileName;
    }
    private static String getDownloadPath() {
        return SDCARD + DOWNLOAD_PATH;
    }

    private static String getCoverPath() {
        return SDCARD + COVER_PATH;
    }

    private static String getDouyinPath(){
        return SDCARD+DOUYIN_PATH;
    }
    public static Connection getConnection(String downloadUrl, String cookies, String defaultApi) {
        Connection connect = Jsoup.connect(downloadUrl);

        connect.userAgent(USER_AGENT);


        connect.header("Accept", "*/*");
        connect.header("Accept-Encoding", "gzip, deflate, br");
        connect.header("Accept-Language", "zh-CN,zh;q=0.9");
        connect.header("Connection", "keep-alive");
        connect.header("Referer", defaultApi);
        connect.header("SESSDATA", cookies);
        connect.header("Origin", "https://www.bilibili.com");
        connect.timeout(3600000);
        connect.ignoreContentType(true);
        connect.ignoreHttpErrors(true);

        return connect;
    }

    public static ExecutorService getSingleThreadPool() {
        return Executors.newSingleThreadExecutor();
    }

    public static Map<String, String> getHeader() {
        String cookies = VideoParser.getInstance().getCookies();
        String defaultApi = new BilibiliUtil().getDefaultApi();
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Accept-Encoding", "gzip, deflate, br");
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        header.put("Connection", "keep-alive");
        header.put("Referer", defaultApi);
        header.put("SESSDATA", cookies);
        header.put("Origin", "https://www.bilibili.com");
        return header;
    }


    public static String formatSeconds(long seconds) {
        String standardTime;
        if (seconds <= 0) {
            standardTime = "00:00";
        } else if (seconds < 60) {
            standardTime = String.format(Locale.getDefault(), "00:%02d", seconds % 60);
        } else if (seconds < 3600) {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
        } else {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", seconds / 3600, seconds % 3600 / 60, seconds % 60);
        }
        return standardTime;
    }
}
