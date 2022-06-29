package com.loitp.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LAppResource
import com.core.utilities.LPrefUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.viewmodels.MainViewModel
import com.views.LWebViewAdblock
import kotlinx.android.synthetic.main.frm_chap.*

@LogTag("ReadFragment")
class ReadFragment(
    val currentPosition: Int
) : BaseFragment() {

    private var mainViewModel: MainViewModel? = null
    var onScroll = 0

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_chap
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()

        mainViewModel?.loadContain(position = currentPosition)
    }

    private fun setupViews() {
//        logD("setupViews currentPosition $currentPosition")
        webView.callback = object : LWebViewAdblock.Callback {
            override fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int) {
                logD("onScroll $t")
                onScroll = t
            }

            override fun onScrollTopToBottom() {
//                logD("onScrollTopToBottom")
            }

            override fun onScrollBottomToTop() {
//                logD("onScrollBottomToTop")
            }

            override fun onProgressChanged(progress: Int) {
//                logD("onProgressChanged $progress")
                //warning show check null here for better
                pb?.let {
                    it.progress = progress
                    if (it.progress == 100) {
                        it.visibility = View.GONE
                        webView?.visibility = View.VISIBLE
                    } else {
                        it.visibility = View.VISIBLE
                    }
                }
            }

            override fun shouldOverrideUrlLoading(url: String) {
            }
        }
    }

    private fun setupViewModels() {
        mainViewModel = getSelfViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            mvm.contentLiveData.observe(viewLifecycleOwner) { content ->
//                logD("<<<contentLiveData $currentPosition")
                setupData(content = content)
            }
        }
    }

    private fun setupData(content: String) {
//        logD("<<<setupData $currentPosition $content")
        val paddingPx = LAppResource.getDimenValue(R.dimen.margin_padding_small)
        val backgroundColor: String
        val textColor: String
        if (LUIUtil.isDarkTheme()) {
            backgroundColor = "black"
            textColor = "white"
        } else {
            backgroundColor = "white"
            textColor = "black"
        }
        webView.loadDataString(
            bodyContent = content,
            backgroundColor = backgroundColor,
            textColor = textColor,
            textAlign = "justify",
//                fontSizePx = LAppResource.getDimenValue(R.dimen.txt_8),
            paddingPx = paddingPx
        )
        webView.setTextSize(sizePercent = LPrefUtil.getTextSizePercentEpub())
    }

    @Suppress("DEPRECATION")
    fun zoomIn() {
        logD("zoomIn")
        val settings = webView.settings
        val currentApiVersion = Build.VERSION.SDK_INT
        if (currentApiVersion <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            settings.textSize = WebSettings.TextSize.LARGER
        } else {
            var size = (settings.textZoom * 1.1).toInt()
            if (size > 250) {
                size = 250
            }
            LPrefUtil.setTextSizePercentEpub(value = size)
            webView.setTextSize(sizePercent = size)
        }
    }

    @Suppress("DEPRECATION")
    fun zoomOut() {
        logD("zoomOut")
        val settings = webView.settings
        val currentAiVersion = Build.VERSION.SDK_INT
        if (currentAiVersion <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            settings.textSize = WebSettings.TextSize.SMALLEST
        } else {
            var size = (settings.textZoom / 1.1).toInt()
            if (size < 50) {
                size = 50
            }
            LPrefUtil.setTextSizePercentEpub(value = size)
            webView.setTextSize(sizePercent = size)
        }
    }

    fun scrollToPosition(scroll: Int) {
        logD(">>>scrollToPosition scroll $scroll")
        webView.scrollTo(0, scroll)
    }

}
