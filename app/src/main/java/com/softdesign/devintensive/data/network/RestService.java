package com.softdesign.devintensive.data.network;

import com.softdesign.devintensive.data.network.req.UserModelReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by shmakova on 09.07.16.
 */
public interface RestService {
    @POST("login")
    Call<UserModelRes> loginUser (@Body UserModelReq req);
}
