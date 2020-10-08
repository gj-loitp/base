package com.function.epub.core

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Html.ImageGetter
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
import com.R
import com.annotation.IsFullScreen
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LAnimationUtil
import com.core.utilities.LConnectivityUtil
import com.core.utilities.LPrefUtil.Companion.getTextSizeEpub
import com.core.utilities.LPrefUtil.Companion.setTextSizeEpub
import com.core.utilities.LReaderUtil.Companion.decodeBitmapFromByteArray
import com.core.utilities.LReaderUtil.Companion.defaultCover
import com.core.utilities.LScreenUtil
import com.core.utilities.LUIUtil
import com.daimajia.androidanimations.library.Techniques
import com.function.epub.BookSection
import com.function.epub.Reader
import com.function.epub.core.PageFragment.OnFragmentReadyListener
import com.function.epub.exception.OutOfPagesException
import com.function.epub.exception.ReadingException
import com.function.epub.model.BookInfo
import com.function.epub.model.BookInfoData
import com.function.epub.viewmodels.EpubViewModel
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.interfaces.CallbackAnimation
import com.utils.util.ConvertUtils
import com.views.LWebView
import com.views.setSafeOnClickListener
import com.views.viewpager.viewpagertransformers.ZoomOutSlideTransformer
import kotlinx.android.synthetic.main.l_activity_epub_reader_read.*
import java.util.*

@LogTag("loitppEpubReaderReadActivity")
@IsFullScreen(false)
class EpubReaderReadActivity : BaseFontActivity(), OnFragmentReadyListener {

    companion object {
        private const val idWebView = 696969
    }

    private val reader = Reader()
    private var isSkippedToPage = false
    private var sectionsPagerAdapter: SectionsPagerAdapter? = null
    private var pageCount = Int.MAX_VALUE
    private var pxScreenWidth = LScreenUtil.screenWidth
    private var bookInfo: BookInfo? = null
    private var adView: AdView? = null
    private var epubViewModel: EpubViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.l_activity_epub_reader_read)

        bookInfo = BookInfoData.instance.bookInfo
        if (bookInfo == null) {
            showShort(getString(R.string.err_unknow), true)
            onBackPressed()
        }

        setupViews()
        setupViewModels()

        bookInfo?.let {
            epubViewModel?.loadData(reader = reader, bookInfo = it)
        }
    }

    private fun setupViews() {
        LUIUtil.setTextShadow(textView = tvTitle)
        setCoverBitmap()
        val titleBook = bookInfo?.title ?: "Loading..."
        tvTitle.text = titleBook

        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        LUIUtil.setPullLikeIOSHorizontal(viewPager = viewPager)
        viewPager.offscreenPageLimit = 0
        viewPager.setPageTransformer(true, ZoomOutSlideTransformer())
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                tvPage.text = "$position"
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        viewPager.adapter = sectionsPagerAdapter

        val adUnitId = intent.getStringExtra(Constants.AD_UNIT_ID_BANNER)
        if (adUnitId.isNullOrEmpty() || !LConnectivityUtil.isConnected()) {
            lnAdView.visibility = View.GONE
        } else {
            adView = AdView(this)
            adView?.let { av ->
                av.adSize = AdSize.SMART_BANNER
                av.adUnitId = adUnitId
                LUIUtil.createAdBanner(av)
                lnAdView.addView(av)
                lnAdView.requestLayout()
            }
        }
        btBack.setSafeOnClickListener {
            onBackPressed()
        }
        btZoomIn.setSafeOnClickListener { view ->
            LAnimationUtil.play(view = view, techniques = Techniques.Pulse)

            sectionsPagerAdapter?.let { adapter ->
                val pageFragment = adapter.instantiateItem(viewPager, viewPager.currentItem)
                if (pageFragment is PageFragment) {
                    zoomIn(pageFragment = pageFragment)
                }

                val pageFragmentNext = adapter.instantiateItem(viewPager, viewPager.currentItem + 1)
                if (pageFragmentNext is PageFragment) {
                    zoomIn(pageFragment = pageFragmentNext)
                }

                val pageFragmentPrev = adapter.instantiateItem(viewPager, viewPager.currentItem - 1)
                if (pageFragmentPrev is PageFragment) {
                    zoomIn(pageFragment = pageFragmentPrev)
                }
            }
        }
        btZoomOut.setSafeOnClickListener { view ->
            LAnimationUtil.play(view = view, techniques = Techniques.Pulse)
            sectionsPagerAdapter?.let { adapter ->
                val pageFragment = adapter.instantiateItem(viewPager, viewPager.currentItem)
                if (pageFragment is PageFragment) {
                    zoomOut(pageFragment)
                }

                val pageFragmentNext = adapter.instantiateItem(viewPager, viewPager.currentItem + 1)
                if (pageFragmentNext is PageFragment) {
                    zoomOut(pageFragmentNext)
                }

                val pageFragmentPrev = adapter.instantiateItem(viewPager, viewPager.currentItem - 1)
                if (pageFragmentPrev is PageFragment) {
                    zoomOut(pageFragmentPrev)
                }
            }
        }
        llGuide.setSafeOnClickListener {
            LAnimationUtil.play(view = llGuide, techniques = Techniques.SlideOutLeft, callbackAnimation = object : CallbackAnimation {
                override fun onCancel() {}
                override fun onEnd() {
                    llGuide?.visibility = View.GONE
                }

                override fun onRepeat() {}
                override fun onStart() {}
            })
        }
    }

    private fun setupViewModels() {
        epubViewModel = getViewModel(EpubViewModel::class.java)
        epubViewModel?.let { vm ->
            vm.loadDataActionLiveData.observe(this, androidx.lifecycle.Observer { actionData ->
//                logD("loadDataActionLiveData observe " + BaseApplication.gson.toJson(actionData))
                val isDoing = actionData.isDoing
                val isSuccess = actionData.isSuccess
                
                if (isDoing == false && isSuccess == true) {
                    LUIUtil.setDelay(mls = 1000, runnable = Runnable {
                        rlSplash?.visibility = View.GONE
                    })
                    sectionsPagerAdapter?.notifyDataSetChanged()
                    val lastSavedPage = actionData.data ?: 0
                    viewPager?.currentItem = lastSavedPage
                    if (lastSavedPage == 0) {
                        tvPage?.text = "0"
                    }
                    llGuide?.visibility = View.VISIBLE
                }
            })
        }
    }

    private fun setCoverBitmap() {
        bookInfo?.let { bi ->
            val isCoverImageNotExists = bi.isCoverImageNotExists
            if (!isCoverImageNotExists) {
                // Not searched for coverImage for this position yet. It may exist.
                val savedBitmap = bi.coverImageBitmap
                if (savedBitmap == null) {
                    val coverImageAsBytes = bi.coverImage
                    if (coverImageAsBytes == null) {
                        // Searched and not found.
                        bi.isCoverImageNotExists = true
                        ivCover.setImageResource(defaultCover)
                    } else {
                        val bitmap = decodeBitmapFromByteArray(coverImage = coverImageAsBytes, reqWidth = 100, reqHeight = 200)
                        bi.coverImageBitmap = bitmap
                        bi.coverImage = null
                        ivCover.setImageBitmap(bitmap)
                    }
                } else {
                    ivCover.setImageBitmap(savedBitmap)
                }
            } else {
                // Searched before and not found.
                ivCover.setImageResource(defaultCover)
            }
        }
    }

    override fun onFragmentReady(position: Int): View? {
        var bookSection: BookSection? = null
        try {
            bookSection = reader.readSection(position)
        } catch (e: ReadingException) {
            e.printStackTrace()
        } catch (e: OutOfPagesException) {
            e.printStackTrace()
            pageCount = e.pageCount
            if (isSkippedToPage) {
                showShort("Max page number is: $pageCount")
            }
            sectionsPagerAdapter?.notifyDataSetChanged()
        } catch (e: Exception) {
            logE("onFragmentReady $e")
        }
        isSkippedToPage = false
        return if (bookSection != null) {
            setFragmentView(isContentStyled = true,
                    data = bookSection.sectionContent,
                    mimeType = "text/html",
                    encoding = "UTF-8")
        } else {
            null
        }
    }

    public override fun onPause() {
        adView?.pause()
        super.onPause()
    }

    public override fun onResume() {
        adView?.resume()
        super.onResume()
    }

    override fun onDestroy() {
        adView?.destroy()
        BookInfoData.instance.bookInfo = null
        super.onDestroy()
    }

    public override fun onStop() {
        super.onStop()
        try {
            reader.saveProgress(viewPager.currentItem)
            showShort(msg = "Saved page: " + viewPager.currentItem + "...")
        } catch (e: ReadingException) {
            e.printStackTrace()
            showShort(msg = "Progress is not saved: " + e.message)
        } catch (e: OutOfPagesException) {
            e.printStackTrace()
            showShort(msg = "Progress is not saved. Out of Bounds. Page Count: " + e.pageCount)
        }
    }

    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return pageCount
        }

        override fun getItem(position: Int): Fragment {
            return PageFragment.newInstance(position)
        }
    }

    @Suppress("DEPRECATION")
    private fun setFragmentView(isContentStyled: Boolean, data: String?, mimeType: String, encoding: String): View {
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return if (isContentStyled) {
            val lWebView = LWebView(this)
//            lWebView.webViewClient = object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                    return true
//                }
//            }
            lWebView.id = idWebView
            lWebView.loadDataWithBaseURL(null, getStyledFont(data), mimeType, encoding, null)
            lWebView.scrollBarSize = ConvertUtils.dp2px(2f)
            lWebView.layoutParams = layoutParams

            val size = getTextSizeEpub()
            updateUIWevViewSize(lWebView, size)
            lWebView
        } else {
            val scrollView = ScrollView(this)
            scrollView.layoutParams = layoutParams
            val textView = TextView(this)
            textView.layoutParams = layoutParams
            textView.text = Html.fromHtml(data, ImageGetter { source ->
                val imageAsStr = source.substring(source.indexOf(";base64,") + 8)
                val imageAsBytes = Base64.decode(imageAsStr, Base64.DEFAULT)
                val imageAsBitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
                val imageWidthStartPx = (pxScreenWidth - imageAsBitmap.width) / 2
                val imageWidthEndPx = pxScreenWidth - imageWidthStartPx
                val imageAsDrawable: Drawable = BitmapDrawable(resources, imageAsBitmap)
                imageAsDrawable.setBounds(imageWidthStartPx, 0, imageWidthEndPx, imageAsBitmap.height)
                imageAsDrawable
            }, null)
            val pxPadding = ConvertUtils.dp2px(12f)
            textView.setPadding(pxPadding, pxPadding, pxPadding, pxPadding)
            scrollView.addView(textView)
            scrollView
        }
    }

    private fun getStyledFont(html: String?): String {
        val addBodyStart = html?.toLowerCase(Locale.getDefault())?.contains("<body>") ?: true
        val addBodyEnd = html?.toLowerCase(Locale.getDefault())?.contains("</body") ?: true
        return "<style type=\"text/css\">@font-face {font-family: CustomFont;" +
                "src: url(\"file:///android_asset/" +
                LUIUtil.fontForAll +
                "\")}" +
                "body {font-family: CustomFont;font-size: medium;text-align: justify;}</style>" +
                (if (addBodyStart) "<body>" else "") + html + if (addBodyEnd) "</body>" else ""
    }

    @Suppress("DEPRECATION")
    private fun zoomIn(pageFragment: PageFragment?) {
        if (pageFragment == null || pageFragment.view == null) {
            return
        }
        val webView = pageFragment.view?.findViewById<WebView>(idWebView)
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
            setTextSizeEpub(value = size)
            updateUIWevViewSize(webView = webView, size = size)
        }
    }

    @Suppress("DEPRECATION")
    private fun zoomOut(pageFragment: PageFragment?) {
        if (pageFragment == null || pageFragment.view == null) {
            return
        }
        val webView = pageFragment.view?.findViewById<WebView>(idWebView) ?: return
        val settings = webView.settings
        val currentAiVersion = Build.VERSION.SDK_INT
        if (currentAiVersion <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            settings.textSize = WebSettings.TextSize.SMALLEST
        } else {
            var size = (settings.textZoom / 1.1).toInt()
            if (size < 50) {
                size = 50
            }
            setTextSizeEpub(value = size)
            updateUIWevViewSize(webView = webView, size = size)
        }
    }

    @Suppress("DEPRECATION")
    private fun updateUIWevViewSize(webView: WebView, size: Int) {
        val settings = webView.settings
        val currentApiVersion = Build.VERSION.SDK_INT
        if (currentApiVersion <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            settings.textSize = WebSettings.TextSize.LARGER
        } else {
            settings.textZoom = size
        }
    }
}