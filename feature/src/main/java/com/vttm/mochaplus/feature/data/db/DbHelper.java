package com.vttm.mochaplus.feature.data.db;

import com.vttm.mochaplus.feature.data.db.datasource.exceptions.RepositoryException;
import com.vttm.mochaplus.feature.data.db.model.CallHistoryConstant;
import com.vttm.mochaplus.feature.data.db.model.ContactConstant;

import java.util.List;

/**
 * Created by HaiKE on 6/7/17.
 */

public interface DbHelper {
    List<ContactConstant> getListContact();
    List<CallHistoryConstant> getListCallHistory();

    List<ContactConstant> insertAll(List<ContactConstant> elementList) throws RepositoryException;
}
