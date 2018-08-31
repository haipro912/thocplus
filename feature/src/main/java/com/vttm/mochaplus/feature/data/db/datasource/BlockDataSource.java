package com.vttm.mochaplus.feature.data.db.datasource;

import com.vttm.mochaplus.feature.data.db.datasource.exceptions.RepositoryException;
import com.vttm.mochaplus.feature.data.db.model.BlockConstant;
import com.vttm.mochaplus.feature.data.db.providers.RealmProvider;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class BlockDataSource implements BaseDataSource<BlockConstant> {
    private RealmProvider realmProvider;

    @Inject
    public BlockDataSource(RealmProvider realmProvider) {
        this.realmProvider = realmProvider;
    }

    @Override
    public List<BlockConstant> findAll() {
        List<BlockConstant> provinceDbs = getRealm().where(BlockConstant.class).findAll();
        return provinceDbs;
    }

    @Override
    public BlockConstant insert(BlockConstant element) throws RepositoryException {
        getRealm().beginTransaction();
        BlockConstant returnedCarDb;
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
    public List<BlockConstant> insertAll(List<BlockConstant> elementList) throws RepositoryException {
        getRealm().beginTransaction();
        List<BlockConstant> returnedCarsDb;
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
    public void remove(BlockConstant element) {
        getRealm().beginTransaction();
        RealmQuery<BlockConstant> realmQuery = getRealm().where(BlockConstant.class).equalTo(BlockConstant.NUMBER, element.getNumber());
        RealmResults<BlockConstant> realmResults = realmQuery.findAll();
        realmResults.deleteAllFromRealm();
        getRealm().commitTransaction();
        getRealm().close();

    }

    @Override
    public void removeAll() {
        getRealm().beginTransaction();
        getRealm().where(BlockConstant.class).findAll().deleteAllFromRealm();
        getRealm().commitTransaction();
        getRealm().close();
    }

    private Realm getRealm() {
        return realmProvider.getDatabase();
    }
}