/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.vttm.mochaplus.feature.data.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vttm.mochaplus.feature.di.ApiInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by janisharali on 27/01/17.
 */

@Singleton
public class ApiHeader {

    private ProtectedApiHeader mProtectedApiHeader;
    private PublicApiHeader mPublicApiHeader;

//    @Inject
//    public ApiHeader(PublicApiHeader publicApiHeader, ProtectedApiHeader protectedApiHeader) {
//        mPublicApiHeader = publicApiHeader;
//        mProtectedApiHeader = protectedApiHeader;
//    }

    @Inject
    public ApiHeader() {

    }

    public ProtectedApiHeader getProtectedApiHeader() {
        return mProtectedApiHeader;
    }

    public PublicApiHeader getPublicApiHeader() {
        return mPublicApiHeader;
    }

    public static final class PublicApiHeader {

        @Expose
        @SerializedName("api_key")
        private String mApiKey;

        @Inject
        public PublicApiHeader(@ApiInfo String apiKey) {
            mApiKey = apiKey;
        }

        public String getApiKey() {
            return mApiKey;
        }

        public void setApiKey(String apiKey) {
            mApiKey = apiKey;
        }
    }

    public static final class ProtectedApiHeader {

        @Expose
        @SerializedName("api_key")
        private String mApiKey;

        @Expose
        @SerializedName("user")
        private String user;

        @Expose
        @SerializedName("password")
        private String password;

        @Expose
        @SerializedName("version")
        private String version;

        @Expose
        @SerializedName("device")
        private String device;

        @Expose
        @SerializedName("imei")
        private String imei;

        @Expose
        @SerializedName("msisdn")
        private String msisdn;

        @Expose
        @SerializedName("uuid")
        private String uuid;

        @Inject
        public ProtectedApiHeader(String mApiKey, String user, String password, String version, String device, String deviceId, String msisdn, String uuid) {
            this.mApiKey = mApiKey;
            this.user = user;
            this.password = password;
            this.version = version;
            this.device = device;
            this.imei = deviceId;
            this.msisdn = msisdn;
            this.uuid = uuid;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String apiKey) {
            version = apiKey;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
          this.imei = imei;
        }

        public String getApiKey() {
            return mApiKey;
        }

        public void setApiKey(String apiKey) {
            mApiKey = apiKey;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
