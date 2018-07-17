package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "contact", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class ContactConstant extends RealmObject{

    public static final String CONTACT_NAME = "name";

    private String contact_id;
    private String name;
    private int favorite;
    private String name_unicode;

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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getName_unicode() {
        return name_unicode;
    }

    public void setName_unicode(String name_unicode) {
        this.name_unicode = name_unicode;
    }
}
