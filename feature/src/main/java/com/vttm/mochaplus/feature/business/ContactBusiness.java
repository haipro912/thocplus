package com.vttm.mochaplus.feature.business;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.data.db.datasource.exceptions.RepositoryException;
import com.vttm.mochaplus.feature.data.db.model.ContactConstant;
import com.vttm.mochaplus.feature.helper.PhoneNumberHelper;
import com.vttm.mochaplus.feature.model.PhoneNumber;
import com.vttm.mochaplus.feature.utils.AppLogger;

import java.util.ArrayList;

/**
 * Created by HaiKE on 6/26/14.
 */
public class ContactBusiness {
    //http://stackoverflow.com/questions/31319918/how-to-add-my-apps-connection-in-phonebook-contacts-as-whatsapp-and
    // -viber-does
    private static final String TAG = ContactBusiness.class.getSimpleName();
    private ApplicationController mApplication;
    private Context mContext;
    private PhoneNumberUtil mPhoneUtil;
    private boolean syncContact = false;
    private boolean isNewInsertDB = false;

    public ContactBusiness(ApplicationController appication) {
        mApplication = appication;
        this.mContext = appication;
    }

    public void init() {
        mPhoneUtil = mApplication.getPhoneUtil();
    }

    public ArrayList<PhoneNumber> getListNumberFromDevice() {
        ArrayList<PhoneNumber> listNumbers = new ArrayList<>();
        Cursor cur = null;
        try {
            ContentResolver cr = mContext.getContentResolver();
            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.STARRED};
            cur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection, null, null, null);
            if (cur != null) {
                while (cur.moveToNext()) {
                    PhoneNumber number = getNumberFromCursor(cur, "VN");// get number
                    if (number != null) {
                        listNumbers.add(number);
                    }
                }
            }
        } catch (Exception e) {
            AppLogger.e(TAG, "Exception", e);
        } finally {
            closeCursor(cur);
        }
        return listNumbers;
    }

    private PhoneNumber getNumberFromCursor(Cursor cur, String regionCode) {
        PhoneNumber pNumber = new PhoneNumber();
        String nb = cur.getString(cur
                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        if (TextUtils.isEmpty(nb)) {// so rong
            return null;
        }
        Phonenumber.PhoneNumber phoneProtocol = PhoneNumberHelper.
                getInstant().getPhoneNumberProtocol(mPhoneUtil, nb, regionCode);
        if (PhoneNumberHelper.getInstant().isValidPhoneNumber(mPhoneUtil, phoneProtocol)) {// so hop le
            // lay jid
            String jidNumber = PhoneNumberHelper.getInstant().getNumberJidFromNumberE164(
                    mPhoneUtil.format(phoneProtocol, PhoneNumberUtil.PhoneNumberFormat.E164));
            pNumber.setJidNumber(jidNumber);
            // lay raw number
            /*String rawNumber = PhoneNumberHelper.getInstant().getRawNumber(mPhoneUtil, phoneProtocol, regionCode);
            pNumber.setRawNumber(rawNumber);*/
            String id = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            pNumber.setId(id);
            String contactId = cur.getString(cur
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            pNumber.setContactId(contactId);
            String name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            /* if (TextUtils.isEmpty(name)) {
                name = rawNumber;
            }*/
            pNumber.setPhoneProtocol(phoneProtocol);
            pNumber.setName(name);
            pNumber.setFavorite(cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)));
            return pNumber;
        } else {
            AppLogger.i(TAG, "phone invalid: " + nb);
        }
        return null;
    }

    // close cursor
    private void closeCursor(Cursor cur) {
        if (cur != null) {
            cur.close();
        }
    }

    public boolean isNewInsertDB() {
        return isNewInsertDB;
    }

    public void setNewInsertDB(boolean isNewInsertDB) {
        this.isNewInsertDB = isNewInsertDB;
    }

    public boolean isSyncContact() {
        return syncContact;
    }

    public void setSyncContact(boolean isSync) {
        this.syncContact = isSync;
    }

    public synchronized void syncContact() {

        ArrayList<PhoneNumber> datas = getListNumberFromDevice();
        ArrayList<ContactConstant> listContact = new ArrayList<>();

        for(int i = 0; i < datas.size(); i++)
        {
            ContactConstant contact = new ContactConstant();
            PhoneNumber phoneNumber = datas.get(i);
            contact.setNumber_id(phoneNumber.getId());
            contact.setContact_id(phoneNumber.getContactId());
            contact.setName(phoneNumber.getName());
            contact.setFavorite(phoneNumber.getFavorite());
            contact.setNumber(phoneNumber.getJidNumber());

            listContact.add(contact);
        }
        try {
            mApplication.getDataManager().insertAll(listContact);
            setSyncContact(true);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }
}