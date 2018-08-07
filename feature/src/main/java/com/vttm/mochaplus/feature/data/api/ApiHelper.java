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


import com.vttm.mochaplus.feature.data.api.request.BaseRequest;
import com.vttm.mochaplus.feature.data.api.request.VideoDetailRequest;
import com.vttm.mochaplus.feature.data.api.request.VideoRelateRequest;
import com.vttm.mochaplus.feature.data.api.request.VideoRequest;
import com.vttm.mochaplus.feature.data.api.response.VideoCategoryResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoDetailResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;
import com.vttm.mochaplus.feature.data.api.restful.ApiCallback;

/**
 * Created by janisharali on 27/01/17.
 */

public interface ApiHelper {

    ApiHeader getApiHeader();

    void getVideoCategory(BaseRequest request, ApiCallback<VideoCategoryResponse> callBack);
    void getVideoList(VideoRequest request, ApiCallback<VideoResponse> callBack);
    void getVideoDetail(VideoDetailRequest request, ApiCallback<VideoDetailResponse> callBack);
    void getVideoRelate(VideoRelateRequest request, ApiCallback<VideoResponse> callBack);
}
