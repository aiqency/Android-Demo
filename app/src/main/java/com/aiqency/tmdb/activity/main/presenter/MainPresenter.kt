package com.aiqency.tmdb.activity.main.presenter

import com.aiqency.tmdb.realms.SimpleListener

/**
 * The concrete declaration of the [MainMPVInterface.Presenter]
 * required by the [com.aiqency.tmdb.activity.main.view.MainActivity] in order to display the list of movies.
 */
class MainPresenter(
    override var repo: MovieRepositoryInterface
): MainMPVInterface.Presenter {

    override var view: MainMPVInterface.View? = null

    override fun getLiveMovies() = repo.getLiveMovies()

    override fun updateLiveMoviesList(
        page: Int,
        onRemoteFetchStart: SimpleListener?,
        onRemoteFetchFinished: SimpleListener?
    ) = repo.updateLiveMoviesList(page, { view?.onFetchMoviesCallStart(page) }){
        view?.onFetchMoviesCallFinished()
    }

    override fun cleanResources() = repo.cleanResources()

}
