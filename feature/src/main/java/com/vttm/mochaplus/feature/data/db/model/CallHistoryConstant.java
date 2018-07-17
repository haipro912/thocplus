package com.vttm.mochaplus.feature.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmNamingPolicy;

@RealmClass(name = "call_history", fieldNamingPolicy = RealmNamingPolicy.PASCAL_CASE)
public class CallHistoryConstant extends RealmObject{
    public static final String FRIEND_NAME = "friend_number";
    @PrimaryKey
    private int id;
    private String friend_number;
    private String owner_number;
    private int direction;
    private int state;
    private int call_out;
    private int time;
    private int duration;
    private int count;
    private String column_1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFriend_number() {
        return friend_number;
    }

    public void setFriend_number(String friend_number) {
        this.friend_number = friend_number;
    }

    public String getOwner_number() {
        return owner_number;
    }

    public void setOwner_number(String owner_number) {
        this.owner_number = owner_number;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCall_out() {
        return call_out;
    }

    public void setCall_out(int call_out) {
        this.call_out = call_out;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getColumn_1() {
        return column_1;
    }

    public void setColumn_1(String column_1) {
        this.column_1 = column_1;
    }
}
