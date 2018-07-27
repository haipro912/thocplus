package com.vttm.mochaplus.feature.helper;

import android.text.TextUtils;
import android.util.Log;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.vttm.mochaplus.feature.model.ReengAccount;
import com.vttm.mochaplus.feature.utils.Config;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class PhoneNumberHelper {
    private final String TAG = PhoneNumberHelper.class.getSimpleName();
    private static PhoneNumberHelper mInstant;
    private Pattern mViettelPattern;
    //private Pattern mPhonePattern;
    private Pattern mNumberPattern;
    private Pattern mMorePhone;
    private Pattern mPatternLixi;
    private ArrayList<String> smsOutPrefixes;
    private String regionCode;

    public static synchronized PhoneNumberHelper getInstant() {
        if (mInstant == null) {
            mInstant = new PhoneNumberHelper();
        }
        return mInstant;
    }

    private PhoneNumberHelper() {
        initPattern();
        smsOutPrefixes = new ArrayList<>();
    }

    private void initPattern() {
        mViettelPattern = Pattern.compile(Config.Pattern.VIETTEL);
        //mPhonePattern = Pattern.compile(Config.Pattern.PHONE);
        mNumberPattern = Pattern.compile("[^0-9]");
        mMorePhone = Pattern.compile(Config.Pattern.MORE_PHONE);
        mPatternLixi = Pattern.compile("\\d+");
    }

    public Pattern getPatternLixi() {
        if (mPatternLixi == null) {
            initPattern();
        }
        return mPatternLixi;
    }

    /**
     * @param number
     * @return boolean
     */
    public boolean isValidNumberNotRemoveChar(String number) {
        if (number == null || number.length() <= 0) {
            return false;
        }
        return mMorePhone.matcher(number).find();
    }

    /**
     * check so viettel
     * th so vietnam chua lay duoc config thi check nhu ham cu
     *
     * @param jidNumber
     * @return
     */
    public boolean isViettelNumber(String jidNumber) {
        synchronized (TAG) {
            if (TextUtils.isEmpty(jidNumber)) {
                return false;
            } else if (smsOutPrefixes.isEmpty()) {
                if ("VN".equals(regionCode)) {
                    return mViettelPattern.matcher(jidNumber).find();
                }
            } else {
                for (String prefix : smsOutPrefixes) {
                    if (jidNumber.startsWith(prefix)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean isViettelNumberNew(String jidNumber) {
        synchronized (TAG) {
            if (TextUtils.isEmpty(jidNumber)) {
                return false;
            } else if ("VN".equals(regionCode)) {
                for (String prefix : smsOutPrefixes) {
                    if (jidNumber.startsWith(prefix)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean isViettelUser(ReengAccount account) {
        String jidNumber = account.getJidNumber();
        return isViettelNumber(jidNumber);
    }


    public String getAvatarNameFromName(String cname) {
        if (TextUtils.isEmpty(cname)) {
            return "#";
        }
        //Log.d(TAG, "cname: " + cname);
        String avatarName = "";
        String[] splitNames = cname.split("\\s+");
        int size = splitNames.length;
        if (size > 0) {
            if (TextUtils.isEmpty(splitNames[0])) {
                avatarName = "#";
            } else {
                avatarName = avatarName + splitNames[0].charAt(0);
                if (size > 1 && !TextUtils.isEmpty(splitNames[size - 1])) {
                    avatarName += splitNames[size - 1].charAt(0);
                }
            }
        } else {
            avatarName = "#";
        }
        return avatarName.toUpperCase();
    }

//    /**
//     * get phone number by sim
//     */
//    public Phonenumber.PhoneNumber detectPhoneNumberFromDevice(ApplicationController application) {
//        try {
//            if (PermissionHelper.allowedPermission(application, Manifest.permission.READ_PHONE_STATE)) {
//                TelephonyManager mTelephonyManager = null;
//                int res = application.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE");
//                if (res == PackageManager.PERMISSION_GRANTED) {
//                    mTelephonyManager = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
//                }
//                if (mTelephonyManager != null) {
//                    String number = mTelephonyManager.getLine1Number();
//                    PhoneNumberUtil numberUtil = application.getPhoneUtil();
//                    Phonenumber.PhoneNumber phoneNumber = getPhoneNumberProtocol(numberUtil, number, "VN");
//                    if (isValidPhoneNumber(numberUtil, phoneNumber)) {
//                        return phoneNumber;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Exception", e);
//        }
//        return null;
//    }
//
//    public String detectRegionCodeFromDevice(ApplicationController application) {
//        try {
//            if (PermissionHelper.allowedPermission(application, Manifest.permission.READ_PHONE_STATE)) {
//                String regionCode = null;
//                TelephonyManager mTelephonyManager = null;
//                int res = application.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE");
//                if (res == PackageManager.PERMISSION_GRANTED) {
//                    mTelephonyManager = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
//                }
//                if (mTelephonyManager != null) {
//                    regionCode = mTelephonyManager.getNetworkCountryIso();
//                }
//                if (regionCode == null) {
//                    regionCode = application.getResources().getConfiguration().locale.getCountry();
//                }
//                if (regionCode != null) {
//                    Log.d(TAG, "detectRegionCodeFromDevice: " + regionCode);
//                    return regionCode.toUpperCase();
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Exception", e);
//        }
//        /*return null;*/
//        return "VN";
//    }

    public String encodeStrangerNumber(String input) {
        int length = input.length();
        StringBuilder sb = new StringBuilder();
        sb.append(input.substring(0, 3));// 4 so dau
        for (int i = 3; i < length; i++) {
            sb.append("*");
        }
        return sb.toString();
    }

    // so quoc te
    public boolean isValidPhoneNumber(PhoneNumberUtil phoneUtil, Phonenumber.PhoneNumber numberProtocol) {
        if (numberProtocol == null) {
            return false;
        } else {
            return phoneUtil.isValidNumber(numberProtocol);
        }
    }

    public Phonenumber.PhoneNumber getPhoneNumberProtocol(PhoneNumberUtil phoneUtil,
                                                          String number, String regionCode) {
        if (TextUtils.isEmpty(number)) {
            return null;
        }
        try {
            return phoneUtil.parse(number, regionCode);
        } catch (NumberParseException e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    /*public String getNumberJidFromNumberE164(String numberE164) {
        if (!TextUtils.isEmpty(numberE164)) {
            if (numberE164.startsWith("+84")) {// so vietnam thi bo +84 thay bang 0
                numberE164 = "0" + numberE164.substring(3, numberE164.length());
            } else if (numberE164.startsWith("+")) {
                numberE164 = numberE164.substring(1, numberE164.length());
            }
        }
        return numberE164;
    }*/

    public String getNumberJidFromNumberE164(String numberE164) {
        // so vietnam thi bo +84 thay bang 0
        if (!TextUtils.isEmpty(numberE164) && numberE164.startsWith("+84")) {
            numberE164 = "0" + numberE164.substring(3, numberE164.length());
        }
        return numberE164;
    }

    /**
     * lay raw number khi init contact
     *
     * @param numberProtocol
     * @return
     */
    public String getRawNumber(PhoneNumberUtil phoneUtil,
                               Phonenumber.PhoneNumber numberProtocol, String regionCode) {
        String numberNational;
        if (phoneUtil.isValidNumberForRegion(numberProtocol, regionCode)) {
            numberNational = mNumberPattern.matcher(phoneUtil.
                    format(numberProtocol, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)).replaceAll("");
        } else {
            numberNational = phoneUtil.format(numberProtocol, PhoneNumberUtil.PhoneNumberFormat.E164);
        }
        return numberNational;
    }

//    /**
//     * lay raw number khi so da duoc chuan hoa
//     *
//     * @param application
//     * @param jidNumber
//     * @return
//     */
//    public String getRawNumber(ApplicationController application, String jidNumber) {
//        String rawNumber;
//        PhoneNumberUtil mPhoneUtil = application.getPhoneUtil();
//        String regionCode = application.getReengAccountBusiness().getRegionCode();
//        Phonenumber.PhoneNumber phoneProtocol;
//        if (!"VN".equals(regionCode)) {// so dang nhap ko phai vn
//            if (jidNumber.startsWith("0")) {// jid bat dau =0 la jid vn
//                String jidVn = "+84" + jidNumber.substring(1, jidNumber.length());
//                phoneProtocol = PhoneNumberHelper.
//                        getInstant().getPhoneNumberProtocol(mPhoneUtil, jidVn, regionCode);
//            } else {// so khac viet nam
//                phoneProtocol = PhoneNumberHelper.
//                        getInstant().getPhoneNumberProtocol(mPhoneUtil, jidNumber, regionCode);
//            }
//        } else {
//            if (jidNumber.startsWith("0")) {// so dang nhap vm, jid vn thi tra lai luon
//                phoneProtocol = null;
//            } else {        // so dang nhao vn, jid quoc te
//                phoneProtocol = PhoneNumberHelper.
//                        getInstant().getPhoneNumberProtocol(mPhoneUtil, jidNumber, regionCode);
//            }
//        }
//        if (phoneProtocol != null) {
//            // lay raw number
//            rawNumber = PhoneNumberHelper.getInstant().getRawNumber(mPhoneUtil, phoneProtocol, regionCode);
//        } else {
//            rawNumber = jidNumber;
//        }
//        return rawNumber;
//    }

    public String getNumberNational(PhoneNumberUtil phoneUtil, String numberJid, String regionCode) {
        if (TextUtils.isEmpty(numberJid)) {
            return "";
        }
        Phonenumber.PhoneNumber numberProtocol = getPhoneNumberProtocol(phoneUtil, numberJid, regionCode);
        if (numberProtocol != null && phoneUtil.isValidNumber(numberProtocol)) {
            String national = phoneUtil.format(numberProtocol, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            return mNumberPattern.matcher(national).replaceAll("");
        }
        return "";
    }

    public String formatNumberBeforeSaveContact(PhoneNumberUtil phoneUtil, String jidNumber, String regionCode) {
        if (TextUtils.isEmpty(jidNumber)) {
            return null;
        }
        if ("VN".equals(regionCode)) {
            return jidNumber;
        } else if (!TextUtils.isEmpty(jidNumber)) {
            if (jidNumber.startsWith("0")) {// jid bat dau =0 la jid vn
                String jidVN = "+84" + jidNumber.substring(1, jidNumber.length());
                return jidVN;
            } else {// jid so nuoc ngoai thi de nguyen//sau neu sua ve chuan quoc gia thi sua o day
                return jidNumber;
            }
        } else {
            return jidNumber;
        }
    }

    public String getStandardizedNumber(String input) {
        if (TextUtils.isEmpty(input)) {
            return null;
        } else {
            String output = mNumberPattern.matcher(input).replaceAll("");
            return output;
        }
    }

    public void setSmsOutPrefixes(String stringPrefixes) {
        synchronized (TAG) {
            StringTokenizer stringTokenizer = new StringTokenizer(stringPrefixes, ",");
            ArrayList<String> prefixes = new ArrayList<>();
            while (stringTokenizer.hasMoreElements()) {
                String prefix = stringTokenizer.nextToken();
                Log.i(TAG, "sms out prefixes = " + prefix);
                if (!prefixes.contains(prefix)) {
                    prefixes.add(prefix);
                }
            }
            smsOutPrefixes = prefixes;
        }
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }
}