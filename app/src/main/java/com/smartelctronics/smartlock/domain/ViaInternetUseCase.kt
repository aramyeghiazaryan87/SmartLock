package com.smartelctronics.smartlock.domain

import android.util.Log
import com.smartelctronics.smartlock.data.common.LockCommand
import com.smartelctronics.smartlock.data.repository.remote.RemoteRepository
import com.smartelctronics.smartlock.utils.Constants
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class ViaInternetUseCase {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val remoteRepository: RemoteRepository = RemoteRepository()

    fun getLockState(): Single<Boolean> = request(LockCommand.STATE, Constants.DEFAULT_REPEAT_COUNT)

    fun setCommand(command: Boolean): Single<Boolean> = when (command) {
        false -> request(2, Constants.DEFAULT_REPEAT_COUNT)
        true -> request(1, Constants.DEFAULT_REPEAT_COUNT)
    }


    private fun request(command: Int, repeatCount: Long): Single<Boolean> =
        Single.create { emitter ->
            compositeDisposable.add(
                remoteRepository.setCommand(command)
                    .subscribe({
                        compositeDisposable.add(remoteRepository.getResponse()
                            .flatMap {
                                Log.i(Constants.TAG, "Results size: " + it.results.size)
                                return@flatMap if (it.results.isEmpty()) Single.error<Long>(Exception())
                                else Single.just(it.results[0].value)
                            }
                            .flatMap {
                                return@flatMap when (it) {
                                    1L -> Single.just(false)
                                    2L -> Single.just(true)
                                    else -> Single.error<Boolean>(Exception("Can not parsing lock state"))
                                }
                            }
                            .retryWhen { it.take(repeatCount).delay(1, TimeUnit.SECONDS) }
                            .subscribe(
                                { result -> emitter.onSuccess(result) },
                                { error -> emitter.onError(error) })
                        )
                    },
                        { error ->
                            emitter.onError(error)
                        })
            )
        }


    fun destroy() = compositeDisposable.dispose()
}