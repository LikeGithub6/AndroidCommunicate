package com.example.teamwork;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LtplBean {
    private String id;
    private String imgurl;
    private String nicheng;
    private String neiorng;
    private Bitmap tupianbit;
    private Boolean isload;

    public LtplBean(String id, String imgurl, String nicheng, String neiorng, Bitmap tupianbit) {
        this.id = id;
        this.imgurl = imgurl;
        this.nicheng = nicheng;
        this.neiorng = neiorng;
        this.tupianbit = tupianbit;
        isload=false;
    }

    public Boolean getIsload() {
        return isload;
    }

    public void setIsload(Boolean isload) {
        this.isload = isload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getNicheng() {
        return nicheng;
    }

    public void setNicheng(String nicheng) {
        this.nicheng = nicheng;
    }

    public String getNeiorng() {
        return neiorng;
    }

    public void setNeiorng(String neiorng) {
        this.neiorng = neiorng;
    }

    public Bitmap getTupianbit() {
        return tupianbit;
    }

    public void setTupianbit(Bitmap tupianbit) {
        this.tupianbit = tupianbit;
    }
}
