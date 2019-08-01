package com.smartelctronics.smartlock.data.repository

import com.smartelctronics.smartlock.data.entitiy.Results
import io.reactivex.Single
import retrofit2.Response

interface BaseRepository {
    fun getResponse(): Single<Results>
    fun setCommand(command: Int): Single<Response<Void>>

}