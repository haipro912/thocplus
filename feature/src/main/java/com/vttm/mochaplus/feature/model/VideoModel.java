package com.vttm.mochaplus.feature.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VideoModel implements Serializable{
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("original_path")
    @Expose
    private String originalPath;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("image_path_small")
    @Expose
    private String image_path_small;
    @SerializedName("image_path_thump")
    @Expose
    private String image_path_thump;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("isView")
    @Expose
    private int isView;
    @SerializedName("is_like")
    @Expose
    private int isLike;
    @SerializedName("is_share")
    @Expose
    private int isShare;
    @SerializedName("is_follow")
    @Expose
    private int isFollow;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("aspecRatio")
    @Expose
    private String aspecRatio;
    @SerializedName("videoType")
    @Expose
    private String videoType;
    @SerializedName("total_like")
    @Expose
    private int totalLike;
    @SerializedName("total_view")
    @Expose
    private int totalView;
    @SerializedName("total_unlike")
    @Expose
    private int totalUnlike;
    @SerializedName("total_share")
    @Expose
    private int totalShare;
    @SerializedName("total_comment")
    @Expose
    private int totalComment;
    @SerializedName("numfollow")
    @Expose
    private int numfollow;
    @SerializedName("categories")
    @Expose
    private List<VideoCategoryModel> categories = null;
    @SerializedName("channels")
    @Expose
    private List<ChannelModel> channels = null;
    @SerializedName("filmGroupsID")
    @Expose
    private int filmGroupsID;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getIsView() {
        return isView;
    }

    public void setIsView(int isView) {
        this.isView = isView;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getIsShare() {
        return isShare;
    }

    public void setIsShare(int isShare) {
        this.isShare = isShare;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAspecRatio() {
        return aspecRatio;
    }

    public void setAspecRatio(String aspecRatio) {
        this.aspecRatio = aspecRatio;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public int getTotalView() {
        return totalView;
    }

    public void setTotalView(int totalView) {
        this.totalView = totalView;
    }

    public int getTotalUnlike() {
        return totalUnlike;
    }

    public void setTotalUnlike(int totalUnlike) {
        this.totalUnlike = totalUnlike;
    }

    public int getTotalShare() {
        return totalShare;
    }

    public void setTotalShare(int totalShare) {
        this.totalShare = totalShare;
    }

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }

    public int getNumfollow() {
        return numfollow;
    }

    public void setNumfollow(int numfollow) {
        this.numfollow = numfollow;
    }

    public List<VideoCategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<VideoCategoryModel> categories) {
        this.categories = categories;
    }

    public List<ChannelModel> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelModel> channels) {
        this.channels = channels;
    }

    public int getFilmGroupsID() {
        return filmGroupsID;
    }

    public void setFilmGroupsID(int filmGroupsID) {
        this.filmGroupsID = filmGroupsID;
    }

    public String getImage_path_small() {
        return image_path_small;
    }

    public void setImage_path_small(String image_path_small) {
        this.image_path_small = image_path_small;
    }

    public String getImage_path_thump() {
        return image_path_thump;
    }

    public void setImage_path_thump(String image_path_thump) {
        this.image_path_thump = image_path_thump;
    }
}
