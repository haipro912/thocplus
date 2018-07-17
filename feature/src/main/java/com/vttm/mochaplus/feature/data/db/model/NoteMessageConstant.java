package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "note_message", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class NoteMessageConstant extends RealmObject {

    @PrimaryKey
    public int id;
    public int thread_type;
    public String thread_jid;
    public String thread_name;
    public String thread_avatar;
    public String content;
    public int stranger_id;
    public int timestamp;
}
