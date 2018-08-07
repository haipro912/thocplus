package com.vttm.mochaplus.feature.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChannelModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url_avatar")
    @Expose
    private String urlAvatar;
    @SerializedName("is_follow")
    @Expose
    private int isFollow;
    @SerializedName("categoryid")
    @Expose
    private int categoryid;
    @SerializedName("numfollow")
    @Expose
    private int numfollow;
    @SerializedName("is_registered")
    @Expose
    private int isRegistered;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("isMyChannel")
    @Expose
    private int isMyChannel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getNumfollow() {
        return numfollow;
    }

    public void setNumfollow(int numfollow) {
        this.numfollow = numfollow;
    }

    public int getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(int isRegistered) {
        this.isRegistered = isRegistered;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsMyChannel() {
        return isMyChannel;
    }

    public void setIsMyChannel(int isMyChannel) {
        this.isMyChannel = isMyChannel;
    }
}
