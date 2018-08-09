package com.vttm.mochaplus.feature;

import android.app.Application;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.vttm.mochaplus.feature.business.ContactBusiness;
import com.vttm.mochaplus.feature.business.MessageBusiness;
import com.vttm.mochaplus.feature.data.DataManager;
import com.vttm.mochaplus.feature.di.component.ApplicationComponent;
import com.vttm.mochaplus.feature.di.component.DaggerApplicationComponent;
import com.vttm.mochaplus.feature.di.module.ApplicationModule;
import com.vttm.mochaplus.feature.utils.AppLogger;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ApplicationController extends Application {

    private PhoneNumberUtil phoneUtil;
    private ContactBusiness mContactBusiness;
    private MessageBusiness mMessageBusiness;
    private ReloadDataThread mReloadDataThread;

    @Inject
    DataManager mDataManager;

    @Inject
    CalligraphyConfig mCalligraphyConfig;

    private ApplicationComponent mApplicationComponent;

    private static ApplicationController applicationController;
    public static synchronized ApplicationController getInstance() {
        if (applicationController == null) {
            applicationController = new ApplicationController();
        }
        return applicationController;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        mApplicationComponent.inject(this);

        AppLogger.init();

        CalligraphyConfig.initDefault(mCalligraphyConfig);

//        Stetho.initialize(
//                Stetho.newInitializerBuilder(this)
//                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
//                        .build());
        createBusiness();
        initData();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    protected void createBusiness() {
        if (phoneUtil == null) {
            phoneUtil = PhoneNumberUtil.getInstance();
        }

        if (mContactBusiness == null) {
            mContactBusiness = new ContactBusiness(this);
        }
    }

    private void initBusiness()
    {
        mContactBusiness.init();
    }

    private void initData() {
//        if (!isDataReady()) {
            mReloadDataThread = null;
            mReloadDataThread = new ReloadDataThread();
            mReloadDataThread.setPriority(Thread.MAX_PRIORITY);
            mReloadDataThread.start();
//        } else {
//            // neu data da load xong thif check version code luon. khong thi cho khi load xong data
////            checkVersionCode();
//        }
    }

    private class ReloadDataThread extends Thread {
        @Override
        public void run() {
            initBusiness();
            boolean isNewAccount = false;
            if (!isNewAccount && !mContactBusiness.isSyncContact()) {// account cu va contact chua sync
                // neu db contact da duoc insert roi thi
                if (!mContactBusiness.isNewInsertDB()) {
                    mContactBusiness.syncContact();
                } else {
                    mContactBusiness.setNewInsertDB(false);
                }
            }
            mReloadDataThread = null;
        }
    }


    public PhoneNumberUtil getPhoneUtil() {
        if (phoneUtil == null) {
            phoneUtil = PhoneNumberUtil.getInstance();
        }
        return phoneUtil;
    }

    public ContactBusiness getContactBusiness() {
        return mContactBusiness;
    }

    public MessageBusiness getMessageBusiness() {
        return mMessageBusiness;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }
}
