package org.jivesoftware.smack.packet;

import android.text.TextUtils;

public class Member {
    private String jid;
    private Role role;
    private int code = -1;
    private String invitedFrom;
    private String nickName;
    private String avatar;

    public Member() {

    }

    public Member(String jid, String roleString, int code, String nickName, String avatar) {
        this.jid = jid;
        this.role = Role.fromString(roleString);
        this.code = code;
        this.nickName = nickName;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInvitedFrom() {
        return invitedFrom;
    }

    public void setInvitedFrom(String invitedFrom) {
        this.invitedFrom = invitedFrom;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String toXml() {
        StringBuilder buf = new StringBuilder();
        buf.append("<member jid=\'").append(jid).append("\'");
        if (role != null) {
            buf.append(" role=\'").append(role).append("\'");
        }
        if (code != -1) {
            buf.append(" code=\'").append(code).append("\'");
        }
        if (!TextUtils.isEmpty(nickName)) {
            buf.append(" name=\'").append(nickName).append("\'");
        }
        if(!TextUtils.isEmpty(avatar)){
            buf.append(" avatar=\'").append(avatar).append("\'");
        }
        buf.append("/>");
        return buf.toString();
    }

    public String toString() {
        String result = "<member jid=\"" + jid + "\"";
        if (role != null) {
            result += " role=\"" + role + "\"";
        }
        if (code > 0) {
            result += " code=\"" + code + "\"";
        }
        if (invitedFrom != null && invitedFrom.length() > 0) {
            result += " invitedFrom=\"" + invitedFrom + "\"";
        }
        if (!TextUtils.isEmpty(nickName)) {
            result += " name=\"" + nickName + "\"";
        }
        if (!TextUtils.isEmpty(avatar)) {
            result += " avatar=\"" + avatar + "\"";
        }
        result += "/>";
        return result;
    }

    public enum Role {
        member, owner, none;

        public static Role fromString(String name) {
            try {
                return Role.valueOf(name);
            } catch (Exception e) {
                return member;
            }
        }
    }

    public boolean isOwner() {
        return role != null && role == Role.owner;
    }

    public boolean isMember() {
        return role != null && role == Role.member;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((jid == null) ? 0 : jid.hashCode())
                + ((role == null) ? 0 : role.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (Member.class != obj.getClass()) {
            return false;
        }
        Member other = (Member) obj;
        String otherJid = other.getJid();
        Role otherRole = other.getRole();
        // so sanh so va role
        if (otherJid != null && otherJid.equals(jid)) {
            return otherRole != null && otherRole == role;
        } else {
            return false;
        }
    }
}