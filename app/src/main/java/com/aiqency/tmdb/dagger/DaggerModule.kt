package com.aiqency.tmdb.dagger

import android.content.Context
import com.aiqency.tmdb.activity.main.presenter.MainMPVInterface
import com.aiqency.tmdb.activity.main.presenter.MainPresenter
import com.aiqency.tmdb.activity.main.presenter.MovieRepositoryInterface
import com.aiqency.tmdb.activity.main.repo.MovieRepository
import com.aiqency.tmdb.rest.Rest
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open internal class RepoModule(private val context: Context) {

    @Provides @Singleton
    open fun app(): Context = context

    @Provides @Singleton
    open fun rest() = Rest.getInstance()

    @Provides
    open fun repo(rest: Rest): MovieRepositoryInterface = MovieRepository(rest)

    @Provides
    open fun mainPresenter(repo: MovieRepositoryInterface): MainMPVInterface.Presenter = MainPresenter(repo)

}