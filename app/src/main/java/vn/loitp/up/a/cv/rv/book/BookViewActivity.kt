package vn.loitp.up.a.cv.rv.book

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.common.NOT_FOUND
import com.loitp.core.ext.setSafeOnClickListenerElastic
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import vn.loitp.databinding.ARvBookViewBinding
import vn.loitp.up.a.cv.rv.normalRv.Movie
import vn.loitp.up.common.Constants

@LogTag("BookViewActivity")
@IsFullScreen(false)
class BookViewActivity : BaseActivityFont() {
    private val movieList: MutableList<Movie> = ArrayList()
    private var bookAdapter: BookAdapter? = null
    private lateinit var binding: ARvBookViewBinding

//    override fun setLayoutResourceId(): Int {
//        return NOT_FOUND
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ARvBookViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.isVisible = false
            this.tvTitle?.text = BookViewActivity::class.java.simpleName
        }
        bookAdapter = BookAdapter(
            context = this, column = 3, moviesList = movieList,
            callback = object : BookAdapter.Callback {
                override fun onClick(movie: Movie, position: Int) {
                    showShortInformation("Click " + movie.title)
                }

                override fun onLongClick(movie: Movie, position: Int) {
                    val isRemoved = movieList.remove(movie)
                    if (isRemoved) {
                        bookAdapter?.apply {
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, movieList.size)
                            checkData()
                        }
                    }
                }

                override fun onLoadMore() {}
            }
        )
        binding.rv.layoutManager = GridLayoutManager(this, 3)
        bookAdapter?.let {
//            val scaleAdapter = AlphaInAnimationAdapter(it)
            val scaleAdapter = ScaleInAnimationAdapter(it)
//            val scaleAdapter = SlideInBottomAnimationAdapter(it)
//            val scaleAdapter = SlideInLeftAnimationAdapter(it)
//            val scaleAdapter = SlideInRightAnimationAdapter(it)
            scaleAdapter.setDuration(1000)
//            scaleAdapter.setInterpolator(OvershootInterpolator())
            scaleAdapter.setFirstOnly(true)
            binding.rv.adapter = scaleAdapter
        }
        prepareMovieData()
    }

    private fun prepareMovieData() {
        var cover: String
        for (i in 0..99) {
            cover = if (i % 2 == 0) {
                Constants.URL_IMG_1
            } else {
                Constants.URL_IMG_2
            }
            val movie = Movie("Loitp $i", "Action & Adventure $i", "Year: $i", cover)
            movieList.add(movie)
        }
        bookAdapter?.apply {
            checkData()
            notifyItemRangeChanged(0, this.itemCount)
        }
    }
}
