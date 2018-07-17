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

import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.vttm.mochaplus.feature.data.api.request.NewsContentRequest;
import com.vttm.mochaplus.feature.data.api.request.NewsRequest;
import com.vttm.mochaplus.feature.data.api.response.NewsContentResponse;
import com.vttm.mochaplus.feature.data.api.response.NewsResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by janisharali on 28/01/17.
 */

@Singleton
public class AppApiHelper implements ApiHelper {

    private ApiHeader mApiHeader;

    @Inject
    public AppApiHelper(ApiHeader apiHeader) {
        mApiHeader = apiHeader;
    }

    @Override
    public ApiHeader getApiHeader() {
        android.util.Log.d("AppApiHelper", "getApiHeader -  duyhuongggg: ");
        return mApiHeader;
    }

    @Override
    public Observable<NewsResponse> getNewsByCategory(NewsRequest request) {
        android.util.Log.d("AppApiHelper", "getNewsByCategory -  duyhuongggg: request= " + request);
        String url;
        if (request.getCateId() == 1602) {
            //world cup
            //api: http://api.tinngan.vn/Tinngan.svc/getEventView/1602/1/20
            url = ApiEndPoint.GET_NEWS_BY_EVENT + "/" + request.getCateId() + "/" + request.getPage() + "/" + request.getNum();
        } else if (request.getCateId() == 3000) {
            // hot news
            url = ApiEndPoint.GET_HOT_NEWS + "/" + 0 + "/" + request.getPage() + "/" + request.getNum() +"/" + request.getUnixTime();
        } else {
            url = ApiEndPoint.GET_NEWS_BY_CATEGORY + "/" + request.getCateId() + "/" + request.getPage() + "/" + request.getNum() + "/" + request.getUnixTime();
        }

        return Rx2AndroidNetworking.get(url)
                .addHeaders(mApiHeader.getProtectedApiHeader())
                .build()
                .getObjectObservable(NewsResponse.class);
    }


    @Override
    public Observable<NewsContentResponse> getNewsContent(NewsContentRequest request) {
        return Rx2AndroidNetworking.get(ApiEndPoint.GET_NEWS_CONTENT + "/" + request.getPid() + "/" + request.getCateId() + "/" + request.getContentId())
                .addHeaders(mApiHeader.getProtectedApiHeader())
                .build()
                .getObjectObservable(NewsContentResponse.class);
    }

}

