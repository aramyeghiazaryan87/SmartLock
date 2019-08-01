package com.smartelctronics.smartlock.data.repository.remote


import com.smartelctronics.smartlock.data.entitiy.Results
import com.smartelctronics.smartlock.data.repository.BaseRepository
import com.smartelctronics.smartlock.data.repository.remote.command.ApiCommand
import com.smartelctronics.smartlock.data.repository.remote.common.RemoteClient
import com.smartelctronics.smartlock.data.repository.remote.result.ApiResult
import com.smartelctronics.smartlock.utils.Constants
import io.reactivex.Single
import retrofit2.Response

class RemoteRepository : BaseRepository {

    override fun setCommand(command: Int): Single<Response<Void>> = RemoteClient.get().create(ApiCommand::class.java)
        .setCommand(Constants.TOKEN, Constants.COMMAND_ID, mapOf("value" to command))

    override fun getResponse(): Single<Results> = RemoteClient.get().create(ApiResult::class.java)
        .getResponse(Constants.TOKEN, Constants.RESULT_ID, System.currentTimeMillis())
}