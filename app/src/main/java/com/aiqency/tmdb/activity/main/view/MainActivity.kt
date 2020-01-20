package com.aiqency.tmdb.activity.main.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.aiqency.tmdb.App
import com.aiqency.tmdb.R
import com.aiqency.tmdb.activity.detail.DetailActivity
import com.aiqency.tmdb.activity.main.presenter.MainMPVInterface
import com.aiqency.tmdb.activity.main.view.adapters.EndlessScrollListener
import com.aiqency.tmdb.activity.main.view.adapters.ItemOffsetDecoration
import com.aiqency.tmdb.activity.main.view.adapters.MoviesAdapter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainMPVInterface.View {

    @Inject
    lateinit var presenter: MainMPVInterface.Presenter

    private val rv by lazy { findViewById<RecyclerView>(R.id.rv) }
    private val progressBar by lazy { findViewById<View>(R.id.progressBar) }
    private val scrollListener: EndlessScrollListener by lazy { EndlessScrollListener { refreshMoviesAt(it + 1) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).getDagger().inject(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar.apply { title = "" })

        presenter.view = this
        refreshMoviesAt()
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
        rv?.apply {
            itemAnimator = null
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = MoviesAdapter(presenter.getLiveMovies()) { (id, img) -> onMovieCLicked(id, img) }
            addItemDecoration(ItemOffsetDecoration(4))
            addOnScrollListener(scrollListener)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun refreshMoviesAt(page: Int = 1) {
        presenter.updateLiveMoviesList(page)
    }

    override fun onFetchMoviesCallStart(page: Int) {
        if (page == 1) showProgress(true)
    }

    override fun onFetchMoviesCallFinished() {
        showProgress(false)
        scrollListener.notifyMorePages()
    }

    private fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun onMovieCLicked(id: Int, img: ImageView) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, img, img.transitionName).toBundle()
        startActivity(Intent(this, DetailActivity::class.java).apply { putExtra("id", id) }, options)
    }

    override fun onDestroy() {
        super.onDestroy()
        rv.removeOnScrollListener(scrollListener)
        presenter.cleanResources()
    }

}
