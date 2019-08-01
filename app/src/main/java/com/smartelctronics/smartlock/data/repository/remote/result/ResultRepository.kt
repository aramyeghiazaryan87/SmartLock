package com.smartelctronics.smartlock.data.repository.remote.result

import com.smartelctronics.smartlock.data.entitiy.Results
import com.smartelctronics.smartlock.utils.Constants
import io.reactivex.Single

class ResultRepository(private val apiResult: ApiResult) {

    fun get(id: Long) : Single<Results> = apiResult.getResponse(Constants.TOKEN, Constants.RESULT_ID, id)
}