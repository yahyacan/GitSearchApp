package com.gitsearchapp.service;

import com.gitsearchapp.model.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRepo {

    @GET("users/{user}/repos")
    Call<List<Repo>> findRepoByUserID(@Path("user") String user, @Query("type") String type, @Query("page") int page,
                               @Query("per_page") int perPage);
}