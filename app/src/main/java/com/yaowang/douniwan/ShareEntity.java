package com.yaowang.douniwan;

import android.graphics.Bitmap;

/**
 * @author : created by chuangWu
 * @version : 0.01
 * @email : chuangwu127@gmail.com
 * @created time : 2015-08-24 15:58
 * @description : none
 * @for your attention : none
 * @revise : none
 */
public class ShareEntity {
    //标题
    private String title;
    //目标地址
    private String targetUrl;
    //内容
    private String summary;
    //网络图片地址
    private String imageUrl;
    //应用名
    private String appName;
    //本地图片地址
    private Bitmap imgBitmap;

    //id
    private String id;
    //类型
    private String type;

    //0微信分享、1微信朋友圈分享
    private int  flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        if(summary!=null && summary.length()>50)
            summary = summary.substring(0,50)+"...";
        this.summary = summary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
