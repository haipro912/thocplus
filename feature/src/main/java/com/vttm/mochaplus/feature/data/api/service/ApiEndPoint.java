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

package com.vttm.mochaplus.feature.data.api.service;

/**
 * Created by HaiKE on 01/02/17.
 */

public final class ApiEndPoint {

//    public static final String ENDPOINT_BASE_URL = "http://api.tinngan.vn/Tinngan.svc/";//BuildConfig.BASE_URL;
    public static final String ENDPOINT_BASE_URL = "http://hlvip.mocha.com.vn/";
    public static final String ENDPOINT_KEENG_URL = "http://vip.service.keeng.vn:8080/KeengWSRestful/ws/";

    public static final String GET_VIDEO_CATEGORY = "onMediaBackendBiz/onmedia/video/getCategory";
    public static final String GET_VIDEO_LIST = "onMediaBackendBiz/onmedia/video/getListVideoByCate/v1";
    public static final String GET_VIDEO_DETAIL = "onMediaBackendBiz/onmedia/video/getVideoDetail";
    public static final String GET_VIDEO_RELATE = "onMediaBackendBiz/onmedia/video/search/v1";

    public static final String GEN_OTP = "ReengBackendBiz/genotp/v23";


    private ApiEndPoint() {
        // This class is not publicly instantiable
    }

}
