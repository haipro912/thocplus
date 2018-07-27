package com.vttm.mochaplus.feature.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChannelModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url_avatar")
    @Expose
    private String urlAvatar;
    @SerializedName("is_follow")
    @Expose
    private Integer isFollow;
    @SerializedName("categoryid")
    @Expose
    private Integer categoryid;
    @SerializedName("numfollow")
    @Expose
    private Integer numfollow;
    @SerializedName("is_registered")
    @Expose
    private Integer isRegistered;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("isMyChannel")
    @Expose
    private Integer isMyChannel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Integer isFollow) {
        this.isFollow = isFollow;
    }

    public Integer getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Integer categoryid) {
        this.categoryid = categoryid;
    }

    public Integer getNumfollow() {
        return numfollow;
    }

    public void setNumfollow(Integer numfollow) {
        this.numfollow = numfollow;
    }

    public Integer getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(Integer isRegistered) {
        this.isRegistered = isRegistered;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsMyChannel() {
        return isMyChannel;
    }

    public void setIsMyChannel(Integer isMyChannel) {
        this.isMyChannel = isMyChannel;
    }
}
