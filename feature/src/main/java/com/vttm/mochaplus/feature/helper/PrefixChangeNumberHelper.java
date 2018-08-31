package com.vttm.mochaplus.feature.helper;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.res.Resources;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.model.ReengAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thanhnt72 on 8/7/2018.
 */

public class PrefixChangeNumberHelper {

    private static PrefixChangeNumberHelper mInstant;
    private ApplicationController mApp;

    public static final String[] PREFIX_OLD = {
            "0120", "0121", "0122", "0126", "0128"
            , "0123", "0124", "0125", "0127", "0129"
            , "0162", "0163", "0164", "0165", "0166", "0167", "0168", "0169"
            , "0186", "0188", "0199"};
    public static final String[] PREFIX_NEW = {
            "070", "079", "077", "076", "078"
            , "083", "084", "085", "081", "082"
            , "032", "033", "034", "035", "036", "037", "038", "039"
            , "056", "058", "059"};

    public static synchronized PrefixChangeNumberHelper getInstant(ApplicationController mApp) {
        if (mInstant == null) {
            mInstant = new PrefixChangeNumberHelper(mApp);
        }
        return mInstant;
    }

    public PrefixChangeNumberHelper(ApplicationController mApp) {
        this.mApp = mApp;
    }

    public String convertNewPrefix(String oldNumber) {
        if (TextUtils.isEmpty(oldNumber)) return null;
        if (mApp.getConfigBusiness().isDisableChangePrefix()) return null;
        if (mApp.getConfigBusiness().needChangePrefix(oldNumber)) {
            String prefix = oldNumber.substring(0, 4);
            String prefixNew = mApp.getConfigBusiness().getHashChangeNumberOld2New().get(prefix);
            oldNumber = oldNumber.replaceFirst(prefix, prefixNew);
            return oldNumber;
        } else
            return null;
    }

    public String getOldNumber(String newNumber) {
        if (TextUtils.isEmpty(newNumber)) return null;
        if (mApp.getConfigBusiness().isDisableChangePrefix()) return null;
        int length = newNumber.length();
        if (length == 10) {
            String prefix = newNumber.substring(0, 3);
            if (mApp.getConfigBusiness().getHashChangeNumberNew2Old().containsKey(prefix)) {
                String prefixOld = mApp.getConfigBusiness().getHashChangeNumberNew2Old().get(prefix);
                newNumber = newNumber.replaceFirst(prefix, prefixOld);
                if (mApp.getConfigBusiness().needChangePrefix(newNumber)) {
                    return newNumber;
                }
            }
        }
        return null;
    }

//    public void showDialogFriendChangePrefix(final PhoneNumber phoneNumber, final String newNumber,
//                                             final BaseSlidingFragmentActivity activity, final EditContactSuccess listener) {
//        String msg = String.format(activity.getResources().getString(R.string.change_prefix_msg_contact_update_old),
//                phoneNumber.getName());
//        showDialogSoloNumber(msg, phoneNumber, newNumber, activity, listener);
//    }
//
//    public void showDialogFriendNewNumber(final PhoneNumber phoneNumber, final String newNumber,
//                                          final BaseSlidingFragmentActivity activity, final EditContactSuccess listener) {
//        String msg = String.format(activity.getResources().getString(R.string.change_prefix_msg_contact_update_new),
//                phoneNumber.getNewNumber(), phoneNumber.getName());
//        showDialogSoloNumber(msg, phoneNumber, newNumber, activity, listener);
//    }
//
//
//    private void showDialogSoloNumber(String msg, final PhoneNumber phoneNumber, final String newNumber,
//                                      final BaseSlidingFragmentActivity activity, final EditContactSuccess listener) {
//        ArrayList<Object> list = new ArrayList<>();
//        list.add(phoneNumber);
//        final DialogChangePrefixNumber dialogConfirm = new DialogChangePrefixNumber(activity, true, list);
//        dialogConfirm.setMessage(msg);
//        dialogConfirm.setPositiveListener(new PositiveListener<Object>() {
//            @Override
//            public void onPositive(Object result) {
//                if (PermissionHelper.declinedPermission(activity, Manifest.permission.WRITE_CONTACTS)) {
//                    PermissionHelper.requestPermission(activity,
//                            Manifest.permission.WRITE_CONTACTS,
//                            Constants.PERMISSION.PERMISSION_REQUEST_EDIT_CONTACT);
//                } else {
//                    activity.showLoadingDialog("", R.string.loading);
//                    boolean isSuccess = updateContact(phoneNumber.getJidNumber(), phoneNumber.getContactId(), newNumber);
//                    HashMap<String, String> listNumber = new HashMap<>();
//                    listNumber.put(phoneNumber.getJidNumber(), newNumber);
//                    listener.onEditContact(isSuccess, listNumber, null);
////                editContact(activity, phoneNumber.getContactId());
//                }
//
//            }
//        });
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                activity.setCurrentPrefixDialog(dialogConfirm);
//                dialogConfirm.show();
//            }
//        });
//
//    }
//
//    public void showDialogNonContactChangePrefix(final NonContact nonContact,
//                                                 final BaseSlidingFragmentActivity activity,
//                                                 PositiveListener<Object> listener) {
//
//
//        Resources res = activity.getResources();
//        String msg = String.format(res.getString(
//                R.string.change_prefix_msg_contact_update_old), nonContact.getJidNumber());
//        ArrayList<Object> listObj = new ArrayList<>();
//        listObj.add(nonContact);
//        final DialogChangePrefixNumber dialogConfirm = new DialogChangePrefixNumber(activity, true, listObj);
//        dialogConfirm.setMessage(msg);
//        dialogConfirm.setPositiveListener(listener);
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                activity.setCurrentPrefixDialog(dialogConfirm);
//                dialogConfirm.show();
//            }
//        });
//    }
//
//    public void showDialogGroupChangePrefix(final HashMap<String, PhoneNumber> listChange,
//                                            final HashMap<String, String> listChangeNonContact,
//                                            final BaseSlidingFragmentActivity activity,
//                                            final EditContactSuccess listener) {
//
//        if (!listChange.isEmpty()) {
//            ArrayList<Object> list = new ArrayList<>();
//            for (Map.Entry<String, PhoneNumber> entry : listChange.entrySet()) {
////                String key = entry.getKey();
//                PhoneNumber p = entry.getValue();
//                list.add(p);
//            }
//
//            Resources res = activity.getResources();
//            String msg;
//            if (list.size() == 1)
//                msg = res.getString(R.string.change_prefix_msg_group_single_change);
//            else
//                msg = String.format(res.getString(R.string.change_prefix_msg_group), list.size());
//            final DialogChangePrefixNumber dialogConfirm = new DialogChangePrefixNumber(activity, true, list);
//            dialogConfirm.setMessage(msg);
//            dialogConfirm.setPositiveListener(new PositiveListener<Object>() {
//                @Override
//                public void onPositive(Object result) {
//                    if (PermissionHelper.declinedPermission(activity, Manifest.permission.WRITE_CONTACTS)) {
//                        PermissionHelper.requestPermission(activity,
//                                Manifest.permission.WRITE_CONTACTS,
//                                Constants.PERMISSION.PERMISSION_REQUEST_EDIT_CONTACT);
//                    } else {
//                        activity.showLoadingDialog("", R.string.loading);
//                        HashMap<String, String> listEditSuccess = processEditListContact(listChange, activity);
//                        listener.onEditContact(!listEditSuccess.isEmpty(), listEditSuccess, listChangeNonContact);
////                editContact(activity, phoneNumber.getContactId());
//                    }
//
//                }
//            });
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    activity.setCurrentPrefixDialog(dialogConfirm);
//                    dialogConfirm.show();
//                }
//            });
//        }
//
//    }
//
//    private HashMap<String, String> processEditListContact(HashMap<String, PhoneNumber> listEdit, BaseSlidingFragmentActivity activity) {
//        HashMap<String, String> listEditSuccess = new HashMap<>();
//
//        for (Map.Entry<String, PhoneNumber> entry : listEdit.entrySet()) {
////                String key = entry.getKey();
//            PhoneNumber p = entry.getValue();
//            boolean isSuccess = updateContact(p.getJidNumber(), p.getContactId(), p.getNewNumber());
//            if (isSuccess) listEditSuccess.put(p.getJidNumber(), p.getNewNumber());
//        }
//        return listEditSuccess;
//    }
//
//
//    private boolean updateContact(String oldNumber, String contactId, String newNumber) {
//        Cursor phones = mApp.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE}
//                , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "='" + contactId + "'", null, null);
//
//        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
//        boolean isSuccess = false;
//        while (phones.moveToNext()) {
//            try {
//                String rawId = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
//                String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                String type = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
//
//                String formatNumber = null;
//
//                ReengAccount account = mApp.getReengAccountBusiness().getCurrentAccount();
//                Phonenumber.PhoneNumber phoneNumberProtocol = PhoneNumberHelper.getInstant().
//                        getPhoneNumberProtocol(mApp.getPhoneUtil(), number, account.getRegionCode());
//                if (phoneNumberProtocol != null) {
//                    formatNumber = PhoneNumberHelper.getInstant().getNumberJidFromNumberE164(
//                            mApp.getPhoneUtil().format(phoneNumberProtocol, PhoneNumberUtil.PhoneNumberFormat
//                                    .E164));
//                }
//
//                if (formatNumber == null || !formatNumber.equals(oldNumber))
//                    continue;
//
//                ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
//                builder.withSelection(ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?"
//                                + " AND " + ContactsContract.Data.MIMETYPE + "=?"
//                                + " AND " + ContactsContract.CommonDataKinds.Phone.NUMBER + "=?"
//                                + " AND " + ContactsContract.CommonDataKinds.Phone.TYPE + "=?",
//                        new String[]{rawId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, number, type});
//                builder.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newNumber);
//                ops.add(builder.build());
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        try {
//            ContentProviderResult[] array = mApp.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
//            if (array.length > 0) isSuccess = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        phones.close();
//        return isSuccess;
//    }
//
//
//    public interface EditContactSuccess {
//        void onEditContact(boolean isSuccess, HashMap<String, String> newNumber, HashMap<String, String> listNonContact);
//
////        void onEditNonContact(boolean isSuccess, HashMap<String, String> listChangeSuccess);
//    }
//
//    public static class ChangeNumberObject {
//
//        public static final int STATE_NON_CONTACT = 0;
//        public static final int STATE_FRIEND_NEED_CHANGE = 1;
//        public static final int STATE_FRIEND_CHANGED = 2;
//        public static final int STATE_AUTO_UPDATE_VIEW = 3;
//
//        int state = -1;
//        String oldNumber;
//        String newNumber;
//        PhoneNumber phoneNumber;
//        NonContact nonContact;
//
//        public ChangeNumberObject(int state) {
//            this.state = state;
//        }
//
//        public ChangeNumberObject(int state, String oldNumber, String newNumber, PhoneNumber phoneNumber) {
//            this.state = state;
//            this.oldNumber = oldNumber;
//            this.newNumber = newNumber;
//            this.phoneNumber = phoneNumber;
//        }
//
//        public int getState() {
//            return state;
//        }
//
//        public String getOldNumber() {
//            return oldNumber;
//        }
//
//        public String getNewNumber() {
//            return newNumber;
//        }
//
//        public PhoneNumber getPhoneNumber() {
//            return phoneNumber;
//        }
//
//        public NonContact getNonContact() {
//            return nonContact;
//        }
//
//        public void setNonContact(NonContact nonContact) {
//            this.nonContact = nonContact;
//        }
//    }
//
//
//    public interface UpdateThreadChat {
//        void onUpdateThreadChat(int threadId);
//    }

}
