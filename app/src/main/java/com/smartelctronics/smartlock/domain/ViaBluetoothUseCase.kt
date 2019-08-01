package com.smartelctronics.smartlock.domain

import android.util.Log
import androidx.fragment.app.Fragment
import com.polidea.rxandroidble2.RxBleDevice
import com.smartelctronics.smartlock.data.repository.bluetooth.BluetoothRepository
import com.smartelctronics.smartlock.utils.Constants
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import java.nio.charset.Charset
import java.util.*

class ViaBluetoothUseCase(fragment: Fragment) {

    private val connectionDisposable: CompositeDisposable = CompositeDisposable()
    private var scanDisposable: Disposable? = null
    private val bluetoothRepository = BluetoothRepository(fragment)

    fun initBluetooth() = bluetoothRepository.requestPermission()

    fun enableBluetooth() = bluetoothRepository.enableBluetooth()

    fun scan() = Single.create<RxBleDevice> { emitter ->
        bluetoothRepository.scan()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { scanDisposable = null }
            .filter { return@filter it.bleDevice.name.equals(Constants.DEVICE_NAME) }
            .take(1)
            .subscribe {
                emitter.onSuccess(it.bleDevice)
            }
            .let { scanDisposable = it }
    }

    fun connect(bleDevice: RxBleDevice) = Single.create<Boolean> { emitter ->
        bluetoothRepository.connect(bleDevice)
            .flatMapSingle { it.discoverServices() }
            .flatMapSingle { it.getCharacteristic(UUID.fromString(Constants.CHARACTERISTIC_TX)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    emitter.onSuccess(true)
                },
                { emitter.onError(it) },
                { Log.i(Constants.TAG, "Complete established!") }
            )
            .let { connectionDisposable.add(it) }
    }

    fun getLockState(): Single<Boolean> = writeCommand(4).flatMap { parseLockState(it) }

    fun setCommand(command: Boolean): Single<Boolean> = when (command) {
        false -> writeCommand(2)
        true -> writeCommand(1)
    }.flatMap { parseLockState(it) }

    private fun parseLockState(byteArray: ByteArray) = when {
        "1".toByteArray(Charset.defaultCharset()).contentEquals(byteArray) -> Single.just(false)
        "2".toByteArray(Charset.defaultCharset()).contentEquals(byteArray) -> Single.just(true)
        else -> Single.error<Boolean>(Exception("Can not parsing lock state"))
    }

    private fun writeCommand(command: Int): Single<ByteArray> =
        Single.zip(bluetoothRepository.notify().first("0".toByteArray()),
            bluetoothRepository.write(command.toString()), BiFunction { notifyData, commandData ->  notifyData})

    fun destroy() {
        connectionDisposable.dispose()
    }
}