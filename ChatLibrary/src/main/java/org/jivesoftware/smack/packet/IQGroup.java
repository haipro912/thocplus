package org.jivesoftware.smack.packet;

import com.viettel.util.Log;

import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by toanvk2 on 11/5/14.
 */
public class IQGroup extends IQ {
    private GroupType groupType;
    private String nameSpace;
    private String groupName;
    private String groupJid;
    private ArrayList<String> members;
    private ArrayList<Member> memberObjects = new ArrayList<Member>();
    private int groupPrivate = -1;
    private String groupAvatar;
    private int groupClass = 0;

    public IQGroup() {

    }

    public IQGroup(GroupType groupType, Type type, String to) {
        nameSpace = groupType.toString();
        setType(type);
        setTo(to + "@muc.reeng");
    }

    @Override
    public String getChildElementXML() {
        return null;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupJid() {
        return groupJid;
    }

    public void setGroupJid(String groupJid) {
        this.groupJid = groupJid;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public ArrayList<Member> getMemberObjects() {
        return memberObjects;
    }

    public void setMemberObjects(ArrayList<Member> memberObjects) {
        this.memberObjects = memberObjects;
    }

    public void addMemberObject(String jid, String role, int code, String nickName, String avatar) {
        Member member = new Member(jid, role, code, nickName, avatar);
        if (memberObjects == null) {
            memberObjects = new ArrayList<Member>();
        }
        memberObjects.add(member);
    }

    /*public ArrayList<String> getSuccessMembers() {
        ArrayList<String> members = new ArrayList<String>();
        for (Member member : memberObjects) {
            if (member.getCode() == 200) {
                members.add(member.getJid());
            }
        }
        if (members.size() > 0) {
            return members;
        } else {
            return null;
        }
    }*/

    public ArrayList<Member> getMembers() {
        ArrayList<Member> members = new ArrayList<Member>();
        for (Member member : memberObjects) {
            if (member.getCode() == 200) {
                members.add(member);
            }
        }
        return members;
    }

    public int getGroupPrivate() {
        return groupPrivate;
    }

    public void setGroupPrivate(int groupPrivate) {
        this.groupPrivate = groupPrivate;
    }

    public String getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    public int getGroupClass() {
        return groupClass;
    }

    public void setGroupClass(int groupClass) {
        this.groupClass = groupClass;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<iq");
        if (getPacketID() != null) {
            buf.append(" id=\"").append(getPacketID()).append("\"");
        }
        if (getTo() != null) {
            buf.append(" to=\"").append(StringUtils.escapeForXML(getTo()))
                    .append("\"");
        }
        if (getFrom() != null) {
            buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom()))
                    .append("\"");
        }
        if (getType() != null) {
            buf.append(" type=\"").append(getType()).append("\"");
        }
        buf.append(">");
        // add event namespace
        if (nameSpace != null)
            buf.append("<query xmlns=\"").append(nameSpace).append("\">");
        // add name of group
        if (groupName != null) {
            buf.append("<room").append(" name=\"").append(StringUtils.escapeForXML(groupName)).append("\"");
            if (groupJid != null && groupJid.length() > 0) {
                buf.append(" jid=\"").append(groupJid).append("\"");
            }
            buf.append("/>");
        }
        // add member
        if (memberObjects != null && memberObjects.size() > 0) {
            // add members object
            for (Member m : memberObjects) {
                buf.append(m.toXml());
            }
        } else if (members != null && members.size() > 0) {
            // add members
            for (int i = 0; i < members.size(); i++) {
                buf.append("<member jid=\'").append(members.get(i))
                        .append("\' role=\"member\"/>");
            }
        }
        // private
        if (groupPrivate != -1) {
            buf.append("<keep_private value=\'").append(groupPrivate).append("\'/>");
        }
        buf.append("</query>");
        buf.append("</iq>");
        return buf.toString();
    }

    /**
     * Represents the group type of IQ group
     */
    public enum GroupType {
        /**
         * (Default) a normal
         */
        normal,
        create,
        invite,
        rename,
        leave,
        config,
        kick,
        makeAdmin,
        groupPrivate;

        public static GroupType fromString(String name) {
            try {
                return GroupType.valueOf(name);
            } catch (Exception e) {
                Log.e("IQGroup", "Exception", e);
                return normal;
            }
        }
    }
}
