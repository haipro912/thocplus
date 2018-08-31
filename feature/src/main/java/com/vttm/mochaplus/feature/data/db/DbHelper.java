package com.vttm.mochaplus.feature.data.db;

import com.vttm.mochaplus.feature.data.db.datasource.exceptions.RepositoryException;
import com.vttm.mochaplus.feature.data.db.model.BlockConstant;
import com.vttm.mochaplus.feature.data.db.model.CallHistoryConstant;
import com.vttm.mochaplus.feature.data.db.model.ContactConstant;
import com.vttm.mochaplus.feature.data.db.model.ReengAccountConstant;

import java.util.List;

/**
 * Created by HaiKE on 6/7/17.
 */

public interface DbHelper {
    List<ContactConstant> getListContact();
    List<CallHistoryConstant> getListCallHistory();

    List<ContactConstant> insertAllContact(List<ContactConstant> elementList) throws RepositoryException;


    //Reeng Account Datasource
    ReengAccountConstant getAccount();
    void updateAccount(ReengAccountConstant account);
    void updateAccountToken(long reengAccountID, String token);
    ReengAccountConstant insertAccount(ReengAccountConstant element) throws RepositoryException;
    void removeAccount(ReengAccountConstant element);

    //Block datasource
    List<BlockConstant> getAllBlockNumber();
    void deleteAllBlockNumber();
    void deleteBlockNumber(BlockConstant number);
    void insertListBlockNumber(List<BlockConstant> numbers) throws RepositoryException;
    void insertBlockNumber(BlockConstant number) throws RepositoryException;
}
