package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmField;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "block_number", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class BlockConstant extends RealmObject {
    @RealmField(name = "number")
    private String number;
}
