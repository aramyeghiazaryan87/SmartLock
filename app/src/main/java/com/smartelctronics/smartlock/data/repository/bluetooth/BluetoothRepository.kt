package com.smartelctronics.smartlock.data.repository.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.Fragment
import com.jakewharton.rx.ReplayingShare
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanSettings
import com.smartelctronics.smartlock.data.entitiy.Results
import com.smartelctronics.smartlock.data.repository.BaseRepository
import com.smartelctronics.smartlock.utils.Constants
import io.reactivex.CompletableEmitter
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Response
import java.nio.charset.Charset
import java.util.*


class BluetoothRepository(private val fragment: Fragment) : BaseRepository {

    private val rxBleClient: RxBleClient by lazy {
        RxBleClient.create(fragment.activity?.application!!)
    }

    private lateinit var bleDevice: RxBleDevice

    private val connectionObservable: Observable<RxBleConnection> by lazy {
        bleDevice.establishConnection(false).compose(ReplayingShare.instance())
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fragment.activity?.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                enableBluetooth()
            } else {
                fragment.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    Constants.PERMISSION_REQUEST_COARSE_LOCATION
                )
            }
        } else {
            enableBluetooth()
        }
//        fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
//            Constants.PERMISSION_REQUEST_COARSE_LOCATION
//        )
    }

    fun enableBluetooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        fragment.startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT)
    }


    fun observeState(): Observable<RxBleClient.State> {
        return rxBleClient.observeStateChanges()
    }

    fun scan() = rxBleClient
        .scanBleDevices(
            ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build()
        )

    fun connect(bleDevice: RxBleDevice): Observable<RxBleConnection> {
        this.bleDevice = bleDevice
        return connectionObservable
    }

    fun write(data: String) =
        connectionObservable
            .firstOrError()
            .flatMap {
                it.writeCharacteristic(
                    UUID.fromString(Constants.CHARACTERISTIC_TX),
                    data.toByteArray(Charset.defaultCharset())
                )
            }

    fun notify(): Observable<ByteArray> = connectionObservable
        .flatMap { it.setupNotification(UUID.fromString(Constants.CHARACTERISTIC_TX)) }
        .flatMap { it }


    override fun getResponse(): Single<Results> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCommand(command: Int): Single<Response<Void>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}