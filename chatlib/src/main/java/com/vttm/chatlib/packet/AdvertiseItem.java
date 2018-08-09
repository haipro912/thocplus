package com.vttm.chatlib.packet;

/**
 * Created by thanhnt72 on 8/3/2016.
 */
public class AdvertiseItem {
    private String title = "";
    private String des = "";
    private String iconUrl = "";
    private String action = "";

    public AdvertiseItem(String title, String des, String iconUrl, String action) {
        this.title = title;
        this.des = des;
        this.iconUrl = iconUrl;
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<action title=\'").append(title).append("\'")
                .append(" desc=\'").append(des).append("\'")
                .append(" icon=\'").append(iconUrl).append("\'>")
                .append(action).append("</action>");
        return buf.toString();
    }

    @Override
    public String toString() {
        return "AdvertiseItem{" +
                "title='" + title + '\'' +
                ", des='" + des + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
