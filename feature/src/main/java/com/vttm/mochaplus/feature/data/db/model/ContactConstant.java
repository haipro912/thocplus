package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "phone_number", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class ContactConstant extends RealmObject{

    public static final String CONTACT_NAME = "name";

    @PrimaryKey
    public String number_id;
    public String number;
    public String status;
    public String lc_avatar;
    public int state;
    public int is_new;
    public String add_roster;
    public int favorite;
    public int gender;
    public String birthday;
    public String contact_id;
    public String name;
    public String name_unicode;
    public String number_format_e164;
    public String birthday_string;
    public String cover;
    public String albums;
    public int permission;

    public String getNumber_id() {
        return number_id;
    }

    public void setNumber_id(String number_id) {
        this.number_id = number_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLc_avatar() {
        return lc_avatar;
    }

    public void setLc_avatar(String lc_avatar) {
        this.lc_avatar = lc_avatar;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIs_new() {
        return is_new;
    }

    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    public String getAdd_roster() {
        return add_roster;
    }

    public void setAdd_roster(String add_roster) {
        this.add_roster = add_roster;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
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

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_unicode() {
        return name_unicode;
    }

    public void setName_unicode(String name_unicode) {
        this.name_unicode = name_unicode;
    }

    public String getNumber_format_e164() {
        return number_format_e164;
    }

    public void setNumber_format_e164(String number_format_e164) {
        this.number_format_e164 = number_format_e164;
    }

    public String getBirthday_string() {
        return birthday_string;
    }

    public void setBirthday_string(String birthday_string) {
        this.birthday_string = birthday_string;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAlbums() {
        return albums;
    }

    public void setAlbums(String albums) {
        this.albums = albums;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
