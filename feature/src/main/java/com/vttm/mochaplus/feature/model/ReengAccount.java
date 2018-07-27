package com.vttm.mochaplus.feature.model;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.vttm.mochaplus.feature.helper.PhoneNumberHelper;
import com.vttm.mochaplus.feature.helper.TimeHelper;
import com.vttm.mochaplus.feature.helper.Version;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.utils.EncryptUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ThaoDV on 6/6/14.
 */
public class ReengAccount implements Serializable {
    public static final String TAG = ReengAccount.class.getSimpleName();
    private long id;
    private boolean isActive;
    // so de xac thuc len sv // so nuoc ngoai thi la mã nuoc + so, so vietnam là 0 + so(luu tren db)ko tinh dau '+' o
    // dau
    private String numberJid;
    private String name = "";
    private String token = "";
    private String lastChangeAvatar;
    private String status;
    private int gender = AppConstants.CONTACT.GENDER_MALE;
    private String birthday;
    private String regionCode;
    private String facebookId;      //chi luu tren mem
    private String birthdayString = "";
    private String avatarPath;
    private String coverUrl;
    private boolean needUpload = false;
    private ArrayList<ImageProfile> albums;
    private int permission = -1;
    private String avatarVerify;
    private String avnoNumber;
    private String avnoICFront, avnoICBack;

    public String getStatus() {
        if (status != null && status.length() > AppConstants.CONTACT.STATUS_LIMIT)
            status = status.substring(0, AppConstants.CONTACT.STATUS_LIMIT - 1);
        return status;
    }

    public void setStatus(String status) {
        if (status != null && status.length() > AppConstants.CONTACT.STATUS_LIMIT)
            status = status.substring(0, AppConstants.CONTACT.STATUS_LIMIT - 1);
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public int getIdInt() {
        return (int) (long) id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getJidNumber() {
        return numberJid;
    }

    public void setNumberJid(String numberJid) {
        this.numberJid = numberJid;
        //isViettel = PhoneNumberHelper.getInstant().isViettelByCountry(this, numberJid);
    }

    public String getName() {
        if (name == null) {
            //return numberJid;
            name = "";
        }
        return name;
    }

    public void setName(String name) {
        if (name != null) {//??????????? cha hieu cho nay ntn
            if (Version.hasN()) {
                name = Html.fromHtml(name, Html.FROM_HTML_MODE_LEGACY).toString();
            } else {
                name = Html.fromHtml(name).toString();
            }
        }
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLastChangeAvatar() {
        return lastChangeAvatar;
    }

    public void setLastChangeAvatar(String avatarUrl) {
        this.lastChangeAvatar = avatarUrl;
    }

    public boolean isViettelNumber() {
        return PhoneNumberHelper.getInstant().isViettelUser(this);
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public long getBirthdayLong() {
        long birthDay = TimeHelper.BIRTHDAY_DEFAULT;
        try {
            birthDay = Long.parseLong(birthday);
        } catch (NumberFormatException e) {
            Log.e(TAG, "NumberFormatException", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException", e);
        } catch (Exception ex) {
            Log.e(TAG, "Exception", ex);
        }
        return birthDay;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegionCode() {
        if (TextUtils.isEmpty(regionCode)) {
            setRegionCode("VN");// mac dinh la vn
        }
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        if (TextUtils.isEmpty(regionCode)) {
            regionCode = "VN";// mac dinh la vn
        }
        this.regionCode = regionCode;
        PhoneNumberHelper.getInstant().setRegionCode(regionCode);
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getBirthdayString() {
        return birthdayString;
    }

    public void setBirthdayString(String birthdayString) {
        this.birthdayString = birthdayString;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public boolean getNeedUpload() {
        return needUpload;
    }

    public void setNeedUpload(boolean needUpload) {
        this.needUpload = needUpload;
    }

    @Override
    public String toString() {
        return "ReengAccount{" +
                "id=" + id +
                ", phoneNumber='" + numberJid + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", lastChangeAvatar='" + lastChangeAvatar + '\'' +
                ", permission='" + permission + '\'' +
                '}';
    }

    // convert json
    public JSONObject getJsonObject(String email) {
        JSONObject obj = new JSONObject();
        try {
            if (name != null) {
                obj.put(AppConstants.HTTP.USER_INFOR.NAME, name);
            }
            obj.put(AppConstants.HTTP.USER_INFOR.GENDER, gender);
            obj.put(AppConstants.HTTP.USER_INFOR.BIRTHDAY, birthday);
            obj.put(AppConstants.HTTP.USER_INFOR.BIRTHDAY_STRING, birthdayString);
            if (!TextUtils.isEmpty(facebookId)) {
                obj.put(AppConstants.HTTP.USER_FACEBOOK_ID, facebookId);
            }
            if (!TextUtils.isEmpty(email)) {
                obj.put(AppConstants.HTTP.USER_INFOR.EMAILS, getEmailJsonArray(email));
            }
        } catch (JSONException e) {
            Log.e(TAG, "getJsonObject", e);
        }
        return obj;
    }

    public JSONArray getEmailJsonArray(String email) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(email);
        return jsonArray;
    }

//    public void setJsonObject(JSONObject object) {
//        try {
//            // name
//            if (object.has(AppConstants.HTTP.USER_INFOR.NAME)) {
//                setName(object.getString(AppConstants.HTTP.USER_INFOR.NAME));
//            }
//            // gender
//            if (object.has(AppConstants.HTTP.USER_INFOR.GENDER))
//                gender = object.getInt(AppConstants.HTTP.USER_INFOR.GENDER);
//            //birthday
//            if (object.has(AppConstants.HTTP.USER_INFOR.BIRTHDAY))
//                birthday = object.getString(AppConstants.HTTP.USER_INFOR.BIRTHDAY);
//            if (object.has(AppConstants.HTTP.USER_INFOR.STATUS))
//                status = object.getString(AppConstants.HTTP.USER_INFOR.STATUS);
//            if (object.has(AppConstants.HTTP.USER_INFOR.LAST_AVATAR)) {
//                String lAvatar = object.getString(AppConstants.HTTP.USER_INFOR.LAST_AVATAR);
//                if (lAvatar != null && "0".equals(lAvatar))
//                    lastChangeAvatar = null;
//                else
//                    lastChangeAvatar = lAvatar;
//            }
//            if (object.has((AppConstants.HTTP.USER_INFOR.BIRTHDAY_STRING))) {
//                birthdayString = object.getString(AppConstants.HTTP.USER_INFOR.BIRTHDAY_STRING);
//            }
//            if (object.has(AppConstants.HTTP.USER_INFOR.COVER_IMAGE)) {
//                coverUrl = object.getString(AppConstants.HTTP.USER_INFOR.COVER_IMAGE);
//            }
//            if (object.has(AppConstants.HTTP.USER_INFOR.ALBUMS)) {
//                albums = new ArrayList<>();
//                JSONArray jsonArray = object.getJSONArray(AppConstants.HTTP.USER_INFOR.ALBUMS);
//                if (jsonArray != null) {
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObj = jsonArray.getJSONObject(i);
//                        if (jsonObj != null) {
//                            ImageProfile image = new ImageProfile();
//                            image.setIdServerString(jsonObj.getString("imageSId"));
//                            image.setImageUrl(jsonObj.getString("imageUrl"));
//                            image.setTypeImage(ImageProfileConstant.IMAGE_NORMAL);
//                            image.setUpload(true);
//                            image.setTime((new Date()).getTime());
//                            albums.add(image);
//                        }
//                    }
//                }
//            }
//            int permission = 1;
//            if (object.has(AppConstants.HTTP.USER_INFOR.PERMISSION)) {
//                permission = object.getInt(AppConstants.HTTP.USER_INFOR.PERMISSION);
//            }
//            if (object.has(AppConstants.HTTP.USER_INFOR.HIDE_STRANGLE_HISTORY)) {
//                int strangerPermission = object.getInt(AppConstants.HTTP.USER_INFOR.HIDE_STRANGLE_HISTORY);
//                if (strangerPermission == 1) {// hide
//                    permission = permission | ContactConstant.PERMISSION_HIDE_STRANGER_HISTORY;
//                }
//            }
//            avnoNumber = object.optString("virtualNumber", null);
//            avnoICFront = object.optString("ic_1", null);
//            avnoICBack = object.optString("ic_2", null);
//            this.permission = permission;
//
////            int birthday_reminder = 1;
////            if (object.has(AppConstants.HTTP.USER_INFOR.BIRTHDAY_REMINDER)) {
////                try {
////                    birthday_reminder = object.getInt(AppConstants.HTTP.USER_INFOR.BIRTHDAY_REMINDER);
////                } catch (Exception e) {
////                    birthday_reminder = 1;
////                }
////            }
////            this.birthday_reminder = birthday_reminder;
//
//        } catch (JSONException e) {
//            Log.e(TAG, "getJsonObject", e);
//        }
//    }

    public ReengAccount() {
    }

    public ReengAccount(Long id) {
        this.id = id;
    }

//    public void setCoverPath(String coverPath) {
//        this.coverPath = coverPath;
//    }
//
//    public String getCoverPath() {
//        return coverPath;
//    }

    public ArrayList<ImageProfile> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<ImageProfile> albums) {
        this.albums = albums;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getAvatarVerify() {
        if (TextUtils.isEmpty(avatarVerify)) {
            avatarVerify = EncryptUtil.getVerify(numberJid);
        }
        return avatarVerify;
    }

    public String getAvnoNumber() {
        return avnoNumber;
    }

    public void setAvnoNumber(String avnoNumber) {
        this.avnoNumber = avnoNumber;
    }

    public void setAvnoICFront(String avnoICFront) {
        this.avnoICFront = avnoICFront;
    }

    public void setAvnoICBack(String avnoICBack) {
        this.avnoICBack = avnoICBack;
    }

    public String getAvnoICFront() {
        return avnoICFront;
    }

    public String getAvnoICBack() {
        return avnoICBack;
    }
}