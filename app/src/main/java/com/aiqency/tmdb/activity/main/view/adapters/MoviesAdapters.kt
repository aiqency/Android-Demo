package com.aiqency.tmdb.activity.main.view.adapters

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.aiqency.tmdb.BuildConfig
import com.aiqency.tmdb.realms.GenericListener
import com.aiqency.tmdb.realms.MovieRealm
import com.aiqency.tmdb.realms.loadAsync
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.cards.view.*


class MoviesAdapter(
    private val movies: OrderedRealmCollection<MovieRealm>,
    private val listener: GenericListener<Pair<Int, ImageView>>
) : RealmRecyclerViewAdapter<MovieRealm, MoviesAdapter.ViewHolder>(movies, true) {

    override fun getItemCount(): Int = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(com.aiqency.tmdb.R.layout.cards, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = movies[position]
        with(holder) {
            title.text = item?.title
            vote.rating = item?.vote_average?.toFloat()?.div(2f) ?: 0f
            itemView.setOnClickListener { listener.invoke(Pair(item.id!!, img)) }
            img.loadAsync("${BuildConfig.IMG_URL}${item!!.poster_path}")
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.title as TextView
        val vote = view.vote as RatingBar
        val img = view.img as ImageView
    }
}

class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

    constructor(@NonNull context: Context, @DimenRes itemOffsetId: Int) : this(
        context.resources.getDimensionPixelSize(itemOffsetId)
    )

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
    }
}