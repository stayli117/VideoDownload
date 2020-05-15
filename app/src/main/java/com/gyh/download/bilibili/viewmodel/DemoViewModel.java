package com.gyh.download.bilibili.viewmodel;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.gyh.download.bilibili.bibili.Data;
import com.gyh.download.bilibili.bibili.InputData;
import com.gyh.download.bilibili.bibili.QualityData;
import com.gyh.download.bilibili.bibili.VideoParser;
import com.gyh.download.bilibili.model.PageData;
import com.gyh.download.bilibili.model.VideoDownloadData;
import com.gyh.download.bilibili.model.VideoInfoData;
import com.gyh.download.bilibili.utils.Util;
import com.gyh.download.bilibili.viewmodel.tasks.BaseTask;
import com.gyh.download.bilibili.viewmodel.tasks.IExecute;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by  yahuigao
 * Date: 2019-12-24
 * Time: 15:47
 * Description:
 */
public class DemoViewModel extends ViewModel {

    private static final String TAG = "DemoViewModel";
    private final MediatorLiveData<Data> mVideoLiveData = new MediatorLiveData<>();

    private VideoParser mVideoParser;
    private ExecutorService mService;

    public DemoViewModel() {
        mVideoParser = VideoParser.getInstance();
        mService = Util.getSingleThreadPool();
        parseCookies();
    }

    public LiveData<Data> getVideoData() {
        return mVideoLiveData;
    }

    private void parseCookies() {

        BaseTask<String> task = new BaseTask<>(new IExecute<String>() {
            @Override
            public String executeOnSub() {
                Document cookiesDocument = mVideoParser.getCookiesDocument();
                if (cookiesDocument == null) return null;
                Elements elementsByClass = cookiesDocument.getElementsByClass("wo-entry-section wo-text-markdown");
                if (elementsByClass == null) return null;
                return elementsByClass.text();
            }

            @Override
            public void endOnMain(String cookies) {
                mVideoParser.setCookies(cookies);
                Log.d(TAG, "parseCookies: cookies text " + cookies);
            }
        });
        mService.execute(task);
    }

    public void loadVideoData(final InputData inputData) {
        BaseTask<Data> task = new BaseTask<>(new IExecute<Data>() {
            @Override
            public Data executeOnSub() {
                String cookies = mVideoParser.getCookies();
                if (TextUtils.isEmpty(cookies)) {
                    Log.d(TAG, "executeOnSub: cookies =" + cookies);
                    return null;
                }
                Data data = new Data();
                String dataDocument = mVideoParser.getDataDocument(inputData);
                Gson gson = new Gson();
                VideoInfoData videoInfoData = gson.fromJson(dataDocument, VideoInfoData.class);

                int code = videoInfoData.getCode();
                if (code != 0) {
                    data.setTitle("视频信息获取错误");
                    return data;
                }
                covertData(data, videoInfoData);
                inputData.setQuality(data.getQuality());
                inputData.setCid(data.getCid());
                inputData.setAid(data.getAid());
                parseDownloadUrlData(data, inputData);

                return data;
            }

            @Override
            public void endOnMain(Data data) {
                mVideoLiveData.setValue(data);
            }
        });
        mService.execute(task);
    }


    public void updateVideoData(final InputData inputData) {
        BaseTask<Data> task = new BaseTask<>(new IExecute<Data>() {
            @Override
            public Data executeOnSub() {
                Data data = mVideoLiveData.getValue();
                if (data != null) {
                    data.setCid(inputData.getCid());
                    data.setQuality(inputData.getQuality());
                    parseDownloadUrlData(data, inputData);
                }
                return data;
            }

            @Override
            public void endOnMain(Data data) {
                mVideoLiveData.setValue(data);
            }
        });
        mService.execute(task);
    }

    private void parseDownloadUrlData(Data data, InputData inputData) {
        Gson gson = new Gson();
        String urlDocument = mVideoParser.getUrlDocument(inputData);
        VideoDownloadData videoDownloadData = gson.fromJson(urlDocument, VideoDownloadData.class);
        covertData(data, videoDownloadData);

    }

    private void covertData(Data data, VideoInfoData videoInfoData) {
        VideoInfoData.DataBean infoData = videoInfoData.getData();
        data.setTitle(infoData.getTitle());
        data.setCid(String.valueOf(infoData.getCid()));
        data.setAid(String.valueOf(infoData.getAid()));
        data.setBid(infoData.getBvid());
        data.setPageDataList(infoData.getPages());
        data.setCoverLink(infoData.getPic());
        data.setQuality(16); // 设置默认值
    }

    private void covertData(Data data, VideoDownloadData videoDownloadData) {
        VideoDownloadData.DataBean infoData = videoDownloadData.getData();
        VideoDownloadData.DataBean.DurlBean durlBean = infoData.getDurl().get(0);
        data.setSize(durlBean.getSize());
        List<String> backup_url = durlBean.getBackup_url();
        String downloadUrl = durlBean.getUrl();
        if (downloadUrl != null && backup_url.size() > 0) {
            downloadUrl = backup_url.get(0);
        }

        Log.d(TAG, "covertData: downloadUrl " + downloadUrl + " backup_url " + backup_url);
        data.setDownloadUrl(downloadUrl);
        data.setQuality(infoData.getQuality());
        List<String> accept_description = infoData.getAccept_description();
        List<Integer> accept_quality = infoData.getAccept_quality();
        int size = accept_quality.size();
        List<QualityData> qualityDataList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Integer integer = accept_quality.get(i);
            String des = accept_description.get(i);
            int quality = data.getQuality();
            boolean checked = quality == integer;
            qualityDataList.add(new QualityData(integer, des, checked));
        }
        Collections.reverse(qualityDataList);
        data.setQualitys(qualityDataList);

        List<PageData> pages = data.getPageDataList();
        for (PageData page : pages) {
            boolean checked = String.valueOf(page.getCid()).equals(data.getCid());
            page.setChecked(checked);
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();

    }
}
