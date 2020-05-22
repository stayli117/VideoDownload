package com.gyh.download.bilibili.bibili;

import com.gyh.download.bilibili.model.PageData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  yahuigao
 * Date: 2019-12-17
 * Time: 11:34
 * Description:
 */
public class Data {

    private String aid = "";
    private String bid = "";
    private String cid = "";
    private String coverLink = "";
    private String downloadUrl = "";
    private List<PageData> pageDataList;
    private int quality = 0;
    private List<QualityData> qualitys = new ArrayList<>();
    private long size = 0;
    private String title = "";
    private String format = "flv";

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public String getAid() {
        return this.aid;
    }

    public String getCid() {
        return this.cid;
    }

    public String getCoverLink() {
        return this.coverLink;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public int getQuality() {
        return this.quality;
    }

    public List<QualityData> getQualitys() {
        return this.qualitys;
    }

    public long getSize() {
        return this.size;
    }

    public String getTitle() {
        return this.title;
    }

    public void setAid(String paramString) {
        this.aid = paramString;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBid() {
        return bid;
    }

    public void setCid(String paramString) {
        this.cid = paramString;
    }

    public void setCoverLink(String paramString) {
        this.coverLink = paramString;
    }

    public void setDownloadUrl(String paramString) {
        this.downloadUrl = paramString;
    }

    public void setPageDataList(List<PageData> pageDataList) {
        this.pageDataList = pageDataList;
    }

    public List<PageData> getPageDataList() {
        return pageDataList;
    }

    public void setQuality(int paramInt) {
        this.quality = paramInt;
    }

    public void setQualitys(List<QualityData> paramList) {
        this.qualitys = paramList;
    }

    public void setSize(long paramInt) {
        this.size = paramInt;
    }

    public void setTitle(String paramString) {
        this.title = paramString;
    }
}
