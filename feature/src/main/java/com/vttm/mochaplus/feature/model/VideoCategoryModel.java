package com.vttm.mochaplus.feature.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoCategoryModel implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("parentid")
    @Expose
    private int parentid;
    @SerializedName("categoryname")
    @Expose
    private String categoryname;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("categoryid")
    @Expose
    private int categoryid;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("order")
    @Expose
    private int order;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("url_images")
    @Expose
    private String urlImages;
    @SerializedName("settop")
    @Expose
    private int settop;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(String urlImages) {
        this.urlImages = urlImages;
    }

    public int getSettop() {
        return settop;
    }

    public void setSettop(int settop) {
        this.settop = settop;
    }
}
