package com.gyh.download.bilibili.bibili;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by  yahuigao
 * Date: 2019-12-17
 * Time: 11:13
 * Description:
 */
public class BilibiliUtil {

    // aid 和bvid 转换 api 格式 http://api.bilibili.com/x/web-interface/archive/stat?aid=170001
    // 或者http://api.bilibili.com/x/web-interface/archive/stat?bvid=BV17x411w7KC
    private String videobvid= "http://api.bilibili.com/x/web-interface/archive/stat";

    private String cookiesApi = "https://xqher.wodemo.net/entry/509318";
    private String dataApi = "https://api.bilibili.com/x/web-interface/view";
    private String defaultApi = "https://www.bilibili.com/video/av";
    private String downloadApi = "https://api.bilibili.com/x/player/playurl";
    private String getCookiesApi = "https://api.kaaass.net/biliapi/user/login";
    private String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

    public String DealInput(String paramString) {
        String str = "";
        Matcher localMatcher;
        if ((paramString.indexOf("av") != -1) && (!"av".equals(paramString))) {
            localMatcher = Pattern.compile("av(\\d+)*", Pattern.CASE_INSENSITIVE).matcher(paramString);
            paramString = str;
            if (localMatcher.find()) {
                str = localMatcher.group(1);
                paramString = str;
            }
        }

        return paramString;
    }

    public void errorDialog(Activity paramActivity, Exception paramException) {
        new AlertDialog.Builder(paramActivity).setTitle("cuowwu").setMessage(new StringBuffer().append(new StringBuffer().append("cuowwu").append(paramException.getCause().getClass()).toString()).append("\ncuowwu").toString() + paramException.getCause().getMessage()).setNegativeButton("cuowwu", (DialogInterface.OnClickListener) null).create().show();
    }

    public void existsFolder(String[] paramArrayOfString) {
        for (int i = 0; ; i++) {
            if (i >= paramArrayOfString.length) {
                return;
            }
            if (!new File(this.sdcard + paramArrayOfString[i]).exists()) {
                new File(this.sdcard + paramArrayOfString[i]).mkdirs();
            }
        }
    }

    public String getClipData(Context paramContext) {

        Object localObject = ((ClipboardManager) paramContext.getSystemService(Context.CLIPBOARD_SERVICE)).getPrimaryClip();
        String str = "";
        if (localObject != null) {
            localObject = ((ClipData) localObject).getItemAt(0);
            if (localObject != null) {
                str = ((ClipData.Item) localObject).getText().toString();
                if (str != null) {
                    if (!"".equals(str)) {
                        return str;
                    }
                }
            }
        }
        return null;

    }

    public String getCookiesApi() {
        return this.cookiesApi;
    }

    public String getDataApi() {
        return this.dataApi;
    }

    public String getDataSize(int paramInt) {
        int i = paramInt;
        if (paramInt < 0) {
            i = 0;
        }
        Object localObject = new DecimalFormat("####.0");
        if (i < 1024) {
            localObject = i + "bytes";
            return localObject.toString();
        }

        float f;
        if (i < Math.pow(1024, 2)) {
            f = i / 1024.0F;
            localObject = ((DecimalFormat) localObject).format(f) + "K";
        } else if (i < Math.pow(1024, 3)) {
            f = i / 1024 / 1024.0F;
            localObject = ((DecimalFormat) localObject).format(f) + "M";
        } else if (i < Math.pow(1024, 4)) {
            f = i / 1024 / 1024 / 1024.0F;
            localObject = ((DecimalFormat) localObject).format(f) + "G";
        } else {
            localObject = "size: error";
        }
        return localObject.toString();
    }

    public String getDefaultApi() {
        return this.defaultApi;
    }

    public String getDownloadApi() {
        return this.downloadApi;
    }

    public String getGetCookiesApi() {
        return this.getCookiesApi;
    }

    public String getNumber(String paramString) {
        Matcher localMatcher = Pattern.compile("(\\d+)*", Pattern.CASE_INSENSITIVE).matcher(paramString);
        if (!localMatcher.find()) {
            paramString = localMatcher.group(1);
        }
        return paramString;
    }

    public String getSdcard() {
        return this.sdcard;
    }

    public String getShareData(Activity paramActivity) {
        Intent intent = paramActivity.getIntent();
        String str1 = intent.getAction();
        String str2 = intent.getType();
        if ((str1 != null) && (str1.equals("android.intent.action.SEND")) && (str2 != null) && (str2.equals("text/plain"))) {
            String text = intent.getStringExtra("android.intent.extra.TEXT");
            if ((text == null) || ("".equals(text))) {
                return null;
            }
            return text;
        }
        return null;
    }

    public String getTimeFromMillisecond(Long paramLong) {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("mm:ss");
        if (paramLong > 3600000) {
            localSimpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        }
        return localSimpleDateFormat.format(new Date(((Long) paramLong).longValue()));
    }

    public boolean isAvNumber(String str) {
        if (str.contains("av")) {
            if ("av".equals(str)) {
                return false;
            }
            return Pattern.compile("av(\\d+)*", 2).matcher(str).find();
        } else {
            return isNumber(str);
        }
    }


    public boolean isBvNumber(String str) {
        String temp = str;
        temp = temp.toLowerCase();
        if (temp.contains("bv") && temp.startsWith("bv")) {
            if ("bv".equals(str)) {
                return false;
            }
            return Pattern.compile("bv(\\d+)*", 2).matcher(str).find();
        } else {
            return isNumber(temp);
        }
    }

    public boolean isNetworkAvalible(Activity paramActivity) {
        if (paramActivity == null) return false;
        ConnectivityManager connectivity = (ConnectivityManager) paramActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    public boolean isNumber(String paramString) {
        return Pattern.compile("^-?\\d+$", 2).matcher(paramString).matches();
    }

    public String saveBitmap(String filePath, Bitmap paramBitmap) {
        if (paramBitmap == null) return null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(filePath));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            fileOutputStream.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setCookiesApi(String paramString) {
        this.cookiesApi = paramString;
    }

    public void setDataApi(String paramString) {
        this.dataApi = paramString;
    }

    public void setDefaultApi(String paramString) {
        this.defaultApi = paramString;
    }

    public void setDownloadApi(String paramString) {
        this.downloadApi = paramString;
    }

    public void setGetCookiesApi(String paramString) {
        this.getCookiesApi = paramString;
    }

    public void setSdcard(String paramString) {
        this.sdcard = paramString;
    }

}
