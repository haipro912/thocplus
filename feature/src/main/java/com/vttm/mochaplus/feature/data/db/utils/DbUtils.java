package com.vttm.mochaplus.feature.data.db.utils;

import com.vttm.mochaplus.feature.data.db.model.BlockConstant;
import com.vttm.mochaplus.feature.data.db.model.ReengAccountConstant;
import com.vttm.mochaplus.feature.model.ReengAccount;

import java.util.ArrayList;
import java.util.List;

public class DbUtils {

    public static ReengAccountConstant clone(ReengAccount account)
    {
        ReengAccountConstant object = new ReengAccountConstant();
        object.setIs_active(account.isActive() ? 1 : 0);
        object.setName(account.getName());
        object.setPhone_number(account.getJidNumber());
        object.setToken(account.getToken());
        object.setAvatar_last_change(account.getLastChangeAvatar());
        object.setStatus(account.getStatus());
        object.setGender(account.getGender());
        object.setBirthday(account.getBirthday());
        object.setRegion_code(account.getRegionCode());
        object.setBirthday_string(account.getBirthdayString());
        object.setAvatar_path(account.getAvatarPath());
        object.setNeed_upload(account.getNeedUpload() ? 1 : 0);
        object.setPermission(account.getPermission());
        object.setAvno_number(account.getAvnoNumber());
        object.setIc_front(account.getAvnoICFront());
        object.setIc_back(account.getAvnoICBack());
        return object;
    }

    public static ReengAccount clone(ReengAccountConstant account)
    {
        ReengAccount object = new ReengAccount();
        object.setActive(account.getIs_active() == 1);
        object.setName(account.getName());
        object.setNumberJid(account.getPhone_number());
        object.setToken(account.getToken());
        object.setLastChangeAvatar(account.getAvatar_last_change());
        object.setStatus(account.getStatus());
        object.setGender(account.getGender());
        object.setBirthday(account.getBirthday());
        object.setRegionCode(account.getRegion_code());
        object.setBirthdayString(account.getBirthday_string());
        object.setAvatarPath(account.getAvatar_path());
        object.setNeedUpload(account.getNeed_upload() == 1);
        object.setPermission(account.getPermission());
        object.setAvnoNumber(account.getAvno_number());
        object.setAvnoICFront(account.getIc_front());
        object.setAvnoICBack(account.getIc_back());
        return object;
    }

    public static BlockConstant clone(String number)
    {
        return new BlockConstant(number);
    }

    public static List<BlockConstant> cloneBlock(List<String> numbers)
    {
        ArrayList<BlockConstant> blockConstants = new ArrayList<>();
        for(int i = 0; i < numbers.size(); i++)
        {
            blockConstants.add(new BlockConstant(numbers.get(i)));
        }
        return blockConstants;
    }

    public static ArrayList<String> clone(List<BlockConstant> numbers)
    {
        ArrayList<String> blockConstants = new ArrayList<>();
        for(int i = 0; i < numbers.size(); i++)
        {
            blockConstants.add(numbers.get(i).getNumber());
        }
        return blockConstants;
    }
}
