package com.aiqency.tmdb.activity.main.repo.parser

import androidx.annotation.StringRes
import com.aiqency.tmdb.R
import com.aiqency.tmdb.activity.main.repo.parser.MoviesParser.MoviesType
import com.aiqency.tmdb.realms.MoviesResponse
import okhttp3.ResponseBody


/**
 * Responsible for parsing and join the MoviesResponse response bodies
 * @param responses the [MoviesResponse] fetched from the remote datasource. vararg should be ordered by [MoviesType] ordinal
 */
class MoviesParser(
   private vararg val responses: MoviesResponse
) {

    val successfulResponsesBodies by lazy { responses.filter { it.isSuccessful }.mapNotNull { it.body() } }


    /**
     * Reformat all the input calls
     */
    init {
        responses.forEachIndexed { i, r -> r.format(MoviesType.values()[i]) }
    }

    /**
     * @return Error code and response body of the unsuccessful calls
     */
    fun getErrors(): List<Pair<Int, ResponseBody?>>? =
        if (responses.size > successfulResponsesBodies.size)
            responses.filter { !it.isSuccessful }.map { Pair( it.code(), it.errorBody()) }
        else null


    /**
     * Responsible in formatting the response body to include required metadata
     */
    private fun MoviesResponse.format(mType: MoviesType) {
        body()?.apply {
            type = mType.name
            lastUpdate = System.currentTimeMillis()
            results?.forEach { it.belongToPage = page }
        }
    }

    private enum class MoviesType(@StringRes var res: Int) {
        TOP_RATED(R.string.top_rated), UPCOMING(R.string.upcoming), POPULAR(R.string.popular)
    }

}


