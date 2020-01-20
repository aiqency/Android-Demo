package com.aiqency.tmdb.realms

import io.realm.RealmList
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class MoviesRealm: RealmModel {
    @PrimaryKey
    var type: String? = null
    var lastUpdate: Long? = null
    var page: Int? = null
    var results: RealmList<MovieRealm>? = null
    var total_pages: Int? = null
    var total_results: Int? = null
}


@RealmClass
open class MovieRealm: RealmModel {
    @PrimaryKey
    var id: Int? = null
    var adult: Boolean? = null
    var backdrop_path: String? = null
    var original_language: String? = null
    var original_title: String? = null
    var overview: String? = null
    var popularity: Double? = null
    var poster_path: String? = null
    var release_date: String? = null
    var title: String? = null
    var video: Boolean? = null
    var vote_average: Double? = null
    var vote_count: Int? = null

    var belongToPage: Int? = null
}