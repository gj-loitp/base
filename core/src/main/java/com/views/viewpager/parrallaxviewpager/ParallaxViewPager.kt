package com.views.viewpager.parrallaxviewpager

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT
import android.graphics.drawable.GradientDrawable.Orientation.RIGHT_LEFT
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.viewpager.widget.ViewPager
import loitp.core.R

class ParallaxViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {
    private var mMode: Mode? = null
    private val mShadowStart = Color.parseColor("#33000000")
    private val mShadowMid = Color.parseColor("#11000000")
    private val mShadowEnd = Color.parseColor("#00000000")
    private var mRightShadow: Drawable? = GradientDrawable(LEFT_RIGHT, intArrayOf(mShadowStart, mShadowMid, mShadowEnd))
    private var mLeftShadow: Drawable? = GradientDrawable(RIGHT_LEFT, intArrayOf(mShadowStart, mShadowMid, mShadowEnd))
    private val mShadowWidth: Int
    private val mParallaxTransformer: ParallaxTransformer?
    private var mInterpolator: Interpolator? = null
    private var mOutset: Int = 0
    private var mOutsetFraction = 0.5f

    var outset: Int
        get() = mOutset
        set(outset) {
            this.mOutset = outset
            mOutsetFraction = 0f
            mParallaxTransformer?.outset = mOutset
        }

    var outsetFraction: Float
        get() = mOutsetFraction
        set(outsetFraction) {
            this.mOutsetFraction = outsetFraction
            mOutset = 0
            mParallaxTransformer?.setOutsetFraction(mOutsetFraction)
        }

    var interpolator: Interpolator?
        get() = mInterpolator
        set(i) {
            mInterpolator = i
            ensureInterpolator()
        }

    var mode: Mode?
        get() = mMode
        set(mode) {
            mMode = mode
            mParallaxTransformer?.mode = mode
            if (mode === Mode.LEFT_OVERLAY) {
                setPageTransformer(true, mParallaxTransformer)
            } else if (mode === Mode.RIGHT_OVERLAY) {
                setPageTransformer(false, mParallaxTransformer)
            }
        }

    init {
        mParallaxTransformer = ParallaxTransformer()
        val a = context.obtainStyledAttributes(attrs, R.styleable.ParallaxViewPager, 0,
                0)
        mMode = Mode.values()[a.getInt(R.styleable.ParallaxViewPager_modeParallaxViewPager, 0)]
        mode = mMode

        if (a.hasValue(R.styleable.ParallaxViewPager_right_shadow)) {
            mRightShadow = a.getDrawable(R.styleable.ParallaxViewPager_right_shadow)
        }
        if (a.hasValue(R.styleable.ParallaxViewPager_left_shadow)) {
            mLeftShadow = a.getDrawable(R.styleable.ParallaxViewPager_left_shadow)
        }
        mShadowWidth = a.getDimensionPixelSize(R.styleable.ParallaxViewPager_shadow_width, dp2px(20, getContext()).toInt())
        val tv = a.peekValue(
                R.styleable.ParallaxViewPager_outset)
        if (tv != null) {
            if (tv.type == TypedValue.TYPE_FRACTION) {
                mOutsetFraction = a.getFraction(R.styleable.ParallaxViewPager_outset, 1, 1, 0f)
                outsetFraction = mOutsetFraction
            } else if (tv.type == TypedValue.TYPE_DIMENSION) {
                mOutset = TypedValue.complexToDimension(tv.data, resources.displayMetrics).toInt()
                outset = mOutset
            }
        }
        val resID = a.getResourceId(R.styleable.ParallaxViewPager_interpolatorParallaxViewPager, 0)
        if (resID > 0) {
            setInterpolator(context, resID)
        }
        a.recycle()
    }

    fun setRightShadow(rightShadow: GradientDrawable) {
        this.mRightShadow = rightShadow
    }

    fun setLeftShadow(leftShadow: GradientDrawable) {
        this.mLeftShadow = leftShadow
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawShadow(canvas)
    }

    fun setInterpolator(context: Context, resID: Int) {
        interpolator = AnimationUtils.loadInterpolator(context, resID)
    }

    protected fun ensureInterpolator() {
        if (mInterpolator == null) {
            mInterpolator = LinearInterpolator()
        }
        mParallaxTransformer?.interpolator = mInterpolator as Interpolator
    }

    fun drawShadow(canvas: Canvas) {
        if (mMode === Mode.NONE) {
            return
        }
        if (scrollX % width == 0) {
            return
        }
        when (mMode) {
            Mode.LEFT_OVERLAY -> drawRightShadow(canvas)
            Mode.RIGHT_OVERLAY -> drawLeftShadow(canvas)
            else -> {
            }
        }
    }

    private fun drawRightShadow(canvas: Canvas) {
        canvas.save()
        val translate = ((scrollX / width + 1) * width).toFloat()
        canvas.translate(translate, 0f)
        mRightShadow?.setBounds(0, 0, mShadowWidth, height)
        mRightShadow?.draw(canvas)
        canvas.restore()
    }

    private fun drawLeftShadow(canvas: Canvas) {
        canvas.save()
        val translate = ((scrollX / width + 1) * width - mShadowWidth).toFloat()
        canvas.translate(translate, 0f)
        mLeftShadow?.setBounds(0, 0, mShadowWidth, height)
        mLeftShadow?.draw(canvas)
        canvas.restore()
    }

    override fun setPageMargin(marginPixels: Int) {
        super.setPageMargin(0)
    }

    private fun dp2px(dip: Int, context: Context): Float {
        val scale = context.resources.displayMetrics.density
        return dip * scale + 0.5f
    }

    override fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int) {
        super.onPageScrolled(position, offset, offsetPixels)
        if (offset == 0f) {
            val count = childCount
            for (i in 0 until count) {
                mParallaxTransformer?.transformPage(getChildAt(i), 0f)
            }
        }
    }

}