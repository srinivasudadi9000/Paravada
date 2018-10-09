package com.example.adimn.myapplication;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.example.adimn.myapplication.JSONParser.POST;

interface WebService {


    // adding product

    @Multipart
    @POST("addproduct.php")
    Call<ResponseBody> addProduct(
            @Part("farmerID") RequestBody farmerID,
            @Part MultipartBody.Part productPic,
            @Part("categoryid") RequestBody cat_id,
            @Part("subcategoryid") RequestBody subcat_id,
            @Part("quantity") RequestBody quantity,
            @Part("units") RequestBody units,
            @Part("price") RequestBody price,
            @Part("description")RequestBody description
    );


    //forgot password

    @Multipart
    @POST("forgotpassword.php")
    Call<ResponseBody> forgotpassword(
            @Part("mobile") RequestBody mobile,
            @Part("request_id") RequestBody request_id

    );


}