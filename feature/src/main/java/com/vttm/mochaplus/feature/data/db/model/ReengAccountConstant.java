package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "my_account", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class ReengAccountConstant extends RealmObject{
    public static final String ID = "id";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String NAME = "name";
    public static final String TOKEN = "token";
    public static final String AVATAR_LAST_CHANGE = "avatar_last_change";
    public static final String STATUS = "status";
    public static final String IS_ACTIVE = "is_active"; //acc nay token con active ko
    public static final String GENDER = "gender";
    public static final String BIRTHDAY = "birthday";
    public static final String REGION_CODE = "region_code";
    public static final String BIRTHDAY_STRING = "birthday_string";
    public static final String AVATAR_PATH = "avatar_path";
    public static final String NEED_UPLOAD = "need_upload";
    public static final String PERMISSION = "permission";
    public static final String AVNO_NUMBER = "avno_number";
    public static final String AVNO_IDENTIFY_CARD_FRONT = "ic_front";
    public static final String AVNO_IDENTIFY_CARD_BACK = "ic_back";

    @PrimaryKey
    public int id;
    public String phone_number;
    public String name;
    public String token;
    public String avatar_last_change;
    public String status;
    public int is_active;
    public int gender;
    public String birthday;
    public String region_code;
    public String birthday_string;
    public String avatar_path;
    public int need_upload;
    public int permission;
    public String avno_number;
    public String ic_front;
    public String ic_back;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar_last_change() {
        return avatar_last_change;
    }

    public void setAvatar_last_change(String avatar_last_change) {
        this.avatar_last_change = avatar_last_change;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getBirthday_string() {
        return birthday_string;
    }

    public void setBirthday_string(String birthday_string) {
        this.birthday_string = birthday_string;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    public int getNeed_upload() {
        return need_upload;
    }

    public void setNeed_upload(int need_upload) {
        this.need_upload = need_upload;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getAvno_number() {
        return avno_number;
    }

    public void setAvno_number(String avno_number) {
        this.avno_number = avno_number;
    }

    public String getIc_front() {
        return ic_front;
    }

    public void setIc_front(String ic_front) {
        this.ic_front = ic_front;
    }

    public String getIc_back() {
        return ic_back;
    }

    public void setIc_back(String ic_back) {
        this.ic_back = ic_back;
    }
}
