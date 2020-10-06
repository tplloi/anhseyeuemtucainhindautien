package com.loitp.activity

import android.os.Bundle
import android.view.View
import com.annotation.IsShowAdWhenExit
import com.annotation.LayoutId
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.utilities.LActivityUtil
import com.loitp.R
import com.views.layout.swipeback.SwipeBackLayout
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_read.*

@LayoutId(R.layout.activity_read)
@LogTag("loitppReadActivity")
@IsShowAdWhenExit(true)
class ReadActivity : BaseFontActivity() {

    companion object {
        const val KEY_POSITION_CHAP = "KEY_POSITION_CHAP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
        setupViewModels()

        val position = intent?.getIntExtra(KEY_POSITION_CHAP, 0)
        logD("onCreate position $position")
    }

    private fun setupData() {

    }

    private fun setupViews() {
        swipeBackLayout.setSwipeBackListener(object : SwipeBackLayout.OnSwipeBackListener {
            override fun onViewPositionChanged(mView: View, swipeBackFraction: Float, SWIPE_BACK_FACTOR: Float) {
            }

            override fun onViewSwipeFinished(mView: View, isEnd: Boolean) {
                if (isEnd) {
                    finish()
                    LActivityUtil.transActivityNoAnimation(context = this@ReadActivity)
                }
            }
        })
        ivBack.setSafeOnClickListener {
            onBackPressed()
        }
    }

    private fun setupViewModels() {

    }
}
