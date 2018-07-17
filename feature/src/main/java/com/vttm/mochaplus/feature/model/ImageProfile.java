package com.vttm.mochaplus.feature.model;

import java.io.Serializable;

public class ImageProfile implements Serializable {
    private long id;
    private int typeImage;
    private String imagePathLocal;
    private String imageUrl;
    private boolean isUploaded;
    private long time;
    //    private long idServer;
    private String idServerString;

    private String imageThumb = "";

    //TODO server chua xu ly dc het cac case cho truong hasCover nen tam thoi ko dung den
    private boolean hasCover = true;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(int typeImage) {
        this.typeImage = typeImage;
    }

    public String getImagePathLocal() {
        return imagePathLocal;
    }

    public void setImagePathLocal(String imagePathLocal) {
        this.imagePathLocal = imagePathLocal;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUpload(boolean isUploaded) {
        this.isUploaded = isUploaded;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    /*public long getIdServer() {
        return idServer;
    }

    public void setIdServer(long idServer) {
        this.idServer = idServer;
    }*/

    public String getIdServerString() {
        /*if (TextUtils.isEmpty(idServerString) && idServer != -1) {
            idServerString = String.valueOf(idServer);
        }*/
        return idServerString;
    }

    public void setIdServerString(String idServerString) {
        this.idServerString = idServerString;
    }


    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public boolean hasCover() {
        return hasCover;
    }

    public void setHasCover(boolean hasCover) {
        this.hasCover = hasCover;
    }

    @Override
    public String toString() {
        return "ImageProfile{" +
                "id=" + id +
                ", idServerString=" + idServerString +
                ", typeImage=" + typeImage +
                ", imagePathLocal='" + imagePathLocal + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isUploaded=" + isUploaded +
                ", time=" + time +
                '}';
    }
}
