package com.aiqency.tmdb.app

import android.app.Application
import com.aiqency.tmdb.App
import com.aiqency.tmdb.activity.main.presenter.MovieRepositoryInterface
import com.aiqency.tmdb.dagger.AppComponent
import com.aiqency.tmdb.dagger.DaggerAppComponent
import com.aiqency.tmdb.dagger.RepoModule
import com.aiqency.tmdb.dummyData.dummyData
import com.aiqency.tmdb.realms.MovieRealm
import com.aiqency.tmdb.realms.realmGetList
import com.aiqency.tmdb.realms.realmPersist
import com.aiqency.tmdb.rest.Rest
import org.mockito.Mockito
import org.mockito.Mockito.`when`

/**
 * Override Dagger repo module to mock cache return values
 */
class TestApp : App(){

    override fun initDaggerAppComponent(): AppComponent =
        DaggerAppComponent.builder()
            .repoModule(MockRepoModule(this))
            .build()

    private inner class MockRepoModule internal constructor(application: Application) : RepoModule(application) {

        override fun repo(rest: Rest): MovieRepositoryInterface {
            realmPersist(dummyData())
            val (_, result) = realmGetList(MovieRealm::class.java)

            val mock = Mockito.mock(MovieRepositoryInterface::class.java)
            `when`(mock!!.getLiveMovies()).thenReturn(result)
            return mock
        }

    }

}