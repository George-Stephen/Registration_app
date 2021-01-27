package com.technote.services;

import com.technote.services.models.Member;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface EnrollInterface {

    @GET("api/customers")
    Call<List<Member>> search_member(@Query("account_number") String account_number );


    @Multipart
    @POST("api/customer/new")
    Call<Member> new_member(@Part MultipartBody.Part member_image,@Part("id_type") RequestBody id_type,@Part("id_number") RequestBody id_number,@Part("title") RequestBody title,@Part("first_name")
            RequestBody first_name,@Part("middle_name") RequestBody middle_name,@Part("surname") RequestBody last_name,@Part("gender") RequestBody gender,@Part("birthday") RequestBody birthday
            ,@Part("email_address") RequestBody email_address,@Part("phone_number") RequestBody phone_number,
                            @Part("added_by") RequestBody added_by
            ,@Part("physical_address") RequestBody address,@Part MultipartBody.Part front_image
            ,@Part MultipartBody.Part back_image,@Part MultipartBody.Part sign_image);
}
