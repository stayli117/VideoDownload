package com.gyh.download.bilibili.bibili;

import android.text.TextUtils;

import com.gyh.download.bilibili.model.PageData;

import java.util.List;

/**
 * Created by  yahuigao
 * Date: 2020/4/8
 * Time: 9:39 AM
 * Description:
 */
public class InputData {

    private String aid;
    private String bid;
    private String cid;
    private List<QualityData> qualitys;
    private int quality;
    private List<PageData> pages;

    public void setBid(String bid) {
        this.bid = bid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getBid() {
        return bid;
    }

    public String getAid() {
        return aid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setQualitys(List<QualityData> qualitys) {
        this.qualitys = qualitys;
    }

    public List<QualityData> getQualitys() {
        return qualitys;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getQuality() {
        return quality;
    }

    public void setPages(List<PageData> pages) {
        this.pages = pages;
    }

    public List<PageData> getPages() {
        return pages;
    }



    public boolean isEmptyAid() {
        return TextUtils.isEmpty(aid);
    }

    public boolean isEmptyBid() {
        return TextUtils.isEmpty(bid);
    }


}
