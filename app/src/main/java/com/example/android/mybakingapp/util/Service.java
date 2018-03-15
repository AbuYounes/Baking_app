package com.example.android.mybakingapp.util;


import com.example.android.mybakingapp.data.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getDetails();
}
