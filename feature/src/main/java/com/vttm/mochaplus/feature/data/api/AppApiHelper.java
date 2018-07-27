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

import com.vttm.mochaplus.feature.data.api.request.BaseRequest;
import com.vttm.mochaplus.feature.data.api.request.VideoRequest;
import com.vttm.mochaplus.feature.data.api.response.VideoCategoryResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;
import com.vttm.mochaplus.feature.data.api.restful.ApiCallback;
import com.vttm.mochaplus.feature.data.api.restful.WSRestful;
import com.vttm.mochaplus.feature.data.api.service.ApiService;
import com.vttm.mochaplus.feature.helper.HttpHelper;

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
        String security = HttpHelper.encryptDataV2(context, request.getMsisdn() + request.getDomain() + request.getCategoryid() + request.getLimit() + request.getOffset() + request.getLastIdStr() + getToken() + timeStamp, getToken());

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
        data.put("lastIdStr", request.getLastIdStr());
        data.put("security", URLEncoder.encode(security));

        WSRestful restful = new WSRestful(context);
        ApiService apiClient = restful.getInstance();
        apiClient.getVideoList(data).enqueue(callBack);
    }
}

