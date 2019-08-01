package com.smartelctronics.smartlock.data.repository.remote.result

import com.smartelctronics.smartlock.data.entitiy.Results
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiResult {

    @GET("api/v1.6/variables/{id}/values")
    fun getResponse(@Header("X-Auth-Token") token: String,
                    @Path("id") id: String,
                    @Query("start") start: Long
    ): Single<Results>
}