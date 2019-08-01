package com.smartelctronics.smartlock.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import com.smartelctronics.smartlock.R
import com.smartelctronics.smartlock.data.repository.remote.common.RemoteClient
import com.smartelctronics.smartlock.data.repository.remote.result.ApiResult
import com.smartelctronics.smartlock.data.repository.remote.result.ResultRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host = NavHostFragment.create(R.navigation.nav_graph)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, host)
            .setPrimaryNavigationFragment(host).commit()
    }
}
