package com.loitp.core.helper.gallery.album

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.annotation.Keep
import androidx.recyclerview.widget.LinearLayoutManager
import com.loitp.R
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.IsSwipeActivity
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.common.KEY_REMOVE_ALBUM_FLICKR_LIST
import com.loitp.core.common.NOT_FOUND
import com.loitp.core.common.SK_PHOTOSET_ID
import com.loitp.core.common.SK_PHOTOSET_SIZE
import com.loitp.core.ext.*
import com.loitp.core.helper.gallery.photos.GalleryCorePhotosActivity
import com.loitp.databinding.LAFlickrGalleryCoreAlbumBinding
import com.loitp.restApi.flickr.FlickrConst
import com.loitp.restApi.flickr.model.photoSetGetList.Photoset
import com.loitp.restApi.flickr.service.FlickrService
import com.loitp.restApi.restClient.RestClient
import com.loitp.views.layout.swipeBack.SwipeBackLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator

/**
 * Created by Loitp on 04,August,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
@Keep
@LogTag("GalleryCoreAlbumActivity")
@IsFullScreen(false)
@IsSwipeActivity(true)
class GalleryCoreAlbumActivity : BaseActivityFont() {
    private var albumAdapter: AlbumAdapter? = null
    private val listPhotoSet = ArrayList<Photoset>()
    private var listRemoveAlbum = ArrayList<String>()
    private lateinit var binding: LAFlickrGalleryCoreAlbumBinding

//    override fun setLayoutResourceId(): Int {
//        return NOT_FOUND
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LAFlickrGalleryCoreAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isValidPackageName()
        setupViews()
    }

    private fun setupViews() {

        intent.getStringArrayListExtra(KEY_REMOVE_ALBUM_FLICKR_LIST)?.let {
            listRemoveAlbum.addAll(it)
        }

        val animator = SlideInRightAnimator(OvershootInterpolator(1f))
        animator.addDuration = 1000

        binding.recyclerView.apply {
            itemAnimator = animator
            layoutManager = LinearLayoutManager(this@GalleryCoreAlbumActivity)
            setHasFixedSize(true)
        }

        albumAdapter = AlbumAdapter(
            listPhotoSet = listPhotoSet,
            callback = object : AlbumAdapter.Callback {
                override fun onClick(pos: Int) {
                    val intent =
                        Intent(this@GalleryCoreAlbumActivity, GalleryCorePhotosActivity::class.java)
                    intent.apply {
                        putExtra(SK_PHOTOSET_ID, listPhotoSet[pos].id)
                        putExtra(SK_PHOTOSET_SIZE, listPhotoSet[pos].photos)
                        startActivity(this)
                        this@GalleryCoreAlbumActivity.tranIn()
                    }
                }

                override fun onLongClick(pos: Int) {
                }
            }
        )

        albumAdapter?.let {
//            val animAdapter = AlphaInAnimationAdapter(it)
//            val animAdapter = ScaleInAnimationAdapter(it)
            val animAdapter = SlideInBottomAnimationAdapter(it)
//            val animAdapter = SlideInLeftAnimationAdapter(it)
//            val animAdapter = SlideInRightAnimationAdapter(it)

            animAdapter.setDuration(1000)
//          animAdapter.setInterpolator(OvershootInterpolator())
            animAdapter.setFirstOnly(true)
            binding.recyclerView.adapter = animAdapter
        }

//        LUIUtil.setPullLikeIOSVertical(this)

        binding.swipeBackLayout.setSwipeBackListener(object : SwipeBackLayout.OnSwipeBackListener {
            override fun onViewPositionChanged(
                mView: View?,
                swipeBackFraction: Float,
                swipeBackFactor: Float
            ) {
            }

            override fun onViewSwipeFinished(mView: View?, isEnd: Boolean) {
                if (isEnd) {
                    finish()//correct
                    this@GalleryCoreAlbumActivity.transActivityNoAnimation()
                }
            }
        })
        getListPhotosets()
    }

    private fun getListPhotosets() {
        binding.progressBar.showProgress()
        RestClient.init(getString(R.string.flickr_URL))
        val service = RestClient.createService(FlickrService::class.java)
        val method = FlickrConst.METHOD_PHOTOSETS_GETLIST
        val apiKey = FlickrConst.API_KEY
        val userID = FlickrConst.USER_KEY
        val page = 1
        val perPage = 500
        val primaryPhotoExtras = FlickrConst.PRIMARY_PHOTO_EXTRAS_0
        val format = FlickrConst.FORMAT
        val noJsonCallBack = FlickrConst.NO_JSON_CALLBACK

        compositeDisposable.add(
            service.getListPhotoset(
                method = method,
                apiKey = apiKey,
                userId = userID,
                page = page,
                perPage = perPage,
                primaryPhotoExtras = primaryPhotoExtras,
                format = format,
                noJsonCallback = noJsonCallBack
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ wrapperPhotoSetGetList ->
                    wrapperPhotoSetGetList?.photosets?.photoset?.let {
                        listPhotoSet.addAll(it)
                    }
                    listRemoveAlbum.let {
                        for (i in it.indices.reversed()) {
                            for (j in listPhotoSet.indices.reversed()) {
                                if (it[i] == listPhotoSet[j].id) {
                                    listPhotoSet.removeAt(j)
                                }
                            }
                        }
                    }

                    // sort date
//                    listPhotoSet.sortWith { o1, o2 ->
//                        java.lang.Long.valueOf(o2.dateUpdate).compareTo(java.lang.Long.valueOf(o1.dateUpdate))
//                    }
                    // random
                    listPhotoSet.shuffle()

                    updateAllViews()
                    binding.progressBar.hideProgress()
                }, { throwable ->
                    handleException(throwable)
                    binding.progressBar.hideProgress()
                })
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateAllViews() {
        albumAdapter?.notifyDataSetChanged()
    }

}
