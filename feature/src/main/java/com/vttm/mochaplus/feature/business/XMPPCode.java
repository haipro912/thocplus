package com.vttm.mochaplus.feature.business;

import android.content.Context;
import android.content.res.Resources;

import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.helper.TimeHelper;
import com.vttm.mochaplus.feature.utils.TextUtils;

/**
 * Created by thaodv on 6/17/2014.
 */

public class XMPPCode {
    //Code duoc sap xep theo thu tu tang dan
    //----------------2xx Code--------------------------
    public static final int E200_OK = 200; //200 E200_OK
    //----------------3xx Code--------------------------
    //----------------4xx Code--------------------------
    public static final int E401_UNAUTHORIZED = 401; //Xác thực sai
    public static final int E402_NO_PERMISSION_ADMIN = 402; //khong co quyen admin
    public static final int E403_NUMBER_OVER_MAX = 403; //Số lượng thành viên vượt quá giới hạn cho phép của nhóm
    public static final int E404_GROUP_NO_LONGER_EXIST = 404; //Nhóm chat không còn tồn tại
    public static final int E405_GROUP_ADMIN_EXIST = 405;// Nhom da co admin
    public static final int E406_ALL_USERS_UNAVAILABLE = 406;//Tất cả số điện thoại không sử dụng dịch vụ chat
    public static final int E409_RESOURCE_CONFLICT = 409; //resource conflict
    public static final int E415_NOT_ALLOW_LEAVE_ROOM = 415;// khong phai la thanh vien cua nhom
    public static final int E416_NOT_ALLOW_INVITE = 416;// khong phai la thanh vien cua nhom
    public static final int E423_GROUP_NAME_ERROR = 423;//Ten nhom khong hop le
    public static final int E490_ILLEGAL_STATE_EXCEPTION = 490;
    //----------------5xx Code---------------------------
    public static final int E500_INTERNAL_SERVER_ERROR = 500;//loi xu ly cua server
    public static final int E503_USER_UNAVAILABLE = 503; //Số điện thoại không sử dụng dịch vụ hoac service_unavailable
    public static final int E505_USER_NOT_SUPPORT_FUNCTION = 505; // số đt sử dụng không hỗ trợ tính năng này
    public static final int E550_GENERATE_PASSWORD_OVER_LIMIT = 550;//qua so lan gui request sinh pass
    //----------------6xx Code---------------------------
    public static final int E601_ERROR_BUT_UNDEFINED = 601; //co error trong ban tin nhung chua ro type la gi
    public static final int E602_ERROR_NULL_TYPE = 602;//khong co truong type trong ban tin tra ve
    public static final int E603_ERROR_IQ_NO_RESPONE = 603; //ko co ban tin IQ tra ve
    public static final int E604_ERROR_XMPP_EXCEPTION = 604; //loi XMPP Exception
    public static final int E666_ERROR_NOT_SUPPORT = 666;//loi chuc nang chua ho tro
    public static final int E560_ERROR_OVER_TOTAL_MESSAGES = 560;
    public static final int E561_ERROR_OVER_24H_MESSAGES = 561;
    public static final int E409_ALREADY_IN_GROUP = 409;

    /**
     * @param responseCode
     * @return string resource id of @responseCode
     */
    public static int getResourceOfCode(int responseCode) {
        int stringResourceId;
        switch (responseCode) {
            case E200_OK:
                stringResourceId = R.string.e200_ok;
                break;
            case E401_UNAUTHORIZED:
                stringResourceId = R.string.e401_unauthorized;
                break;
            case E402_NO_PERMISSION_ADMIN:
                stringResourceId = R.string.e402_no_permission_admin;
                break;
            case E403_NUMBER_OVER_MAX:
                stringResourceId = R.string.e403_number_over_max;
                break;
            case E404_GROUP_NO_LONGER_EXIST:
                stringResourceId = R.string.e404_group_no_longer_exist;
                break;
            case E405_GROUP_ADMIN_EXIST:
                stringResourceId = R.string.e405_group_admin_exist;
                break;
            case E406_ALL_USERS_UNAVAILABLE:
                stringResourceId = R.string.e406_required_information_not_provided;
                break;
            case E409_RESOURCE_CONFLICT:
                stringResourceId = R.string.e409_resource_conflict;
                break;
            case E415_NOT_ALLOW_LEAVE_ROOM:
                stringResourceId = R.string.e416_not_allow_invite;
                break;
            case E416_NOT_ALLOW_INVITE:
                stringResourceId = R.string.e416_not_allow_invite;
                break;
            case E423_GROUP_NAME_ERROR:
                stringResourceId = R.string.e423_group_name_error;
                break;
            case E490_ILLEGAL_STATE_EXCEPTION:
                stringResourceId = R.string.e490_illegal_state_exception;
                break;
            case E500_INTERNAL_SERVER_ERROR:
                stringResourceId = R.string.e500_internal_server_error;
                break;
            case E503_USER_UNAVAILABLE:
                stringResourceId = R.string.e503_user_unavailable;
                break;
            case E505_USER_NOT_SUPPORT_FUNCTION:
                stringResourceId = R.string.e505_user_notsupport_function;
                break;
            case E550_GENERATE_PASSWORD_OVER_LIMIT:
                stringResourceId = R.string.e550_password_over_limit;
                break;
            case E601_ERROR_BUT_UNDEFINED:
                stringResourceId = R.string.e601_error_but_undefined;
                break;
            case E602_ERROR_NULL_TYPE:
                stringResourceId = R.string.e601_error_but_undefined;
                break;
            case E603_ERROR_IQ_NO_RESPONE:
                stringResourceId = R.string.e603_error_iq_no_respone;
                break;
            case E604_ERROR_XMPP_EXCEPTION:
                stringResourceId = R.string.e604_error_connect_server;
                break;
            case E666_ERROR_NOT_SUPPORT:
                stringResourceId = R.string.e666_not_support_function;
                break;
            default:
                stringResourceId = R.string.error_unidentify;
                break;
        }
        return stringResourceId;
    }

    public static String getNotifyStringForLogin(Context ctx, String exception) {
        final String lockedException = "Client is locked:";
        final String loginFailButRetry = "SASL authentication failed:";
        if (exception.contains(loginFailButRetry)) {
            //            int numberRetry = Integer.valueOf(exception.substring(loginFailButRetry.length()).trim());
            return ctx.getString(R.string.login_fail_and_retry);
        } else if (exception.contains(lockedException)) {
            long timeLock = TextUtils.parserLongFromString(exception.substring(lockedException.length()).trim(), 0);
            Resources res = ctx.getResources();
            return TimeHelper.formatTimeLocked(timeLock, res);
        } else return ctx.getString(R.string.e604_error_connect_server);
    }
}