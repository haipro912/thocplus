package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmField;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "block_number", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class BlockConstant extends RealmObject {
    public static final String NUMBER = "number";

    @RealmField(name = "number")
    private String number;

    public BlockConstant(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
