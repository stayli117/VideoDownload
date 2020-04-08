package com.gyh.download.bilibili.bibili;

/**
 * Created by  yahuigao
 * Date: 2020/4/8
 * Time: 10:37 AM
 * Description:
 */
public class QualityData {

    private String description;

    private int quality;

    private boolean checked;

    public QualityData(int quality, String description, boolean checked) {
        this.checked = checked;
        this.description = description;
        this.quality = quality;
    }

    public String getDescription() {
        return description;
    }

    public int getQuality() {
        return quality;
    }

    public boolean isChecked() {
        return checked;
    }
}
