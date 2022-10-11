package com.roy93group.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.daimajia.androidanimations.library.Techniques
import com.roy93group.R
import com.roy93group.common.AppConstant
import com.roy93group.fragment.ReadFragment
import com.loitpcore.annotation.IsSwipeActivity
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFontActivity
import com.loitpcore.core.utilities.LActivityUtil
import com.loitpcore.core.utilities.LAnimationUtil
import com.loitpcore.core.utilities.LSharedPrefsUtil
import com.loitpcore.views.layout.swipeBack.SwipeBackLayout
import com.loitpcore.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_read.*

/**
 * Created by Loitp on 2022.10.11
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
@LogTag("ReadActivity")
@IsSwipeActivity(true)
class ReadActivity : BaseFontActivity() {

    companion object {
        const val KEY_LIST_DATA = "KEY_LIST_DATA"
        const val KEY_POSITION_CHAP = "KEY_POSITION_CHAP"
        const val KEY_SCROLL = "KEY_SCROLL"
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
//        logD("setupData currentPosition $currentPosition")
//        logD("setupData listChap " + BaseApplication.gson.toJson(listChap))
        vp.adapter?.notifyDataSetChanged()
        vp.currentItem = currentPosition
        if (currentPosition == 0) {
            setTextChap()
        }

        //book mark scroll
        intent?.getIntExtra(KEY_SCROLL, 0)?.let { scroll ->
            logD("setupData scroll $scroll")
            vp.post {
                val readFragment = vp.adapter?.instantiateItem(vp, vp.currentItem)
                if (readFragment is ReadFragment) {
                    logD("readFragment is ReadFragment setupData scroll $scroll")
                    readFragment.scrollToPosition(scroll = scroll)
                }
            }
        }
    }

    private fun setupViews() {
        swipeBackLayout.setSwipeBackListener(object : SwipeBackLayout.OnSwipeBackListener {
            override fun onViewPositionChanged(mView: View?, swipeBackFraction: Float, swipeBackFactor: Float) {
            }

            override fun onViewSwipeFinished(mView: View?, isEnd: Boolean) {
                if (isEnd) {
                    finish()
                    LActivityUtil.transActivityNoAnimation(context = this@ReadActivity)
                }
            }
        })
        ivBack.setSafeOnClickListener {
            onBackPressed()
        }
        btZoomIn.setOnClickListener {
            handleZoomIn()
        }
        btZoomOut.setOnClickListener {
            handleZoomOut()
        }
        vp.adapter = SlidePagerAdapter(supportFragmentManager)
//        vp.setPageTransformer(true, ZoomOutSlideTransformer())
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
//        logD("setTextChap currentPosition $currentPosition")
        if (currentPosition < 0 || currentPosition > (listChap.size - 1)) {
            return
        }
        tvChap.text = listChap[currentPosition]
    }

    private fun setupViewModels() {
        //do nothing
    }

    private inner class SlidePagerAdapter(fm: FragmentManager)
        : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return ReadFragment(currentPosition = position)
        }

        override fun getCount(): Int {
            return listChap.size
        }
    }

    private fun handleZoomIn() {
        LAnimationUtil.play(view = btZoomIn, techniques = Techniques.Pulse)
        vp.adapter?.let { adapter ->
            try {
                val readFragment = adapter.instantiateItem(vp, vp.currentItem)
                if (readFragment is ReadFragment) {
                    readFragment.zoomIn()
                }

                val readFragmentNext = adapter.instantiateItem(vp, vp.currentItem + 1)
                if (readFragmentNext is ReadFragment) {
                    readFragmentNext.zoomIn()
                }

                val readFragmentPrev = adapter.instantiateItem(vp, vp.currentItem - 1)
                if (readFragmentPrev is ReadFragment) {
                    readFragmentPrev.zoomIn()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleZoomOut() {
        LAnimationUtil.play(view = btZoomOut, techniques = Techniques.Pulse)
        vp.adapter?.let { adapter ->
            try {
                val readFragment = adapter.instantiateItem(vp, vp.currentItem)
                if (readFragment is ReadFragment) {
                    readFragment.zoomOut()
                }

                val readFragmentNext = adapter.instantiateItem(vp, vp.currentItem + 1)
                if (readFragmentNext is ReadFragment) {
                    readFragmentNext.zoomOut()
                }

                val readFragmentPrev = adapter.instantiateItem(vp, vp.currentItem - 1)
                if (readFragmentPrev is ReadFragment) {
                    readFragmentPrev.zoomOut()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onBackPressed() {
        val readFragment = vp.adapter?.instantiateItem(vp, vp.currentItem)
        if (readFragment is ReadFragment) {
            val onScroll = readFragment.onScroll
//            logD("onBackPressed onScroll $onScroll, currentItem: " + vp.currentItem)
            LSharedPrefsUtil.instance.putInt(AppConstant.KEY_CURRENT_POSITION, vp.currentItem)
            LSharedPrefsUtil.instance.putInt(AppConstant.KEY_SCROLL, onScroll)
            showShortInformation(getString(R.string.book_mark_success))
        }
        super.onBackPressed()
    }
}
