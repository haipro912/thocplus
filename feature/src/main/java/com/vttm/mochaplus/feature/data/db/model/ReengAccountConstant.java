package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "my_account", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class ReengAccountConstant extends RealmObject{
    public int id;
    public String phone_number;
    public String name;
    public String token;
    public String avatar_last_change;
    public String status;
    public String is_active;
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
}
