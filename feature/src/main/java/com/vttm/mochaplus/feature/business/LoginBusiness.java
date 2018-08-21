package com.vttm.mochaplus.feature.business;

import android.content.res.Resources;

import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.data.socket.xmpp.XMPPManager;
import com.vttm.mochaplus.feature.data.socket.xmpp.XMPPResponseCode;
import com.vttm.mochaplus.feature.model.Region;
import com.vttm.mochaplus.feature.utils.AppLogger;

import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;

public class LoginBusiness {
    private static final String TAG = LoginBusiness.class.getSimpleName();
    private ApplicationController mApplicationController;
    private Resources mRes;
    private ArrayList<Region> mAllRegions;

    public LoginBusiness(ApplicationController app) {
        this.mApplicationController = app;
        mRes = mApplicationController.getResources();
        initRegion();
    }

    public ArrayList<Region> getAllRegions() {
        return mAllRegions;
    }


    /**
     * login by code
     *
     * @param mApplication
     * @param numberJid
     * @param password
     * @return response code
     */
    public XMPPResponseCode loginByCode(ApplicationController mApplication, String numberJid,
                                        String password, String regionCode, boolean createAccount) {
        XMPPManager mXMPPManager = mApplication.getXmppManager();
        XMPPResponseCode mXMPPResponseCode = new XMPPResponseCode();
        try {
            if (createAccount) {
                mApplicationController.getReengAccountBusiness().createReengAccountBeforeLogin(mApplication, numberJid, regionCode);
            }
            mXMPPManager.connectByCode(mApplicationController, numberJid, password, regionCode);
            mXMPPResponseCode.setCode(XMPPCode.E200_OK);
        } catch (IllegalStateException e) {
            mXMPPResponseCode.setCode(XMPPCode.E604_ERROR_XMPP_EXCEPTION);
            mXMPPResponseCode.setDescription(mRes.getString(R.string.e604_error_connect_server));
            AppLogger.e(TAG, "IllegalStateException  ", e);
        } catch (XMPPException e) {
            AppLogger.e(TAG, "Exception", e);
//            XMPPError xmppError = e.getXMPPError();
//            if (xmppError != null) {
//                int code = xmppError.getCode();
//                if (code == XMPPCode.E401_UNAUTHORIZED || code == XMPPCode.E409_RESOURCE_CONFLICT) {
//                    mXMPPResponseCode.setDescription(mApplication.getString(XMPPCode.getResourceOfCode(code)));
//                } else if (XMPPError.Condition.request_timeout.toString().equals(xmppError.getCondition())) {
//                    mXMPPResponseCode.setDescription(mApplication.getString(XMPPCode.getResourceOfCode(XMPPCode
//                            .E603_ERROR_IQ_NO_RESPONE)));
//                } else {
//                    mXMPPResponseCode.setDescription(mRes.getString(R.string.e604_error_connect_server));
//                }
//            } else if (e.getMessage() != null) {
//                mXMPPResponseCode.setDescription(XMPPCode.getNotifyStringForLogin(mApplication, e.getMessage()));
//            } else {
//                mXMPPResponseCode.setDescription(mRes.getString(R.string.e604_error_connect_server));
//            }
            AppLogger.e(TAG, "XMPPException ", e);
        } catch (Exception e) {
            AppLogger.e(TAG, "Exception", e);
        }
        if (mXMPPResponseCode.getCode() != XMPPCode.E200_OK) {
            //ket noi ko thanh cong
//            mApplication.trackingException("" + mXMPPResponseCode.getDescription(), false);
            mXMPPManager.destroyAnonymousConnection();
            XMPPManager.notifyXMPPDisconneted();
        }
        return mXMPPResponseCode;
    }

    private void initRegion() {
        mRes = mApplicationController.getResources();
        String[] regions = mRes.getStringArray(R.array.country_codes);
        mAllRegions = new ArrayList<>();
        if (regions != null) {
            for (String region : regions) {
                mAllRegions.add(new Region(region));
            }
        }
    }

    public Region getRegionByRegionCode(String regionCode) {
        if (mAllRegions == null) {
            initRegion();
        }
        regionCode = regionCode.toUpperCase();
        for (Region region : mAllRegions) {
            if (regionCode.equals(region.getRegionCode())) {
                return region;
            }
        }
        return null;
    }

    public int getPositionRegion(String regionCode) {
        if (mAllRegions == null) {
            initRegion();
        }
        regionCode = regionCode.toUpperCase();
        int size = mAllRegions.size();
        for (int i = 0; i < size; i++) {
            if (regionCode.equals(mAllRegions.get(i).getRegionCode())) {
                return i;
            }
        }
        return 0;
    }
}
