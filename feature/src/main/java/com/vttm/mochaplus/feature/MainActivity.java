package com.vttm.mochaplus.feature;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.vttm.mochaplus.feature.mvp.base.BaseActivity;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;
import com.vttm.mochaplus.feature.mvp.main.IMainView;
import com.vttm.mochaplus.feature.mvp.main.MainFragment;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.utils.AppLogger;
import com.vttm.mochaplus.feature.utils.ToastUtils;

public class MainActivity extends BaseActivity  implements IMainView{

    public final String TAG = getClass().getSimpleName();
    private static final int COUNT_DONW = 2000;
    private boolean isTouchTwoTimes = false;
    private BaseFragment currentFragment;
//    private RxPermissions rxPermissions;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);

        setUp();
    }

    @Override
    protected void setUp() {
//        rxPermissions = new RxPermissions(this);
//        requestPermission();
        showFragment(AppConstants.TAB_MAIN, null);
    }


    @Override
    public void showFragment(int tabId, Bundle bundle) {
        switch (tabId) {
            case AppConstants.TAB_MAIN:
                currentFragment = MainFragment.newInstance();
                break;
        }

        if (currentFragment != null) {
            if (!getSupportFragmentManager().getFragments().contains(currentFragment)) {
                try {
                    if (!currentFragment.isAdded()) {
                        if (currentFragment.getArguments() == null)
                            currentFragment.setArguments(bundle);
                        else
                            currentFragment.getArguments().putAll(bundle);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .disallowAddToBackStack()
                                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                                .add(R.id.fragment_container, currentFragment, currentFragment.TAG)
                                .commitAllowingStateLoss();
                    }
                } catch (IllegalStateException e) {
                    AppLogger.e(TAG,e);

                } catch (RuntimeException e) {
                    AppLogger.e(TAG,e);

                } catch (Exception ex) {
                    AppLogger.e(TAG,ex);

                }
            }
        }
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .remove(fragment)
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        goPrevTab();
    }

    public void goPrevTab() {
        try {
            FragmentManager mFragmentManager = getSupportFragmentManager();
            int entryCount = mFragmentManager.getFragments().size();
            if (entryCount > 0) {
                for (int i = entryCount - 1; i >= 0; i--) {
                    Fragment fragment = mFragmentManager.getFragments().get(i);
                    if (fragment != null) {
                        if (fragment instanceof MainFragment) {
                            exitApp();
                            break;
                        } else if (fragment instanceof SupportRequestManagerFragment) {
                            continue;
                        } else {
                            onFragmentDetached(fragment.getTag());
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
//            Log.e(TAG, ex);
            AppLogger.e(TAG,ex);

        }
    }

    private void exitApp() {
        if (!isTouchTwoTimes) {
            ToastUtils.makeText(this, getString(R.string.back_exit));
            isTouchTwoTimes = true;
            new CountDownTimer(COUNT_DONW, COUNT_DONW) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    isTouchTwoTimes = false;
                }
            }.start();
            return;
        }
        finishAffinity();
        System.runFinalizersOnExit(true);
        System.exit(0);
    }


//    private void requestPermission()
//    {
//        rxPermissions.requestEach(Manifest.permission.READ_CONTACTS,
//                        Manifest.permission.READ_PHONE_STATE)
//                .subscribe(permission -> { // will emit 2 Permission objects
//                    if (permission.granted) {
//                        // `permission.name` is granted !
//                        AppLogger.i(TAG, "pass");
//                    } else if (permission.shouldShowRequestPermissionRationale) {
//                        // Bỏ qua ko hỏi lại
//                        AppLogger.i(TAG, "Bỏ qua ko hỏi lại");
//                    } else {
//                        AppLogger.i(TAG, "Bỏ qua");
//                        // Denied permission with ask never again
//                        // Need to go to the settings
//                    }
//                });
//    }











    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


}
