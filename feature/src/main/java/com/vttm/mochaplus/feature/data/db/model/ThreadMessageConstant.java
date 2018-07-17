package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "thread", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class ThreadMessageConstant extends RealmObject {
    @PrimaryKey
    public int id;
    public String name;
    public int THREAD_IS_GROUP;
    public String numbers;
    public String thread_chat_room_id;
    public int thread_is_joined;
    public int num_unread_msg;
    public String time_last_change;
    public String draft_msg;
    public int last_time_share_music;
    public String background;
    public int last_time_save_draft;
    public int thread_state;
    public String time_last_show_invite;
    public int last_message_id;
    public String admin_numbers;
    public int group_class;
    public String pin_message;


}
