package com.aiqency.tmdb.dagger

import com.aiqency.tmdb.activity.main.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepoModule::class])
interface AppComponent {
    fun inject(application: MainActivity)
}