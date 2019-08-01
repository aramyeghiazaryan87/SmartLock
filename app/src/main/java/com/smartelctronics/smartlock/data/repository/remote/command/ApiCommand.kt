package com.smartelctronics.smartlock.data.repository.remote.command

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiCommand {

    @POST("api/v1.6/variables/{id}/values")
    fun setCommand(
        @Header("X-Auth-Token") token: String,
        @Path("id") id: String,
        @Body value: Map<String, Int>
    ): Single<Response<Void>>
}