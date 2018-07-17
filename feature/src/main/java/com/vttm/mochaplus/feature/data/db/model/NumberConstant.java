package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "phone_number", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class NumberConstant extends RealmObject {

    @PrimaryKey
    public int number_id;
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
}
