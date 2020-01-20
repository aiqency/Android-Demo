package com.aiqency.tmdb.activity.main.repo.caches

import com.aiqency.tmdb.realms.MovieRealm
import io.realm.OrderedRealmCollection

interface CacheInterface {
    fun getAll(): OrderedRealmCollection<MovieRealm>
    fun isPageCached(page: Int): Boolean
    fun close(): Unit?
}