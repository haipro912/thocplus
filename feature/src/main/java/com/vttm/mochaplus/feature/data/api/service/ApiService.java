package com.vttm.mochaplus.feature.data.api.service;

import com.vttm.mochaplus.feature.data.api.response.VideoCategoryResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoDetailResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET(ApiEndPoint.GET_VIDEO_CATEGORY)
    Call<VideoCategoryResponse> getVideoCategory(@QueryMap(encoded=true) Map<String, String> options);

    @GET(ApiEndPoint.GET_VIDEO_LIST)
    Call<VideoResponse> getVideoList(@QueryMap(encoded=true) Map<String, String> options);

    @GET(ApiEndPoint.GET_VIDEO_DETAIL)
    Call<VideoDetailResponse> getVideoDetail(@QueryMap(encoded=true) Map<String, String> options);

    @GET(ApiEndPoint.GET_VIDEO_RELATE)
    Call<VideoResponse> getVideoRelate(@QueryMap(encoded=true) Map<String, String> options);


    @FormUrlEncoded
    @POST(ApiEndPoint.GEN_OTP)
    Call<String> genOTP(@FieldMap(encoded=true) Map<String, String> options);
}
