package vn.loitp.a.cv.layout.basket

import android.os.Bundle
import androidx.core.view.isVisible
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.setDelay
import com.loitp.core.ext.setSafeOnClickListenerElastic
import com.yonder.basketlayout.BasketLayoutViewListener
import kotlinx.android.synthetic.main.a_basket_layout.*
import vn.loitp.R

@LogTag("BasketLayoutActivity")
@IsFullScreen(false)
class BasketLayoutActivityFont : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_basket_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.isVisible = false
            this.tvTitle?.text = BasketLayoutActivityFont::class.java.simpleName
        }
        basketView.apply {
            setBasketQuantity(quantity = 2)
            setMaxQuantity(maxQuantity = 5)
            setBasketLayoutListener(object : BasketLayoutViewListener {
                override fun onExceedMaxQuantity(quantity: Int) {
                    showShortInformation("Exceed max quantity: $quantity")
                }

                override fun onClickDecreaseQuantity(quantity: Int) {
                    showShortInformation("Decrease quantity to $quantity")
                    setDelay(1000) {
                        setBasketQuantity(quantity)
                    }
                }

                override fun onClickIncreaseQuantity(quantity: Int) {
                    showShortInformation("Increase quantity to $quantity")
                    setDelay(1000) {
                        setBasketQuantity(quantity)
                    }
                }

                override fun onClickTrash() {
                    showShortInformation("on Click Trash Button")
                }
            })
        }
    }
}
