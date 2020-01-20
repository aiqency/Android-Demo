package com.aiqency.tmdb.activity.main.repo.caches

import com.aiqency.tmdb.realms.MovieRealm
import com.aiqency.tmdb.realms.MoviesRealm
import com.aiqency.tmdb.realms.realmGetFirst
import com.aiqency.tmdb.realms.realmGetList
import io.realm.Realm
import io.realm.RealmResults

class CacheMovies: CacheInterface {

    private val realm by lazy { Realm.getDefaultInstance() }

    override fun getAll(): RealmResults<MovieRealm> {
        val (_, result) = realmGetList(MovieRealm::class.java, realm)
        return result
    }

    override fun isPageCached(page: Int): Boolean {
        val (_, cache) = realmGetFirst(MoviesRealm::class.java, realm) { equalTo("page", page)}
        return cache != null
    }

    override fun close() = realm?.close()
}