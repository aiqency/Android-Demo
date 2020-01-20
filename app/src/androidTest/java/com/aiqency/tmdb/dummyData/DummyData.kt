package com.aiqency.tmdb.dummyData

import com.aiqency.tmdb.realms.MovieRealm

fun dummyData(): List<MovieRealm> =
    (0..10).map {
        val picturePath = "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg"
        MovieRealm().apply {
            id = it
            adult = false
            backdrop_path = ""
            original_language = "EN"
            original_title = "Dummy movie"
            overview = picturePath
            popularity = 0.0
            poster_path = picturePath
            release_date = ""
            title = "Dummy movie"
            video = true
            vote_average = 0.0
            vote_count = 0

            belongToPage = 1
        }
    }