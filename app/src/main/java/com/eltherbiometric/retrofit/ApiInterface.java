package com.eltherbiometric.retrofit;

import com.eltherbiometric.data.model.Response;
import com.eltherbiometric.data.model.Upload;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("upload")
    Call<Response> postJson(@Body Upload body);
}
