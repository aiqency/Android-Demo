package com.aiqency.tmdb

import android.app.Application
import com.aiqency.tmdb.dagger.AppComponent
import com.aiqency.tmdb.dagger.DaggerAppComponent
import com.aiqency.tmdb.dagger.RepoModule
import io.realm.Realm

open class App: Application() {

    private lateinit var dagger: AppComponent

    fun getDagger(): AppComponent = dagger

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        dagger = initDaggerAppComponent()
    }

    open fun initDaggerAppComponent(): AppComponent =
        DaggerAppComponent.builder()
            .repoModule(RepoModule(this))
            .build()
}