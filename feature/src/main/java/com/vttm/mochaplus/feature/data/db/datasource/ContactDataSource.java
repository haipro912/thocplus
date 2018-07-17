package com.vttm.mochaplus.feature.data.db.datasource;

import com.vttm.mochaplus.feature.data.db.datasource.exceptions.RepositoryException;
import com.vttm.mochaplus.feature.data.db.model.ContactConstant;
import com.vttm.mochaplus.feature.data.db.providers.RealmProvider;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ContactDataSource implements BaseDataSource<ContactConstant> {
    private RealmProvider realmProvider;

    @Inject
    public ContactDataSource(RealmProvider realmProvider) {
        this.realmProvider = realmProvider;
    }

    @Override
    public List<ContactConstant> findAll() {
        List<ContactConstant> provinceDbs = getRealm().where(ContactConstant.class).findAll();
        return provinceDbs;
    }

    @Override
    public ContactConstant insert(ContactConstant element) throws RepositoryException {
        getRealm().beginTransaction();
        ContactConstant returnedCarDb;
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
    public List<ContactConstant> insertAll(List<ContactConstant> elementList) throws RepositoryException {
        getRealm().beginTransaction();
        List<ContactConstant> returnedCarsDb;
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
    public void remove(ContactConstant element) {
        getRealm().beginTransaction();
        RealmQuery<ContactConstant> realmQuery = getRealm().where(ContactConstant.class).equalTo(ContactConstant.CONTACT_NAME, element.getName());
        RealmResults<ContactConstant> realmResults = realmQuery.findAll();
        realmResults.deleteAllFromRealm();
        getRealm().commitTransaction();
        getRealm().close();

    }

    @Override
    public void removeAll() {
        getRealm().beginTransaction();
        getRealm().where(ContactConstant.class).findAll().deleteAllFromRealm();
        getRealm().commitTransaction();
        getRealm().close();
    }

    private Realm getRealm() {
        return realmProvider.getDatabase();
    }
}
