package com.vttm.mochaplus.feature.data.db.datasource;

import com.vttm.mochaplus.feature.data.db.datasource.exceptions.RepositoryException;
import com.vttm.mochaplus.feature.data.db.model.ReengAccountConstant;
import com.vttm.mochaplus.feature.data.db.providers.RealmProvider;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ReengAccountDataSource implements BaseDataSource<ReengAccountConstant> {
    private RealmProvider realmProvider;

    @Inject
    public ReengAccountDataSource(RealmProvider realmProvider) {
        this.realmProvider = realmProvider;
    }
    @Override
    public List<ReengAccountConstant> findAll() {
        List<ReengAccountConstant> provinceDbs = getRealm().where(ReengAccountConstant.class).findAll();
        return provinceDbs;
    }

    @Override
    public ReengAccountConstant insert(ReengAccountConstant element) throws RepositoryException {
        getRealm().beginTransaction();
        ReengAccountConstant returnedCarDb;
        try {
            returnedCarDb = getRealm().copyToRealm(element);
            getRealm().commitTransaction();
        } catch (Exception e) {

            getRealm().cancelTransaction();
            throw new RepositoryException(e);
        } finally {
            getRealm().close();
        }
        return returnedCarDb;
    }

    @Override
    public List<ReengAccountConstant> insertAll(List<ReengAccountConstant> elementList) throws RepositoryException {
        getRealm().beginTransaction();
        List<ReengAccountConstant> returnedCarsDb;
        try {
            returnedCarsDb = getRealm().copyToRealm(elementList);
        } catch (Exception e) {
            throw new RepositoryException(e);
        } finally {
            getRealm().commitTransaction();
            getRealm().close();
        }
        return returnedCarsDb;
    }

    @Override
    public void remove(ReengAccountConstant element) {
        getRealm().beginTransaction();
        RealmQuery<ReengAccountConstant> realmQuery = getRealm().where(ReengAccountConstant.class).equalTo(ReengAccountConstant.ID, element.getId());
        RealmResults<ReengAccountConstant> realmResults = realmQuery.findAll();
        realmResults.deleteAllFromRealm();
        getRealm().commitTransaction();
        getRealm().close();
    }

    @Override
    public void removeAll() {
        getRealm().beginTransaction();
        getRealm().where(ReengAccountConstant.class).findAll().deleteAllFromRealm();
        getRealm().commitTransaction();
        getRealm().close();
    }

    public ReengAccountConstant getAccount()
    {
        List<ReengAccountConstant> datas = findAll();
        if(datas != null && datas.size() > 0)
            return datas.get(0);
        return null;
    }

    public void update(ReengAccountConstant account) {
        ReengAccountConstant object = getRealm().where(ReengAccountConstant.class)
                .equalTo(ReengAccountConstant.ID, account.getId())
                .findFirst();
        getRealm().beginTransaction();
        if (object == null) {
            //Neu bang null thi co the tao moi object insert vao
//            ReengAccountConstant object = getRealm().createObject(ReengAccountConstant.class);
            // set the fields here
        } else {
            object.setIs_active(account.getIs_active());
            object.setName(account.getName());
            object.setPhone_number(account.getPhone_number());
            object.setToken(account.getToken());
            object.setAvatar_last_change(account.getAvatar_last_change());
            object.setStatus(account.getStatus());
            object.setGender(account.getGender());
            object.setBirthday(account.getBirthday());
            object.setRegion_code(account.getRegion_code());
            object.setBirthday_string(account.getBirthday_string());
            object.setAvatar_path(account.getAvatar_path());
            object.setNeed_upload(account.getNeed_upload());
            object.setPermission(account.getPermission());
            object.setAvno_number(account.getAvno_number());
            object.setIc_front(account.getIc_front());
            object.setIc_back(account.getIc_back());
        }
        getRealm().commitTransaction();
    }

    public void updateToken(long reengAccountID, String token) {
        getRealm().beginTransaction();
        ReengAccountConstant object = getRealm().where(ReengAccountConstant.class)
                .equalTo(ReengAccountConstant.ID, reengAccountID)
                .findFirst();
        if (object == null) {
            //Neu bang null thi co the tao moi object insert vao
//            ReengAccountConstant object = getRealm().createObject(ReengAccountConstant.class);
            // set the fields here
        } else {
            object.setToken(token);
        }
        getRealm().commitTransaction();
    }

    private Realm getRealm() {
        return realmProvider.getDatabase();
    }
}
