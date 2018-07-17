package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "offical", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class OfficerAccountConstant extends RealmObject{

    public int id;
    public String offical_id;
    public String name;
    public String avatar_url;
    public int state;
    public int officer_type;
    public int room_state;
}
