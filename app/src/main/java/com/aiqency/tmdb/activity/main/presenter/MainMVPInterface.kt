package com.aiqency.tmdb.activity.main.presenter

import com.aiqency.tmdb.realms.MovieRealm
import com.aiqency.tmdb.realms.SimpleListener
import io.realm.OrderedRealmCollection

interface MainMPVInterface{

    interface View {
        fun onFetchMoviesCallStart(page: Int = 1)
        fun onFetchMoviesCallFinished()
    }

    interface Presenter: MovieRepositoryInterface {
        var view: View?
        var repo: MovieRepositoryInterface
    }

}

interface MovieRepositoryInterface {

    fun getLiveMovies(): OrderedRealmCollection<MovieRealm>

    fun updateLiveMoviesList(
        page: Int = 1,
        onRemoteFetchStart: SimpleListener? = null,
        onRemoteFetchFinished: SimpleListener? = null)

    fun cleanResources()

}