package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "message", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class ReengMessageConstant extends RealmObject {

    @PrimaryKey
    public int id;
    public String content;
    @Index
    public int thread_id;
    public int is_read;
    public String sender;
    public String receiver;
    public String time;
    public int is_send;
    public String file_type;
    public String packet_id;
    public int status;
    public int duration;
    public String file_path;
    public int server_file_id;
    public String type;
    public String file_name;
    public int chat_mode;
    public int file_size;
    public String delivered_members;
    public String MESSAGE_DIRECTION;
    public int MESSAGE_MUSIC_STATE;
    public String IMAGE_URL;
    public String DIRECT_LINK_MEDIA;
    public String ROOM_INFO;
    public String MESSAGE_FILE_ID_NEW;
    public String MESSAGE_REPLY_DETAIL;
    public int MESSAGE_EXPIRED;
    public String TAG_CONTENT;
}
