package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "config_user", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class ConfigUserConstant extends RealmObject {
    @PrimaryKey
    public String id;
    public String key;
    public String value;
}
