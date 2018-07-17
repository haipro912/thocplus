package com.vttm.mochaplus.feature.model;

/**
 * Created by HaiKE on 8/19/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vttm.mochaplus.feature.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsModel implements Serializable {
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName;
    @SerializedName("Cid")
    @Expose
    private int cid = 0;
    @SerializedName("Comment")
    @Expose
    private int comment;
    @SerializedName("Content")
    @Expose
    private String content;
    @SerializedName("DateAlarm")
    @Expose
    private String dateAlarm;
    @SerializedName("DatePub")
    @Expose
    private String datePub;
    @SerializedName("ID")
    @Expose
    private int iD = 0;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Image169")
    @Expose
    private String image169;
    @SerializedName("Like")
    @Expose
    private int like;
    @SerializedName("Media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("Pid")
    @Expose
    private int pid = 0;
    @SerializedName("Position")
    @Expose
    private int position = 0;
    @SerializedName("Reads")
    @Expose
    private int reads;
    @SerializedName("Shapo")
    @Expose
    private String shapo;
    @SerializedName("Title")
    @Expose
    private String title;

    @SerializedName("Quote")
    @Expose
    private String quote;

    @SerializedName("SourceName")
    @Expose
    private String sourceName;

    @SerializedName("Poster")
    @Expose
    private String poster;

    @SerializedName("Url")
    @Expose
    private String url;

    @SerializedName("Duration")
    @Expose
    private String duration;

    @SerializedName("cateevent")
    @Expose
    private int cateevent;
    @SerializedName("idStory")
    @Expose
    private int idStory;
    @SerializedName("isRead")
    @Expose
    private int isRead;
    @SerializedName("is_nghe")
    @Expose
    private int isNghe;
    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("TypeIcon")
    @Expose
    private int typeIcon;

    @SerializedName("Timestamp")
    @Expose
    private long timeStamp;

    @SerializedName("SourceIcon")
    @Expose
    private String sourceIcon;

    @SerializedName("sid")
    @Expose
    private String sid;

    @SerializedName("unixTime")
    @Expose
    private long unixTime;

    private int readFromSource;

    private boolean isMarkRead;

    private boolean isPlayVideoOfNewsDetail;

    private boolean isPositionFirst;


    private int parentId;

    public boolean isPositionFirst() {
        return isPositionFirst;
    }

    public void setPositionFirst(boolean positionFirst) {
        isPositionFirst = positionFirst;
    }

    public int getReadFromSource() {
        return readFromSource;
    }

    public void setReadFromSource(int readFromSource) {
        this.readFromSource = readFromSource;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    public boolean isPlayVideoOfNewsDetail() {
        return isPlayVideoOfNewsDetail;
    }

    public void setPlayVideoOfNewsDetail(boolean playVideoOfNewsDetail) {
        isPlayVideoOfNewsDetail = playVideoOfNewsDetail;
    }

    public boolean isMarkRead() {
        return isMarkRead;
    }

    public void setMarkRead(boolean markRead) {
        isMarkRead = markRead;
    }

    //    @SerializedName("_body")
//    @Expose
    private List<NewsDetailModel> shortContent = new ArrayList<>();

    @SerializedName("Body")
    @Expose
    private List<NewsDetailModel> body = new ArrayList<>();

    private int contentType = AppConstants.TYPE_NEWS;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    private final static long serialVersionUID = -1901301058722164937L;

    public int getIsNghe() {
        return isNghe;
    }

    public void setIsNghe(int isNghe) {
        this.isNghe = isNghe;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateAlarm() {
        return dateAlarm;
    }

    public void setDateAlarm(String dateAlarm) {
        this.dateAlarm = dateAlarm;
    }

    public String getDatePub() {
        return datePub;
    }

    public void setDatePub(String datePub) {
        this.datePub = datePub;
    }

    public int getID() {
        return iD;
    }

    public void setID(int iD) {
        this.iD = iD;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage169() {
        return image169;
    }

    public void setImage169(String image169) {
        this.image169 = image169;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getReads() {
        return reads;
    }

    public void setReads(int reads) {
        this.reads = reads;
    }

    public String getShapo() {
        return shapo;
    }

    public void setShapo(String shapo) {
        this.shapo = shapo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCateevent() {
        return cateevent;
    }

    public void setCateevent(int cateevent) {
        this.cateevent = cateevent;
    }

    public int getIdStory() {
        return idStory;
    }

    public void setIdStory(int idStory) {
        this.idStory = idStory;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTypeIcon() {
        return typeIcon;
    }

    public void setTypeIcon(int typeIcon) {
        this.typeIcon = typeIcon;
    }

    public List<NewsDetailModel> getBody() {
        if(body == null)
            body = new ArrayList<>();
        return body;
    }

    public void setBody(List<NewsDetailModel> body) {
        this.body = body;
    }

    public List<NewsDetailModel> getShortContent() {
        return shortContent;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceIcon() {
        return sourceIcon;
    }

    public void setSourceIcon(String sourceIcon) {
        this.sourceIcon = sourceIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "NewsModel{" +
                "category='" + category + '\'' +
                ", cid=" + cid +
                ", comment=" + comment +
                ", content='" + content + '\'' +
                ", dateAlarm='" + dateAlarm + '\'' +
                ", datePub='" + datePub + '\'' +
                ", iD=" + iD +
                ", image='" + image + '\'' +
                ", image169='" + image169 + '\'' +
                ", like=" + like +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", pid=" + pid +
                ", reads=" + reads +
                ", shapo='" + shapo + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", cateevent=" + cateevent +
                ", idStory=" + idStory +
                ", isRead=" + isRead +
                ", type=" + type +
                ", typeIcon=" + typeIcon +
                ", timeStamp=" + timeStamp +
                ", shortContent=" + shortContent +
                ", body=" + body +
                '}';
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public NewsModel()
    {

    }

    public NewsModel(String category, int cid, int comment, String content, String dateAlarm, String datePub, int iD, String image, String image169, int like, String mediaUrl, int pid, int reads, String shapo, String title, String sourceName, String poster, String url, String duration, int cateevent, int idStory, int isRead, int isNghe, int type, int typeIcon, long timeStamp, List<NewsDetailModel> body, int contentType) {
        this.category = category;
        this.cid = cid;
        this.comment = comment;
        this.content = content;
        this.dateAlarm = dateAlarm;
        this.datePub = datePub;
        this.iD = iD;
        this.image = image;
        this.image169 = image169;
        this.like = like;
        this.mediaUrl = mediaUrl;
        this.pid = pid;
        this.reads = reads;
        this.shapo = shapo;
        this.title = title;
        this.sourceName = sourceName;
        this.poster = poster;
        this.url = url;
        this.duration = duration;
        this.cateevent = cateevent;
        this.idStory = idStory;
        this.isRead = isRead;
        this.isNghe = isNghe;
        this.type = type;
        this.typeIcon = typeIcon;
        this.timeStamp = timeStamp;
        this.shortContent = shortContent;
        this.body = body;
        this.contentType = contentType;
    }


    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}

