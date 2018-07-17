package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "non_contact_table", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class NonContactConstant extends RealmObject {
    @PrimaryKey
    public int non_contact_id;
    public String non_contact_number;
    public String non_contact_state;
    public String non_contact_status;
    public String non_contact_last_avatar;
    public String non_contact_gender;
    public String non_contact_birthday;
    public String non_contact_unknown_column_1;
    public String non_contact_unknown_column_2;
    public String non_contact_birthday_string;
    public String non_contact_cover;
    public String non_contact_albums;
    public String non_contact_permission;
}
