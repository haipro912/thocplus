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

import android.app.Application;
import android.os.Build;
import android.text.TextUtils;

import com.vttm.mochaplus.feature.BuildConfig;
import com.vttm.mochaplus.feature.data.api.request.BaseRequest;
import com.vttm.mochaplus.feature.data.api.request.GenOtpRequest;
import com.vttm.mochaplus.feature.data.api.request.VideoDetailRequest;
import com.vttm.mochaplus.feature.data.api.request.VideoRelateRequest;
import com.vttm.mochaplus.feature.data.api.request.VideoRequest;
import com.vttm.mochaplus.feature.data.api.response.VideoCategoryResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoDetailResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;
import com.vttm.mochaplus.feature.data.api.restful.ApiCallback;
import com.vttm.mochaplus.feature.data.api.restful.WSRestful;
import com.vttm.mochaplus.feature.data.api.service.ApiService;
import com.vttm.mochaplus.feature.helper.HttpHelper;
import com.vttm.mochaplus.feature.utils.Config;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by janisharali on 28/01/17.
 */

@Singleton
public class AppApiHelper implements ApiHelper {

    private ApiHeader mApiHeader;
    private Application context;

    @Inject
    public AppApiHelper(Application context, ApiHeader apiHeader) {
        mApiHeader = apiHeader;
        this.context = context;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }

    private String getToken()
    {
        return "8229825071532685643600553";
    }

    @Override
    public void getVideoCategory(BaseRequest request, ApiCallback<VideoCategoryResponse> callBack) {

        final String timeStamp = System.currentTimeMillis() + "";
        String security = HttpHelper.encryptDataV2(context, request.getMsisdn() + request.getDomain() + getToken() + timeStamp, getToken());

        Map<String, String> data = new HashMap<>();
        data.put("revision", request.getRevision());
        data.put("domain", request.getDomain());
        data.put("timestamp", timeStamp);
        data.put("clientType", request.getClientType());
        data.put("msisdn", request.getMsisdn());
        data.put("vip", request.getVip());
        data.put("security", URLEncoder.encode(security));

        WSRestful restful = new WSRestful(context);
        ApiService apiClient = restful.getInstance();
        apiClient.getVideoCategory(data).enqueue(callBack);
    }

    @Override
    public void getVideoList(VideoRequest request, ApiCallback<VideoResponse> callBack) {

        final String timeStamp = System.currentTimeMillis() + "";
        String lastId =  TextUtils.isEmpty(request.getLastIdStr()) ? "" : request.getLastIdStr();

        String security = HttpHelper.encryptDataV2(context, request.getMsisdn() + request.getDomain() + request.getCategoryid() + request.getLimit() + request.getOffset() + lastId + getToken() + timeStamp, getToken());

        Map<String, String> data = new HashMap<>();
        data.put("revision", request.getRevision());
        data.put("domain", request.getDomain());
        data.put("timestamp", timeStamp);
        data.put("clientType", request.getClientType());
        data.put("msisdn", request.getMsisdn());
        data.put("vip", request.getVip());
        data.put("offset", request.getOffset() + "");
        data.put("limit", request.getLimit() + "");
        data.put("categoryid", request.getCategoryid() + "");
        data.put("lastIdStr", lastId);
        data.put("security", URLEncoder.encode(security));

        WSRestful restful = new WSRestful(context);
        ApiService apiClient = restful.getInstance();
        apiClient.getVideoList(data).enqueue(callBack);
    }

    @Override
    public void getVideoDetail(VideoDetailRequest request, ApiCallback<VideoDetailResponse> callBack) {
        final String timeStamp = System.currentTimeMillis() + "";
        String security = HttpHelper.encryptDataV2(context, request.getMsisdn() + request.getDomain() + request.getUrl() + getToken() + timeStamp, getToken());

        Map<String, String> data = new HashMap<>();
        data.put("revision", request.getRevision());
        data.put("domain", request.getDomain());
        data.put("timestamp", timeStamp);
        data.put("clientType", request.getClientType());
        data.put("msisdn", request.getMsisdn());
        data.put("vip", request.getVip());
        data.put("security", URLEncoder.encode(security));
        data.put("url", URLEncoder.encode(request.getUrl()));

        WSRestful restful = new WSRestful(context);
        ApiService apiClient = restful.getInstance();
        apiClient.getVideoDetail(data).enqueue(callBack);
    }

    @Override
    public void getVideoRelate(VideoRelateRequest request, ApiCallback<VideoResponse> callBack) {
        final String timeStamp = System.currentTimeMillis() + "";
        String lastId =  TextUtils.isEmpty(request.getLastIdStr()) ? "" : request.getLastIdStr();
        String security = HttpHelper.encryptDataV2(context, request.getMsisdn() + request.getDomain() + request.getQuery() + request.getLimit() + request.getOffset() + lastId + getToken() + timeStamp, getToken());

        Map<String, String> data = new HashMap<>();
        data.put("revision", request.getRevision());
        data.put("domain", request.getDomain());
        data.put("timestamp", timeStamp);
        data.put("clientType", request.getClientType());
        data.put("msisdn", request.getMsisdn());
        data.put("vip", request.getVip());
        data.put("q", URLEncoder.encode(request.getQuery()));
        data.put("offset", request.getOffset() + "");
        data.put("limit", request.getLimit() + "");
        data.put("lastIdStr", lastId);
        data.put("security", URLEncoder.encode(security));

        WSRestful restful = new WSRestful(context);
        ApiService apiClient = restful.getInstance();
        apiClient.getVideoRelate(data).enqueue(callBack);
    }

    @Override
    public void genOTP(GenOtpRequest request, ApiCallback<String> callBack) {
        Map<String, String> data = new HashMap<>();
        data.put("username", request.getUsername());
        data.put("countryCode", request.getCountryCode());
        data.put("platform", Config.CLIENT_TYPE);
        data.put("os_version", Build.VERSION.RELEASE);
        data.put("device", Build.MANUFACTURER + "-" + Build.BRAND + "-" + Build.MODEL);
        data.put("revision", Config.REVISION);
        data.put("version", BuildConfig.VERSION_NAME);

        WSRestful restful = new WSRestful(context);
        ApiService apiClient = restful.getInstance();
        apiClient.genOTP(data).enqueue(callBack);
    }
}

