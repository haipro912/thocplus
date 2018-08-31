package com.vttm.mochaplus.feature.business;


import com.vttm.chatlib.utils.Log;
import com.vttm.mochaplus.feature.ApplicationController;
import com.vttm.mochaplus.feature.data.DataManager;
import com.vttm.mochaplus.feature.data.db.datasource.exceptions.RepositoryException;
import com.vttm.mochaplus.feature.data.db.utils.DbUtils;
import com.vttm.mochaplus.feature.helper.PrefixChangeNumberHelper;

import java.util.ArrayList;

/**
 * Created by toanvk2 on 7/10/14.
 */
public class BlockContactBusiness {
    private static BlockContactBusiness mInstance;
    private ArrayList<String> listBlock = new ArrayList<>();
    private DataManager dataManager;
    private ApplicationController mApplication;

    public BlockContactBusiness(ApplicationController application) {
        this.mApplication = application;

    }

    public void init() {
        dataManager = mApplication.getDataManager();
    }

    public ArrayList<String> getListBlock() {
        return listBlock;
    }

    public void setListBlock(ArrayList<String> mListBlock) {
        this.listBlock = mListBlock;
    }

    public boolean isReady() {
        return listBlock != null;
    }

    public void initBlockBusiness() {
        ArrayList<String> list = DbUtils.clone(dataManager.getAllBlockNumber());
        if (list == null) {
            setListBlock(new ArrayList<String>());
        } else {
            // remove duplicate number block
            setListBlock(list);
        }
    }

    public void updateListAfterBlockNumbers(ArrayList<String> numbers, boolean deleteAllColum) throws RepositoryException {
        if (listBlock == null) listBlock = new ArrayList<>();
        if (deleteAllColum) {
            dataManager.deleteAllBlockNumber();
            if (!listBlock.isEmpty()) {
                listBlock.clear();
            }
        }
        listBlock.addAll(numbers);
        dataManager.insertListBlockNumber(DbUtils.cloneBlock(numbers));
    }

    public void addBlockNumber(String number) throws RepositoryException {
        if (listBlock == null) listBlock = new ArrayList<>();
        listBlock.add(number);
        dataManager.insertBlockNumber(DbUtils.clone(number));
    }

    public void removeBlockNumber(String number) {
        String newNumber = PrefixChangeNumberHelper.getInstant(mApplication).convertNewPrefix(number);
        String oldNumber = PrefixChangeNumberHelper.getInstant(mApplication).getOldNumber(number);
        if (newNumber != null) {
            listBlock.remove(newNumber);
            dataManager.deleteBlockNumber(DbUtils.clone(newNumber));
        }
        if (oldNumber != null) {
            listBlock.remove(oldNumber);
            dataManager.deleteBlockNumber(DbUtils.clone(oldNumber));
        }
        listBlock.remove(number);
        dataManager.deleteBlockNumber(DbUtils.clone(number));
    }

    public void blockNumbers(ArrayList<String> numbers) throws RepositoryException {
        //Remove numbers khong nam trong danh sach block moi
        for (String num : listBlock) {
            Log.d("blockNumbers", "current number" + num);
            if (!isContain(num, numbers)) {
                dataManager.deleteBlockNumber(DbUtils.clone(num));
            }
        }
        initBlockBusiness();
        for (String num : listBlock) {
            Log.d("blockNumbers", "number after delete" + num);
        }
        //cap nhat danh sach block moi
        for (String num : numbers) {
            if (!isBlockNumber(num)) {
                listBlock.add(num);
                dataManager.insertBlockNumber(DbUtils.clone(num));
            }
        }
        for (String num : listBlock) {
            Log.d("blockNumbers", "number after complete" + num);
        }
    }

    public boolean isContain(String number, ArrayList<String> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        for (String item : list) {
            if (item.equals(number)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBlockNumber(String number) {
        if (listBlock == null || listBlock.isEmpty()) {
            return false;
        } else {
            for (String item : listBlock) {
                if (item.equals(number)) {
                    return true;
                }
                String newNumb = PrefixChangeNumberHelper.getInstant(mApplication).convertNewPrefix(item);
                if (newNumb != null) {
                    if (newNumb.equals(number)) return true;
                }

                String oldNumb = PrefixChangeNumberHelper.getInstant(mApplication).getOldNumber(item);
                if (oldNumb != null) {
                    if (oldNumb.equals(number)) return true;
                }
            }
            return false;
        }
    }
}