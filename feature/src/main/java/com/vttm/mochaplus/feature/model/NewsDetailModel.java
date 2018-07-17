package com.vttm.mochaplus.feature.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by HaiKE on 8/19/17.
 */

public class NewsDetailModel implements Serializable
{

    @SerializedName("Content")
    @Expose
    private String content;
    @SerializedName("Height")
    @Expose
    private int height;
    @SerializedName("Media")
    @Expose
    private String media;
    @SerializedName("Poster")
    @Expose
    private String poster;
    @SerializedName("Type")
    @Expose
    private int type;
    @SerializedName("Width")
    @Expose
    private int width;
    private final static long serialVersionUID = 8266931797940501935L;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "";
    }

    public NewsDetailModel(String content, int height, String media, String poster, int type, int width) {
        this.content = content;
        this.height = height;
        this.media = media;
        this.poster = poster;
        this.type = type;
        this.width = width;
    }
}