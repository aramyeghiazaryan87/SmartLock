package com.smartelctronics.smartlock.data.repository.remote.common

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RemoteClient {

    companion object {

        fun get(): Retrofit {
            return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://things.ubidots.com:443/")
                .build()
        }
    }
}