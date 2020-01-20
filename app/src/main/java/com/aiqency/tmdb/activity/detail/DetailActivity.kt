package com.aiqency.tmdb.activity.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aiqency.tmdb.BuildConfig
import com.aiqency.tmdb.R
import com.aiqency.tmdb.realms.MovieRealm
import com.aiqency.tmdb.realms.loadAsync
import com.aiqency.tmdb.realms.realmGetFirst
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_scrolling.*

class DetailActivity : AppCompatActivity() {

    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportPostponeEnterTransition()
        val (r, movie) = realmGetFirst(MovieRealm::class.java) { equalTo("id", intent.getIntExtra("id", -1)) }
        realm = r
        if (movie != null) {
            img.loadAsync("${BuildConfig.IMG_URL}${movie.poster_path!!}") {
                supportStartPostponedEnterTransition()
            }

            img2.loadAsync("${BuildConfig.IMG_URL}${movie.backdrop_path!!}")

            m_title.text = movie.title
            content.text = movie.overview
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

}
