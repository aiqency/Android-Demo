package com.aiqency.tmdb.realms

import retrofit2.Response

typealias SimpleListener = () -> Unit
typealias GenericListener<T> =  (T) -> Unit
typealias MoviesResponse = Response<MoviesRealm>
typealias GenericResponseListener<T> = () -> T