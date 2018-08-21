package com.vttm.mochaplus.feature.model;

import java.io.Serializable;

/**
 * Created by toanvk2 on 26/05/2015.
 */
public class Region implements Serializable {
    private String regionName;
    private String regionCode;
    private int countryCode;
    private boolean isSelected = false;

    public Region() {
    }

    public Region(String regionName, String regionCode, int countryCode) {
        this.regionName = regionName;
        this.regionCode = regionCode;
        this.countryCode = countryCode;
    }

    /*
    * dung cho language code
    **/
    public Region(String regionCode, String regionName) {
        this.regionName = regionName;
        this.regionCode = regionCode;
    }


    public Region(String resString) {
        String[] data = resString.split(";");
        regionName = data[0];
        regionCode = data[1];
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" regionName: ").append(regionName);
        sb.append(" regionCode: ").append(regionCode);
        sb.append(" countryCode: ").append(countryCode);
        sb.append(" isSelected: ").append(isSelected);
        return sb.toString();
    }
}