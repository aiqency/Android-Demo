package com.aiqency.tmdb.activity.main.repo

import android.util.Log
import com.aiqency.tmdb.BuildConfig
import com.aiqency.tmdb.activity.main.presenter.MovieRepositoryInterface
import com.aiqency.tmdb.activity.main.repo.caches.CacheInterface
import com.aiqency.tmdb.activity.main.repo.caches.CacheMovies
import com.aiqency.tmdb.activity.main.repo.parser.MoviesParser
import com.aiqency.tmdb.realms.SimpleListener
import com.aiqency.tmdb.realms.realmPersist
import com.aiqency.tmdb.rest.Rest
import io.realm.OrderedRealmCollection
import kotlinx.coroutines.*


open class MovieRepository(
    private val rest: Rest,
    val cache: CacheInterface = CacheMovies()
) : MovieRepositoryInterface {

    private val TAG = this::class.java.simpleName

    /**
     * Gets movies from cache, local data source (Realm) or remote data source, whichever is available first.
     */
    override fun updateLiveMoviesList(
        page: Int,
        onRemoteFetchStart: SimpleListener?,
        onRemoteFetchFinished: SimpleListener?
    ) {
        val pageIsInCache = cache.isPageCached(page)
        if (!pageIsInCache) {
            onRemoteFetchStart?.invoke()

            CoroutineScope(Dispatchers.IO).launch {
                val topRated = async { rest.getTopRatedMovies(page = page) }
                val popular = async { rest.getPopularMovies(page = page) }
                val upcoming = async { rest.getUpcomingMovies(page = page) }

                val result = MoviesParser(topRated.await(), popular.await(), upcoming.await())

                persistRequestedMovies(result)

                // Notify that the fetching complete
                withContext(Dispatchers.Main) { onRemoteFetchFinished?.invoke() }
            }
        }
    }

    /**
     * @return a live data [OrderedRealmCollection] of Movies object
     */
    override fun getLiveMovies() = cache.getAll()

    /**
     * Persisting the movies will update the liveData returned by [getLiveMovies] method
     */
    private fun persistRequestedMovies(response: MoviesParser) {
        // Print the error responses in case of debug build
        if (BuildConfig.DEBUG) response.getErrors()?.forEach {
            Log.e(TAG, "response.getErrors: code: ${it.first} message: ${it.second?.string()}")
        }

        realmPersist(response.successfulResponsesBodies)
    }

    override fun cleanResources() {
        cache.close()
    }

}


