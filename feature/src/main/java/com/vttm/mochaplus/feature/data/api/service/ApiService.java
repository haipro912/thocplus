package com.vttm.mochaplus.feature.data.api.service;

import com.vttm.mochaplus.feature.data.api.response.VideoCategoryResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET(ApiEndPoint.GET_VIDEO_CATEGORY)
    Call<VideoCategoryResponse> getVideoCategory(@QueryMap(encoded=true) Map<String, String> options);

    @GET(ApiEndPoint.GET_VIDEO_LIST)
    Call<VideoResponse> getVideoList(@QueryMap(encoded=true) Map<String, String> options);
}