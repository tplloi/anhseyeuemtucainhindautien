package com.roy93group.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.core.view.isVisible
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFragment
import com.loitpcore.core.utilities.LAppResource
import com.loitpcore.core.utilities.LPrefUtil
import com.loitpcore.core.utilities.LUIUtil
import com.loitpcore.views.LWebViewAdblock
import com.roy93group.R
import com.roy93group.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.frm_chap.*

/**
 * Created by Loitp on 2022.10.11
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
@LogTag("ReadFrm")
class ReadFrm(
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

        mainViewModel?.loadChapIndex(position = currentPosition)
    }

    private fun setupViews() {
//        logD("setupViews currentPosition $currentPosition")
        wv.callback = object : LWebViewAdblock.Callback {
            override fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int) {
//                logD("onScroll $t")
                onScroll = t
            }

            override fun onScrollTopToBottom() {
//                logD("onScrollTopToBottom")
            }

            override fun onScrollBottomToTop() {
//                logD("onScrollBottomToTop")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
            }

            override fun onProgressChanged(progress: Int) {
//                logD("onProgressChanged $progress")
                pb?.let {
                    it.progress = progress
                    if (it.progress == 100) {
                        it.isVisible = false
                        wv.isVisible = true
                    } else {
                        it.isVisible = true
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
        wv.loadDataString(
            bodyContent = content,
            backgroundColor = backgroundColor,
            textColor = textColor,
            textAlign = "justify",
//                fontSizePx = LAppResource.getDimenValue(R.dimen.txt_8),
            paddingPx = paddingPx
        )
        wv.setTextSize(sizePercent = LPrefUtil.getTextSizePercentEpub())
    }

    @Suppress("DEPRECATION")
    fun zoomIn() {
        logD("zoomIn")
        val settings = wv.settings
        val currentApiVersion = Build.VERSION.SDK_INT
        if (currentApiVersion <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            settings.textSize = WebSettings.TextSize.LARGER
        } else {
            var size = (settings.textZoom * 1.1).toInt()
            if (size > 250) {
                size = 250
            }
            LPrefUtil.setTextSizePercentEpub(value = size)
            wv.setTextSize(sizePercent = size)
        }
    }

    @Suppress("DEPRECATION")
    fun zoomOut() {
        logD("zoomOut")
        val settings = wv.settings
        val currentAiVersion = Build.VERSION.SDK_INT
        if (currentAiVersion <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            settings.textSize = WebSettings.TextSize.SMALLEST
        } else {
            var size = (settings.textZoom / 1.1).toInt()
            if (size < 50) {
                size = 50
            }
            LPrefUtil.setTextSizePercentEpub(value = size)
            wv.setTextSize(sizePercent = size)
        }
    }

    fun scrollToPosition(scroll: Int) {
        logD(">>>scrollToPosition scroll $scroll")
        wv.scrollTo(0, scroll)
    }

}
