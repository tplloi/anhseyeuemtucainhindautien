package com.loitp.fragment

import android.os.Bundle
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LAppResource
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.viewmodels.MainViewModel
import com.views.LWebView
import kotlinx.android.synthetic.main.frm_chap.*

@LogTag("loitppReadFragment")
class ReadFragment(
        val currentPosition: Int
) : BaseFragment() {

    private var mainViewModel: MainViewModel? = null

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
        logD("setupViews currentPosition $currentPosition")
        webView.callback = object : LWebView.Callback {
            override fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int) {
            }

            override fun onScrollTopToBottom() {
                logD("onScrollTopToBottom")
            }

            override fun onScrollBottomToTop() {
                logD("onScrollBottomToTop")
            }

            override fun onProgressChanged(progress: Int) {
                logD("onProgressChanged $progress")
                pb.progress = progress
                if (progress == 100) {
                    pb.visibility = View.GONE
                } else {
                    pb.visibility = View.VISIBLE
                }
            }

            override fun shouldOverrideUrlLoading(url: String) {
            }
        }
    }

    private fun setupViewModels() {
        mainViewModel = getSelfViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            mvm.contentLiveData.observe(viewLifecycleOwner, { content ->
//                logD("<<<contentLiveData $currentPosition")
                setupData(content = content)
            })
        }
    }

    private fun setupData(content: String) {
        logD("<<<setupData $currentPosition $content")
        val fontSizePx = LAppResource.getDimenValue(R.dimen.txt_small)
        val paddingPx = LAppResource.getDimenValue(R.dimen.padding_small)
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
                fontSizePx = fontSizePx,
                paddingPx = paddingPx
        )
    }

}
