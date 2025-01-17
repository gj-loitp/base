package vn.loitp.up.a.cv.rv.galleryLayoutManager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.openUrlInBrowser
import com.loitp.core.ext.setSafeOnClickListenerElastic
import com.loitp.views.rv.gallery.GalleryLayoutManager
import com.loitp.views.rv.gallery.GalleryLayoutManager.ItemTransformer
import vn.loitp.databinding.ARvMenuGalleryLayoutManagerBinding
import vn.loitp.up.a.cv.rv.normalRv.Movie
import vn.loitp.up.a.cv.rv.normalRvWithSingletonData.DummyData.Companion.instance
import vn.loitp.up.common.Constants.Companion.URL_IMG
import kotlin.math.abs

@LogTag("GalleryLayoutManagerHorizontalActivity")
@IsFullScreen(false)
class GalleryLayoutManagerHorizontalActivity : BaseActivityFont() {
    private var mAdapter: GalleryAdapter? = null
    private lateinit var binding: ARvMenuGalleryLayoutManagerBinding

//    override fun setLayoutResourceId(): Int {
//        return NOT_FOUND
//    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ARvMenuGalleryLayoutManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        binding.lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.let {
                it.setSafeOnClickListenerElastic(
                    runnable = {
                        context.openUrlInBrowser(
                            url = "https://github.com/BCsl/GalleryLayoutManager"
                        )
                    }
                )
                it.isVisible = true
                it.setImageResource(com.loitp.R.drawable.ic_baseline_code_48)
            }
            this.tvTitle?.text = GalleryLayoutManagerHorizontalActivity::class.java.simpleName
        }

        mAdapter = GalleryAdapter(
            context = this, moviesList = instance.movieList,
            callback = object : GalleryAdapter.Callback {
                override fun onClick(movie: Movie, position: Int) {
                    showShortInformation("Click " + movie.title)
                }

                override fun onLongClick(movie: Movie, position: Int) {
                    // do nothing
                }

                override fun onLoadMore() {
                    // do nothing
                }
            }
        )
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.rv.layoutManager = mLayoutManager
        binding.rv.itemAnimator = DefaultItemAnimator()
        // rv.setAdapter(mAdapter);
        val layoutManager = GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL)
        layoutManager.attach(binding.rv) // default selected position is 0
        // layoutManager.attach(recyclerView, 30);

        // ...
        // setup adapter for your RecycleView
        binding.rv.adapter = mAdapter
        layoutManager.setCallbackInFling(true) // should receive callback when flinging, default is false
        layoutManager.setOnItemSelectedListener { _, _, position ->
            binding.textView.text = position.toString() + "/" + mAdapter?.itemCount
        }

        // Apply ItemTransformer just like ViewPager
        layoutManager.setItemTransformer(ScaleTransformer())
        prepareMovieData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun prepareMovieData() {
        if (instance.movieList.isEmpty()) {
            for (i in 0..49) {
                val movie = Movie(
                    title = "Loitp $i",
                    genre = "Action & Adventure $i",
                    year = "Year: $i",
                    cover = URL_IMG
                )
                instance.movieList.add(movie)
            }
        }
        mAdapter?.notifyDataSetChanged()
    }

    inner class ScaleTransformer : ItemTransformer {
        override fun transformItem(
            layoutManager: GalleryLayoutManager,
            item: View,
            fraction: Float
        ) {
            item.pivotX = item.width / 2f
            item.pivotY = item.height / 2.0f
            val scale = 1 - 0.4f * abs(fraction)
            item.scaleX = scale
            item.scaleY = scale
        }
    }
}
