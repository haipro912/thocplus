package com.vttm.mochaplus.feature.model;

import android.text.TextUtils;
import android.util.Log;

import com.google.i18n.phonenumbers.Phonenumber;
import com.vttm.mochaplus.feature.helper.PhoneNumberHelper;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.utils.EncryptUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class PhoneNumber implements Serializable {
    private final String TAG = PhoneNumber.class.getSimpleName();
    //contact
    private String contactId = null;
    private String name = null;
    private String nameUnicode = null;
    private int favorite = 0;
    // number
    private String id = null;
    private String jidNumber = null;
    private String status = null;
    private String lastChangeAvatar = null;
    private long lastOnline = 0;
    //last seen
    private long lastSeen = -1;
    private boolean isNewRegister = false;               // nguoi dung moi = true
    private int gender = -1;  //male=1, female=0; default=-1;
    private long birthDay = -1;                          // ngay sinh (mac dinh 1/1/1990)631126800000L
    private int state = AppConstants.CONTACT.NONE;          // =0 ko dungg, =1 dang dung, =2 deactive
    private boolean online = false;
    private boolean isViettel = false;                   // check xem co phai so viettel khong.
    private boolean isChecked = false;
    private boolean isDisable = false;
    private String avatarName;
    private String rawNumber;// so chuan hoa theo quoc gia, bat dau = 0
    private int position = -1;
    private Phonenumber.PhoneNumber mPhoneProtocol;
    private String birthdayString = "";
    private int addRoster;
    private String coverUrl = null;
    private String coverId = "";
    private String albumString = null;
    private int permission = -1;
    private String nickName;// tên trên mocha
    private String avatarVerify;
    private int isCoverDefault = 0;     // =0 la default
    private ArrayList<ImageProfile> mImageProfile;

    private int stateFollow = -2;
    //
    private int sectionType = -1;

    public PhoneNumber() {

    }

    public PhoneNumber(String id, String name, int sectionType) {
        this.contactId = id;
        this.name = name;
        this.sectionType = sectionType;
    }

    public PhoneNumber(String jidNumber, String name) {
        this.jidNumber = jidNumber;
        this.name = name;
        this.state = AppConstants.CONTACT.ACTIVE;
    }

    public ArrayList<ImageProfile> getImageProfile() {
        return mImageProfile;
    }

    public void setImageProfile(ArrayList<ImageProfile> mImageProfile) {
        this.mImageProfile = mImageProfile;
    }

    /**
     * contact
     */
    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        if (name == null) {
            name = rawNumber;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    /**
     * number
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdInt() {
        try {
            if (!TextUtils.isEmpty(id))
                return Integer.valueOf(id);
            return 0;
        } catch (NumberFormatException e) {
            Log.e(TAG, "NumberFormatException", e);
            return 0;
        }
    }

    public String getJidNumber() {
        return jidNumber;
    }

    public void setJidNumber(String jidNumber) {
        isViettel = PhoneNumberHelper.getInstant().isViettelNumber(jidNumber);
        this.jidNumber = jidNumber;
    }

    public void setIsViettel() {
        isViettel = PhoneNumberHelper.getInstant().isViettelNumber(jidNumber);
    }

    public boolean isViettel() {
        return isViettel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastChangeAvatar() {
        return lastChangeAvatar;
    }

    public void setLastChangeAvatar(String lastChangeAvatar) {
        this.lastChangeAvatar = lastChangeAvatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(long birthDay) {
        this.birthDay = birthDay;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public boolean isNewRegister() {
        return isNewRegister;
    }

    public void setNewRegister(boolean isNewRegister) {
        this.isNewRegister = isNewRegister;
    }

    /**
     * other
     */
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean isDisable) {
        this.isDisable = isDisable;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getNameUnicode() {
        return nameUnicode;
    }

    public void setNameUnicode(String nameUnicode) {
        this.nameUnicode = nameUnicode;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getRawNumber() {
        return rawNumber;
    }

    public void setRawNumber(String rawNumber) {
        this.rawNumber = rawNumber;
    }

    public Phonenumber.PhoneNumber getPhoneProtocol() {
        return mPhoneProtocol;
    }

    public void setPhoneProtocol(Phonenumber.PhoneNumber mPhoneProtocol) {
        this.mPhoneProtocol = mPhoneProtocol;
    }

    public String getBirthdayString() {
        return birthdayString;
    }

    public void setBirthdayString(String birthdayString) {
        this.birthdayString = birthdayString;
    }

    public String getAlbumString() {
        return albumString;
    }

    public void setAlbumString(String albumString) {
        this.albumString = albumString;
    }

    public int getStateFollow() {
        return stateFollow;
    }

    public void setStateFollow(int stateFollow) {
        this.stateFollow = stateFollow;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((jidNumber == null) ? 0 : jidNumber.hashCode())
                + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (PhoneNumber.class != obj.getClass()) {
            return false;
        }
        PhoneNumber other = (PhoneNumber) obj;
        String otherJidNumber = other.getJidNumber();
        String otherName = other.getName();
        // so sanh so va name
        if (otherJidNumber != null && otherJidNumber.equals(jidNumber)) {
            return otherName != null && otherName.equals(name);
        } else {
            return false;
        }
    }

    public boolean equalsValues(Object obj) {
        if (obj == null) {
            return false;
        }
        if (PhoneNumber.class != obj.getClass()) {
            return false;
        }
        PhoneNumber other = (PhoneNumber) obj;
        String otherJidNumber = other.getJidNumber();
        String otherContactId = other.getContactId();
        String otherName = other.getName();
        int otherFavorite = other.getFavorite();
        if (otherJidNumber != null && otherJidNumber.equals(jidNumber)) {
            if (otherContactId != null && otherContactId.equals(contactId)) {
                if (otherName != null && otherName.equals(name)) {
                    if (otherFavorite == favorite) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder(TAG);
        temp.append(": Id: ").append(id).append("\n");
        temp.append("Number jid: ").append(jidNumber).append("\n");
        temp.append("Number raw: ").append(rawNumber).append("\n");
        temp.append("state: ").append(state).append("\n");
        temp.append("status: ").append(status).append("\n");
        temp.append("cId: ").append(contactId).append("\n");
        temp.append("Name: ").append(name).append("\n");
        temp.append("Name unicode: ").append(nameUnicode).append("\n");
        temp.append("MochaName: ").append(nickName).append("\n");
        temp.append("LastAva: ").append(lastChangeAvatar).append("\n");
        temp.append("LastOn: ").append(lastOnline).append("\n");
        temp.append("online: ").append(online).append("\n");
        temp.append("Favorite: ").append(favorite).append("\n");
        temp.append("Gender: ").append(gender).append("\n");
        temp.append("LastSeen: ").append(lastSeen).append("\n");
        temp.append("BirthdayString: ").append(birthdayString).append("\n");
        temp.append("Permission: ").append(permission).append("\n");
        temp.append("nickName: ").append(nickName).append("\n");
        return temp.toString();
    }

    public int getAddRoster() {
        return addRoster;
    }

    public void setAddRoster(int roster) {
        addRoster = roster;
    }

    public int getRegister() {
        return isNewRegister ? 1 : 0;
    }

    public void setRegister(int roster) {
        isNewRegister = roster == 1;
    }

    // option
    public boolean isReeng() {
        return state == AppConstants.CONTACT.ACTIVE;
    }

    public void setOnline(boolean isOnline) {
        this.online = isOnline;
    }

    public boolean isOnline() {
        if (state == AppConstants.CONTACT.ACTIVE) {
            return online;
        }
        return false;
    }

    public void setAvatarName(String cname) {
        avatarName = PhoneNumberHelper.getInstant().getAvatarNameFromName(cname);
    }

    public String getAvatarName() {
        if (TextUtils.isEmpty(avatarName))
            avatarName = PhoneNumberHelper.getInstant().getAvatarNameFromName(name);
        return avatarName;
    }

    // convert json
    public JSONObject getJsonObject(boolean setName) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(AppConstants.HTTP.CONTACT.MSISDN, jidNumber);
            obj.put(AppConstants.HTTP.CONTACT.NAME, getName());
        } catch (JSONException e) {
            Log.e(TAG, "Exception", e);
        }
        return obj;
    }

    public void setJsonObject(JSONObject object, long durationTimeSV) {
        // number
        jidNumber = object.optString(AppConstants.HTTP.CONTACT.MSISDN);
        // state
        state = object.optInt(AppConstants.HTTP.CONTACT.STATE);
        //status
        status = object.optString(AppConstants.HTTP.CONTACT.STATUS);
        // gioi tinh
        gender = object.optInt(AppConstants.HTTP.CONTACT.GENDER, -1);
        // ngay sinh ko dung long nua
        //    birthDay = object.getLong(AppConstants.HTTP.CONTACT.BIRTHDAY);
        birthdayString = object.optString(AppConstants.HTTP.CONTACT.BIRTHDAY_STRING);
        //avatar
        String lAvatar = object.optString(AppConstants.HTTP.CONTACT.LAST_AVATAR, null);
        if (lAvatar != null && "0".equals(lAvatar)) {
            lastChangeAvatar = null;
        } else {
            lastChangeAvatar = lAvatar;
        }
        //cover
        coverUrl = object.optString(AppConstants.HTTP.CONTACT.COVER_IMAGE);
        coverId = object.optString(AppConstants.HTTP.CONTACT.COVER_ID);
        albumString = object.optString(AppConstants.HTTP.CONTACT.ALBUMS);
        permission = object.optInt(AppConstants.HTTP.CONTACT.PERMISSION, -1);
        nickName = object.optString(AppConstants.HTTP.USER_INFOR.NAME);
        stateFollow = object.optInt("follow", -2);
        isCoverDefault = object.optInt(AppConstants.HTTP.CONTACT.IS_COVER_DEFAULT);
    }

    public void setJsonObjectSearchUser(JSONObject object, long durationTimeSV) {
        // number
        jidNumber = object.optString(AppConstants.HTTP.STRANGER_MUSIC.MSISDN);
        //name
        name = object.optString(AppConstants.HTTP.STRANGER_MUSIC.NAME);
        //status
        status = object.optString(AppConstants.HTTP.STRANGER_MUSIC.STATUS);
        // gioi tinh
        gender = object.optInt(AppConstants.HTTP.STRANGER_MUSIC.GENDER, -1);
        birthdayString = object.optString(AppConstants.HTTP.STRANGER_MUSIC.BIRTHDAY);
        //avatar
        String lAvatar = object.optString(AppConstants.HTTP.STRANGER_MUSIC.LAST_AVATAR, null);
        if (lAvatar != null && "0".equals(lAvatar)) {
            lastChangeAvatar = null;
        } else {
            lastChangeAvatar = lAvatar;
        }
        state = AppConstants.CONTACT.ACTIVE;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    // convert json for onmedia
    public JSONObject getJsonObjectForOnMedia() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", getName());
            obj.put("phone", jidNumber);
        } catch (JSONException e) {
            Log.e(TAG, "Exception", e);
        }
        return obj;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    public ImageProfile getImageCover() {
        if (TextUtils.isEmpty(coverUrl))
            return null;
        ImageProfile imageProfile = new ImageProfile();
        imageProfile.setTypeImage(AppConstants.IMAGE_TYPE.IMAGE_COVER);
        imageProfile.setImageUrl(coverUrl);
        imageProfile.setIdServerString(coverId);
        imageProfile.setHasCover(isCoverDefault != 0);
        return imageProfile;
    }

    public ArrayList<ImageProfile> getAlbums() {
        if (TextUtils.isEmpty(albumString))
            return new ArrayList<>();

        ArrayList<ImageProfile> albums = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(albumString);
            int length = jsonArray.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    if (jsonObj != null) {
                        ImageProfile image = new ImageProfile();
                        image.setIdServerString(jsonObj.getString("imageSId"));
                        image.setImageUrl(jsonObj.getString("imageUrl"));
                        if (jsonObj.has("thumbUrl")) {
                            image.setImageThumb(jsonObj.getString("thumbUrl"));
                        }
                        image.setTypeImage(AppConstants.IMAGE_TYPE.IMAGE_NORMAL);
                        albums.add(image);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getAlbums", e);
        }
        return albums;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public boolean isShowBirthday() {

        // ngay sinh trong
        if (TextUtils.isEmpty(birthdayString)) {
            return false;
        } else if (permission == -1) {
            return true;
        } else {
            // and bit de lay bit =1
            int permissionBirth = permission & 1;
            return permissionBirth == 1;
        }
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarVerify() {
        if (TextUtils.isEmpty(avatarVerify)) {
            avatarVerify = EncryptUtil.getVerify(jidNumber);
        }
        return avatarVerify;
    }

    public int getSectionType() {
        return sectionType;
    }

    public void setSectionType(int sectionType) {
        this.sectionType = sectionType;
    }
}