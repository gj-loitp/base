package com.loitp.func.epub.core

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.daimajia.androidanimations.library.Techniques
import com.loitp.R
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.common.NOT_FOUND
import com.loitp.core.ext.*
import com.loitp.core.utils.ConvertUtils
import com.loitp.databinding.LAEpubReaderReadBinding
import com.loitp.databinding.LATttComicSplashBinding
import com.loitp.func.epub.BookSection
import com.loitp.func.epub.Reader
import com.loitp.func.epub.core.PageFragment.OnFragmentReadyListener
import com.loitp.func.epub.exception.OutOfPagesException
import com.loitp.func.epub.exception.ReadingException
import com.loitp.func.epub.model.BookInfo
import com.loitp.func.epub.model.BookInfoData
import com.loitp.func.epub.vm.EpubViewModel
import com.loitp.views.wv.LWebViewAdblock

/**
 * Created by Loitp on 04,August,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
@LogTag("EpubReaderReadActivity")
@IsFullScreen(false)
class EpubReaderReadActivity : BaseActivityFont(), OnFragmentReadyListener {
    private lateinit var binding: LAEpubReaderReadBinding

    companion object {
        private const val idWebView = 696969
    }

    private val reader = Reader()
    private var isSkippedToPage = false
    private var sectionsPagerAdapter: SectionsPagerAdapter? = null
    private var pageCount = Int.MAX_VALUE
    private val pxScreenWidth = screenWidth
    private var bookInfo: BookInfo? = null
    private var epubViewModel: EpubViewModel? = null

//    override fun setLayoutResourceId(): Int {
//        return NOT_FOUND
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LAEpubReaderReadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookInfo = BookInfoData.instance.bookInfo
        if (bookInfo == null) {
            showShortError(msg = getString(R.string.err_unknown))
            onBaseBackPressed()
        }

        setupViews()
        setupViewModels()

        bookInfo?.let {
            epubViewModel?.loadData(reader = reader, bookInfo = it)
        }
    }

    private fun setupViews() {
        setCoverBitmap()
        val titleBook = bookInfo?.title ?: getString(R.string.loading)
        binding.tvTitle.text = titleBook

        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        binding.viewPager.apply {
            this.setPullLikeIOSHorizontal()
            this.offscreenPageLimit = 2
//            this.setPageTransformer(true, ZoomOutSlideTransformer())
            this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    binding.tvPage.text = "$position"
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }

        binding.btBack.setSafeOnClickListener {
//            onBackPressed()
            onBaseBackPressed()
        }
        binding.btZoomIn.setSafeOnClickListener {
            handleZoomIn()
        }
        binding.btZoomOut.setSafeOnClickListener {
            handleZoomOut()
        }
        binding.llGuide.setSafeOnClickListener {
            handleGuide()
        }
    }

    private fun setupViewModels() {
        epubViewModel = getViewModel(EpubViewModel::class.java)
        epubViewModel?.let { vm ->
            vm.loadDataActionLiveData.observe(this) { actionData ->
//                logD("loadDataActionLiveData observe " + BaseApplication.gson.toJson(actionData))
                val isDoing = actionData.isDoing
                val isSuccess = actionData.isSuccess

                if (isDoing == false && isSuccess == true) {
                    setDelay(mls = 1000, runnable = {
                        binding.rlSplash.visibility = View.GONE
                    })
                    binding.viewPager.adapter = sectionsPagerAdapter
                    val lastSavedPage = actionData.data ?: 1
                    logD("loadDataActionLiveData lastSavedPage $lastSavedPage")
                    binding.viewPager.currentItem = lastSavedPage
                    if (lastSavedPage == 0) {
                        binding.tvPage.text = "0"
                    }
                    binding.llGuide.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun handleZoomIn() {
        binding.btZoomIn.play(techniques = Techniques.Pulse)

        sectionsPagerAdapter?.let { adapter ->
            try {
                val pageFragment =
                    adapter.instantiateItem(binding.viewPager, binding.viewPager.currentItem)
                if (pageFragment is PageFragment) {
                    zoomIn(pageFragment = pageFragment)
                }

                val pageFragmentNext =
                    adapter.instantiateItem(binding.viewPager, binding.viewPager.currentItem + 1)
                if (pageFragmentNext is PageFragment) {
                    zoomIn(pageFragment = pageFragmentNext)
                }

                val pageFragmentPrev =
                    adapter.instantiateItem(binding.viewPager, binding.viewPager.currentItem - 1)
                if (pageFragmentPrev is PageFragment) {
                    zoomIn(pageFragment = pageFragmentPrev)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleZoomOut() {
        binding.btZoomOut.play(techniques = Techniques.Pulse)
        sectionsPagerAdapter?.let { adapter ->
            try {
                val pageFragment =
                    adapter.instantiateItem(binding.viewPager, binding.viewPager.currentItem)
                if (pageFragment is PageFragment) {
                    zoomOut(pageFragment)
                }

                val pageFragmentNext =
                    adapter.instantiateItem(binding.viewPager, binding.viewPager.currentItem + 1)
                if (pageFragmentNext is PageFragment) {
                    zoomOut(pageFragmentNext)
                }

                val pageFragmentPrev =
                    adapter.instantiateItem(binding.viewPager, binding.viewPager.currentItem - 1)
                if (pageFragmentPrev is PageFragment) {
                    zoomOut(pageFragmentPrev)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleGuide() {
        binding.llGuide.visibility = View.GONE
    }

    private fun setCoverBitmap() {
        bookInfo?.let { bi ->
            val isCoverImageNotExists = bi.isCoverImageNotExists
            if (isCoverImageNotExists) {
                // Searched before and not found.
                binding.ivCover.setImageResource(defaultCover)
            } else {
                // Not searched for coverImage for this position yet. It may exist.
                val savedBitmap = bi.coverImageBitmap
                if (savedBitmap == null) {
                    val coverImageAsBytes = bi.coverImage
                    if (coverImageAsBytes == null) {
                        // Searched and not found.
                        bi.isCoverImageNotExists = true
                        binding.ivCover.setImageResource(defaultCover)
                    } else {
                        val bitmap = decodeBitmapFromByteArray(
                            coverImage = coverImageAsBytes,
                            reqWidth = 100,
                            reqHeight = 200
                        )
                        bi.coverImageBitmap = bitmap
                        bi.coverImage = null
                        binding.ivCover.setImageBitmap(bitmap)
                    }
                } else {
                    binding.ivCover.setImageBitmap(savedBitmap)
                }
            }
        }
    }

    override fun onFragmentReady(position: Int): View? {
        logD("onFragmentReady position $position")
        var bookSection: BookSection? = null
        try {
            bookSection = reader.readSection(position)
        } catch (e: ReadingException) {
            logE("onFragmentReady ReadingException $e")
        } catch (e: OutOfPagesException) {
            logE("onFragmentReady OutOfPagesException $e")
            pageCount = e.pageCount
            if (isSkippedToPage) {
                showShortInformation("Max page number is: $pageCount")
            }
            sectionsPagerAdapter?.notifyDataSetChanged()
        } catch (e: Exception) {
            logE("onFragmentReady Exception $e")
        }
        isSkippedToPage = false
        return if (bookSection == null) {
            null
        } else {
            setFragmentView(
                isContentStyled = true,
                data = bookSection.sectionContent ?: "",
                mimeType = "text/html",
                encoding = "UTF-8"
            )
        }
    }

    override fun onDestroy() {
        BookInfoData.instance.bookInfo = null
        super.onDestroy()
    }

    public override fun onStop() {
        super.onStop()
        try {
            reader.saveProgress(binding.viewPager.currentItem)
            showShortInformation(msg = "Saved page: " + binding.viewPager.currentItem + "...")
        } catch (e: ReadingException) {
            e.printStackTrace()
            showShortError(msg = "Progress is not saved: " + e.message)
        } catch (e: OutOfPagesException) {
            e.printStackTrace()
            showShortError(msg = "Progress is not saved. Out of Bounds. Page Count: " + e.pageCount)
        }
    }

    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return pageCount
        }

        override fun getItem(position: Int): Fragment {
            return PageFragment.newInstance(tabPosition = position)
        }
    }

    @Suppress("DEPRECATION")
    private fun setFragmentView(
        isContentStyled: Boolean,
        data: String,
        mimeType: String,
        encoding: String
    ): View {
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        logD("setFragmentView data $data")
        return if (isContentStyled) {
            val lWebView = LWebViewAdblock(this)
            lWebView.apply {
                setBackgroundColor(Color.TRANSPARENT)
                shouldOverrideUrlLoading(shouldOverrideUrlLoading = true)
                setEnableCopyContent(isEnableCopyContent = true)
                id = idWebView
//                logD(">>>setFragmentView data $data")
                val fontSizePx = getDimenValue(com.loitp.R.dimen.txt_small)
                val paddingPx = getDimenValue(R.dimen.margin_padding_small)
//                logD(">>>setFragmentView fontSizePx $fontSizePx, paddingPx $paddingPx")
                if (isDarkTheme()) {
                    loadDataString(
                        bodyContent = data,
                        backgroundColor = "black",
                        textColor = "white",
                        textAlign = "justify",
                        fontSizePx = fontSizePx,
                        paddingPx = paddingPx
                    )
                } else {
                    loadDataString(
                        bodyContent = data,
                        backgroundColor = "white",
                        textColor = "black",
                        textAlign = "justify",
                        fontSizePx = fontSizePx,
                        paddingPx = paddingPx
                    )
                }

                scrollBarSize = ConvertUtils.dp2px(2f)
                this.layoutParams = layoutParams
                lWebView.setTextSize(sizePercent = getTextSizePercentEpub())
                callback = object : LWebViewAdblock.Callback {
                    override fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int) {
                    }

                    override fun onScrollTopToBottom() {
//                    logD("onScrollTopToBottom")
                    }

                    override fun onScrollBottomToTop() {
//                    logD("onScrollBottomToTop")
                    }

                    override fun onProgressChanged(progress: Int) {
//                    logD("onProgressChanged $progress")
                    }

                    override fun shouldOverrideUrlLoading(url: String) {
//                    logD("shouldOverrideUrlLoading $url")
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                    }
                }
            }
            lWebView
        } else {
            // this case wont occur
            val scrollView = ScrollView(this)
            scrollView.layoutParams = layoutParams
            val textView = TextView(this)
            textView.layoutParams = layoutParams
            textView.text = Html.fromHtml(data, { source ->
                val imageAsStr = source.substring(source.indexOf(";base64,") + 8)
                val imageAsBytes = Base64.decode(imageAsStr, Base64.DEFAULT)
                val imageAsBitmap =
                    BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
                val imageWidthStartPx = (pxScreenWidth - imageAsBitmap.width) / 2
                val imageWidthEndPx = pxScreenWidth - imageWidthStartPx
                val imageAsDrawable: Drawable = BitmapDrawable(resources, imageAsBitmap)
                imageAsDrawable.setBounds(
                    imageWidthStartPx,
                    0,
                    imageWidthEndPx,
                    imageAsBitmap.height
                )
                imageAsDrawable
            }, null)
            val pxPadding = ConvertUtils.dp2px(12f)
            textView.setPadding(pxPadding, pxPadding, pxPadding, pxPadding)
            scrollView.addView(textView)
            scrollView
        }
    }

    @Suppress("DEPRECATION")
    private fun zoomIn(pageFragment: PageFragment?) {
        if (pageFragment == null || pageFragment.view == null) {
            return
        }
        val webView = pageFragment.view?.findViewById<LWebViewAdblock>(idWebView)
        if (webView == null) {
            logE("webView null")
            return
        }
        val settings = webView.settings
        val currentApiVersion = Build.VERSION.SDK_INT
        if (currentApiVersion <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            settings.textSize = WebSettings.TextSize.LARGER
        } else {
            var size = (settings.textZoom * 1.1).toInt()
            if (size > 250) {
                size = 250
            }
            setTextSizePercentEpub(value = size)
            webView.setTextSize(sizePercent = size)
        }
    }

    @Suppress("DEPRECATION")
    private fun zoomOut(pageFragment: PageFragment?) {
        if (pageFragment == null || pageFragment.view == null) {
            return
        }
        val webView = pageFragment.view?.findViewById<LWebViewAdblock>(idWebView) ?: return
        val settings = webView.settings
        val currentAiVersion = Build.VERSION.SDK_INT
        if (currentAiVersion <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            settings.textSize = WebSettings.TextSize.SMALLEST
        } else {
            var size = (settings.textZoom / 1.1).toInt()
            if (size < 50) {
                size = 50
            }
            setTextSizePercentEpub(value = size)
            webView.setTextSize(sizePercent = size)
        }
    }
}
