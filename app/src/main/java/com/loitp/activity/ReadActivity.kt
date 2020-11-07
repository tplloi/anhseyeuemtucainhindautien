package com.loitp.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.annotation.IsShowAdWhenExit
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFontActivity
import com.core.utilities.LActivityUtil
import com.loitp.R
import com.loitp.fragment.ReadFragment
import com.views.layout.swipeback.SwipeBackLayout
import com.views.setSafeOnClickListener
import com.views.viewpager.viewpagertransformers.ZoomOutSlideTransformer
import kotlinx.android.synthetic.main.activity_read.*

@LogTag("loitppReadActivity")
@IsShowAdWhenExit(true)
class ReadActivity : BaseFontActivity() {

    companion object {
        const val KEY_LIST_DATA = "KEY_LIST_DATA"
        const val KEY_POSITION_CHAP = "KEY_POSITION_CHAP"
    }

    private var currentPosition = 0
    private val listChap = ArrayList<String>()

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_read
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
        setupViewModels()
        setupData()
    }

    private fun setupData() {
        intent?.getIntExtra(KEY_POSITION_CHAP, 0)?.let {
            currentPosition = it
        }
        intent?.getStringArrayListExtra(KEY_LIST_DATA)?.let {
            listChap.addAll(it)
        }
        logD("setupData currentPosition $currentPosition")
        logD("setupData listChap " + BaseApplication.gson.toJson(listChap))
        vp.adapter?.notifyDataSetChanged()
        vp.currentItem = currentPosition
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
        vp.adapter = SlidePagerAdapter(supportFragmentManager)
        vp.setPageTransformer(true, ZoomOutSlideTransformer())
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                setTextChap()
            }

        })
    }

    private fun setTextChap() {
        if (currentPosition < 0 || currentPosition > (listChap.size - 1)) {
            return
        }
        tvChap.text = listChap[currentPosition]
    }

    private fun setupViewModels() {

    }

    private inner class SlidePagerAdapter(fm: FragmentManager)
        : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return ReadFragment.newInstance()
        }

        override fun getCount(): Int {
            return listChap.size
        }
    }
}
