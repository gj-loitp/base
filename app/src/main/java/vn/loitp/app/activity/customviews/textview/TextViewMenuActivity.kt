package vn.loitp.app.activity.customviews.textview

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.annotation.IsFullScreen
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.utilities.LActivityUtil
import kotlinx.android.synthetic.main.activity_text_view_menu.*
import vn.loitp.app.R
import vn.loitp.app.activity.customviews.textview.autofittextview.AutoFitTextViewActivity
import vn.loitp.app.activity.customviews.textview.colortextview.ColorTextViewActivity
import vn.loitp.app.activity.customviews.textview.countdown.CountDownActivity
import vn.loitp.app.activity.customviews.textview.extratextview.ExtraTextViewActivity
import vn.loitp.app.activity.customviews.textview.justifiedtextview.JustifiedTextViewActivity
import vn.loitp.app.activity.customviews.textview.scoretext.ScoreTextViewActivity
import vn.loitp.app.activity.customviews.textview.scrollnumber.ScrollNumberActivity
import vn.loitp.app.activity.customviews.textview.selectabletextView.SelectableTextViewActivity
import vn.loitp.app.activity.customviews.textview.strokedtextview.StrokedTextViewActivity
import vn.loitp.app.activity.customviews.textview.textdecorator.TextDecoratorActivity
import vn.loitp.app.activity.customviews.textview.typewritertextview.TypeWriterTextViewActivity
import vn.loitp.app.activity.customviews.textview.verticalmarqueetextview.VerticalMarqueeTextViewActivity
import vn.loitp.app.activity.customviews.textview.zoomtextview.ZoomTextViewActivity

@LogTag("TextViewMenuActivity")
@IsFullScreen(false)
class TextViewMenuActivity : BaseFontActivity(), OnClickListener {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_text_view_menu
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btAutofitTextView.setOnClickListener(this)
        btScoreText.setOnClickListener(this)
        btCountDown.setOnClickListener(this)
        btColorTextView.setOnClickListener(this)
        btScrollNumber.setOnClickListener(this)
        btSelectableTextView.setOnClickListener(this)
        btZoomTextView.setOnClickListener(this)
        btVerticalMarqueeTextView.setOnClickListener(this)
        btTypeWriterTextView.setOnClickListener(this)
        btTextDecorator.setOnClickListener(this)
        btExtraTextview.setOnClickListener(this)
        btStrokedTextView.setOnClickListener(this)
        btJustifiedTextViewActivity.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val intent: Intent? = when (v) {
            btAutofitTextView -> Intent(this, AutoFitTextViewActivity::class.java)
            btScoreText -> Intent(this, ScoreTextViewActivity::class.java)
            btCountDown -> Intent(this, CountDownActivity::class.java)
            btColorTextView -> Intent(this, ColorTextViewActivity::class.java)
            btScrollNumber -> Intent(this, ScrollNumberActivity::class.java)
            btSelectableTextView -> Intent(this, SelectableTextViewActivity::class.java)
            btZoomTextView -> Intent(this, ZoomTextViewActivity::class.java)
            btVerticalMarqueeTextView -> Intent(this, VerticalMarqueeTextViewActivity::class.java)
            btTypeWriterTextView -> Intent(this, TypeWriterTextViewActivity::class.java)
            btTextDecorator -> Intent(this, TextDecoratorActivity::class.java)
            btExtraTextview -> Intent(this, ExtraTextViewActivity::class.java)
            btStrokedTextView -> Intent(this, StrokedTextViewActivity::class.java)
            btJustifiedTextViewActivity -> Intent(this, JustifiedTextViewActivity::class.java)
            else -> {
                null
            }
        }
        intent.let {
            startActivity(it)
            LActivityUtil.tranIn(this)
        }
    }
}
