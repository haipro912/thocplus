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

/**
 * Created by HaiKE on 01/02/17.
 */

public final class ApiEndPoint {

//    public static final String ENDPOINT_BASE_URL = "http://api.tinngan.vn/Tinngan.svc/";//BuildConfig.BASE_URL;
    public static final String ENDPOINT_BASE_URL_TEST = "http://125.235.16.131/Tinngan.svc/";
    public static final String ENDPOINT_BASE_URL = "http://api.tinngan.vn/Tinngan.svc/";
    public static final String ENDPOINT_KEENG_URL = "http://vip.service.keeng.vn:8080/KeengWSRestful/ws/";

    public static final String GET_SETTING = ENDPOINT_BASE_URL + "getSetting";
    public static final String GET_REG_DEVICE = ENDPOINT_BASE_URL + "setRegistration";
    public static final String GET_RENEW_REG_DEVICE = ENDPOINT_BASE_URL + "setRegistrationExpire";
    public static final String GET_NEWS_BY_CATEGORY = ENDPOINT_BASE_URL + "getCateList";
    public static final String GET_NEWS_CATEGORY = ENDPOINT_BASE_URL + "getCategory";
    public static final String GET_VIDEO_CATEGORY = ENDPOINT_BASE_URL + "getCategoryVideo";
    public static final String GET_VIDEO_BY_CATEGORY = ENDPOINT_BASE_URL + "getListVideo";
    public static final String GET_VIDEO_RELATE = ENDPOINT_BASE_URL + "getVideoRelate";
    public static final String GET_NEWS_HOME = ENDPOINT_BASE_URL + "getNewsHomev2";
    public static final String GET_NEWS_HOME_CATE = ENDPOINT_BASE_URL + "getHomeCate";
    public static final String GET_NEWS_CONTENT = ENDPOINT_BASE_URL + "getContent";
    public static final String GET_NEWS_RELATE = ENDPOINT_BASE_URL + "getListRelateReadNews";
    public static final String GET_NEWS_RELATE_FROM_HOME = ENDPOINT_BASE_URL + "getListCateRelate";
    public static final String GET_NEWS_RELATE_FROM_CATEGORY = ENDPOINT_BASE_URL + "getListCateRelateOld";
    public static final String GET_NEWS_RELATE_FROM_CATEGORY_POSTION_0 = ENDPOINT_BASE_URL + "getListCateRelateOldCM";
    public static final String GET_NEWS_RELATE_FROM_EVENT = ENDPOINT_BASE_URL + "getListCateRelateEvent";
    public static final String GET_RADIO_CATEGORY = ENDPOINT_BASE_URL + "getCategoryRadioNew";
    public static final String GET_RADIO_HOME = ENDPOINT_BASE_URL + "getRadioNew";
    public static final String GET_RADIO_BY_CATEGORY = ENDPOINT_BASE_URL + "getListRadio";
    public static final String GET_HOME_TOP_NOW = ENDPOINT_BASE_URL + "getHomeTopOfSource";
    public static final String GET_NEWS_BY_SOURCE = ENDPOINT_BASE_URL + "getListNewsOfSource";
    public static final String GET_SOURCE_LIST = ENDPOINT_BASE_URL + "getListSource";
    public static final String GET_DISCOVER_LIST = ENDPOINT_KEENG_URL + "common/getVideo4Mocha";
    public static final String GET_EVENT_LIST = ENDPOINT_BASE_URL + "getHomeEvent";
    public static final String GET_NEWS_BY_EVENT = ENDPOINT_BASE_URL + "getEventView";
    public static final String GET_COUNT_NEWS = ENDPOINT_BASE_URL + "getCountNew";
    public static final String GET_VIDEO_DETAIL = ENDPOINT_BASE_URL + "getVideoDetail";
    public static final String GET_RADIO_DETAIL = ENDPOINT_BASE_URL + "getRadioDetail";
    public static final String GET_HOT_NEWS = ENDPOINT_BASE_URL + "getMostView";
    public static final String AUTO_LOGIN = ENDPOINT_KEENG_URL + "auth/autoLogin";
    public static final String REGISTER_POINT = ENDPOINT_BASE_URL + "setRegister";
    public static final String LOG_PUSH = ENDPOINT_BASE_URL + "setLogAccepPush";
    public static final String GET_SEARCH = ENDPOINT_BASE_URL + "getSearch";
    public static final String GET_RADIO_STORY = ENDPOINT_BASE_URL + "getRadioStoryNew";
    public static final String GET_RADIO_STORY_DETAIL = ENDPOINT_BASE_URL + "getRadioStoryDetail";
    public static final String GET_MOST_NEWS = ENDPOINT_BASE_URL + "getMostView";
    public static final String GET_WC_BXH = ENDPOINT_BASE_URL + "worldcup/GetWorldCupScore";
    public static final String GET_WC_MATCH_SCHEDULE = ENDPOINT_BASE_URL + "worldcup/GetWorldCupSchedule";

    public static final String ADD_DEVICE_DRM = "http://203.190.170.158:8086/serviceapi/keeng/api/DRMdevice_ManagerWorldcup";
    public static final String GEN_LINK_DRM = "http://203.190.170.158:8086/serviceapi/keeng/api/getLinkWorldCup";

    private ApiEndPoint() {
        // This class is not publicly instantiable
    }

}
