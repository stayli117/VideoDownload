package com.gyh.download.bilibili.model;

/**
 * Created by  yahuigao
 * Date: 2020/4/8
 * Time: 10:04 AM
 * Description:
 */
public class PageData {
    /**
     * cid : 91285683
     * page : 1
     * from : vupload
     * part : IMG_5910
     * duration : 87
     * vid :
     * weblink :
     * dimension : {"width":1920,"height":1080,"rotate":0}
     */

    private boolean checked;

    private int cid;
    private int page;
    private String from;
    private String part;
    private int duration;
    private String vid;
    private String weblink;
    private DimensionData dimension;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getWeblink() {
        return weblink;
    }

    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }

    public DimensionData getDimension() {
        return dimension;
    }

    public void setDimension(DimensionData dimension) {
        this.dimension = dimension;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
}
