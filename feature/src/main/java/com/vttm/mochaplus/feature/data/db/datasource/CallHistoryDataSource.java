package com.vttm.mochaplus.feature.data.db.datasource;

import com.vttm.mochaplus.feature.data.db.datasource.exceptions.RepositoryException;
import com.vttm.mochaplus.feature.data.db.model.CallHistoryConstant;
import com.vttm.mochaplus.feature.data.db.providers.RealmProvider;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class CallHistoryDataSource  implements BaseDataSource<CallHistoryConstant> {
    private RealmProvider realmProvider;

    @Inject
    public CallHistoryDataSource(RealmProvider realmProvider) {
        this.realmProvider = realmProvider;
    }

    @Override
    public List<CallHistoryConstant> findAll() {
        List<CallHistoryConstant> provinceDbs = getRealm().where(CallHistoryConstant.class).findAll();
        return provinceDbs;
    }

    @Override
    public CallHistoryConstant insert(CallHistoryConstant element) throws RepositoryException {
        getRealm().beginTransaction();
        CallHistoryConstant returnedCarDb;
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
    public List<CallHistoryConstant> insertAll(List<CallHistoryConstant> elementList) throws RepositoryException {
        getRealm().beginTransaction();
        List<CallHistoryConstant> returnedCarsDb;
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
    public void remove(CallHistoryConstant element) {
        getRealm().beginTransaction();
        RealmQuery<CallHistoryConstant> realmQuery = getRealm().where(CallHistoryConstant.class).equalTo(CallHistoryConstant.FRIEND_NAME, element.getFriend_number());
        RealmResults<CallHistoryConstant> realmResults = realmQuery.findAll();
        realmResults.deleteAllFromRealm();
        getRealm().commitTransaction();
        getRealm().close();

    }

    @Override
    public void removeAll() {
        getRealm().beginTransaction();
        getRealm().where(CallHistoryConstant.class).findAll().deleteAllFromRealm();
        getRealm().commitTransaction();
        getRealm().close();
    }

    private Realm getRealm() {
        return realmProvider.getDatabase();
    }
}