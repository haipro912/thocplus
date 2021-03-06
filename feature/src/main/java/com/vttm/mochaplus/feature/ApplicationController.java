package com.vttm.mochaplus.feature;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.vttm.mochaplus.feature.broadcast.NetworkChangeReceiver;
import com.vttm.mochaplus.feature.business.ApplicationStateManager;
import com.vttm.mochaplus.feature.business.BlockContactBusiness;
import com.vttm.mochaplus.feature.business.ContactBusiness;
import com.vttm.mochaplus.feature.business.ContentConfigBusiness;
import com.vttm.mochaplus.feature.business.LoginBusiness;
import com.vttm.mochaplus.feature.business.MessageBusiness;
import com.vttm.mochaplus.feature.business.ReengAccountBusiness;
import com.vttm.mochaplus.feature.business.SettingBusiness;
import com.vttm.mochaplus.feature.data.DataManager;
import com.vttm.mochaplus.feature.data.socket.xmpp.XMPPManager;
import com.vttm.mochaplus.feature.di.component.ApplicationComponent;
import com.vttm.mochaplus.feature.di.component.DaggerApplicationComponent;
import com.vttm.mochaplus.feature.di.module.ApplicationModule;
import com.vttm.mochaplus.feature.utils.AppLogger;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ApplicationController extends Application {

    private PhoneNumberUtil phoneUtil;
    private ContactBusiness contactBusiness;
    private MessageBusiness messageBusiness;
    private ReengAccountBusiness accountBusiness;
    private SettingBusiness settingBusiness;
    private LoginBusiness loginBusiness;
    private BlockContactBusiness blockContactBusiness;
    private ContentConfigBusiness configBusiness;
    private ReloadDataThread reloadDataThread;

    private XMPPManager xmppManager;
    private ApplicationStateManager mAppStateManager;
    private NetworkChangeReceiver networkReceiver;

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
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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
        if (xmppManager == null) {
            xmppManager = new XMPPManager(this);
        }

        if (mAppStateManager == null) {
            mAppStateManager = new ApplicationStateManager(this);
        }

        if (phoneUtil == null) {
            phoneUtil = PhoneNumberUtil.getInstance();
        }

        if (contactBusiness == null) {
            contactBusiness = new ContactBusiness(this);
        }

        if (accountBusiness == null) {
            accountBusiness = new ReengAccountBusiness(this);
            accountBusiness.init();
        }

        if (messageBusiness == null) {
            messageBusiness = new MessageBusiness(this);
        }

        if (settingBusiness == null) {
            settingBusiness = new SettingBusiness(this);
        }

        if (loginBusiness == null) {
            loginBusiness = new LoginBusiness(this);
        }

        if (blockContactBusiness == null) {
            blockContactBusiness = new BlockContactBusiness(this);
        }
        if (configBusiness == null) {
            configBusiness = new ContentConfigBusiness(this);
        }
    }

    private void initBusiness()
    {
        blockContactBusiness.init();
        configBusiness.init();
        contactBusiness.init();
        messageBusiness.init();
    }

    private void initData() {
//        if (!isDataReady()) {
            reloadDataThread = null;
            reloadDataThread = new ReloadDataThread();
            reloadDataThread.setPriority(Thread.MAX_PRIORITY);
            reloadDataThread.start();
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
            if (!isNewAccount && !contactBusiness.isSyncContact()) {// account cu va contact chua sync
                // neu db contact da duoc insert roi thi
                if (!contactBusiness.isNewInsertDB()) {
                    contactBusiness.syncContact();
                } else {
                    contactBusiness.setNewInsertDB(false);
                }
            }
            reloadDataThread = null;
        }
    }


    public PhoneNumberUtil getPhoneUtil() {
        if (phoneUtil == null) {
            phoneUtil = PhoneNumberUtil.getInstance();
        }
        return phoneUtil;
    }

    public ContactBusiness getContactBusiness() {
        return contactBusiness;
    }

    public MessageBusiness getMessageBusiness() {
        return messageBusiness;
    }

    public LoginBusiness getLoginBusiness() {
        return loginBusiness;
    }

    public ReengAccountBusiness getReengAccountBusiness() {
        return accountBusiness;
    }

    public BlockContactBusiness getBlockContactBusiness() {
        return blockContactBusiness;
    }

    public ContentConfigBusiness getConfigBusiness() {
        return configBusiness;
    }

    public ApplicationStateManager getAppStateManager() {
        return mAppStateManager;
    }

    public XMPPManager getXmppManager() {
        return xmppManager;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }
}
