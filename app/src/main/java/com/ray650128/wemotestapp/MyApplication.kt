package com.ray650128.wemotestapp

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initRealmDatabase()
    }


    private fun initRealmDatabase() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("WeMoAppTest.realm")
            .compactOnLaunch()
            .build()

        Realm.setDefaultConfiguration(config)
    }
}