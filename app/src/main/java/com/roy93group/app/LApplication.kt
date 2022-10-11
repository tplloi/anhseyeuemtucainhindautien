package com.roy93group.app

import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseApplication
import com.loitpcore.core.common.Constants
import com.loitpcore.core.utilities.LUIUtil
import com.loitpcore.data.ActivityData

//TODO keystore
//TODO ic_laucher

//done
//pkg name 2022.10.11
//valid gg drive 2022.10.11

@LogTag("LApplication")
class LApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        setupApp()
    }

    private fun setupApp() {
        ActivityData.instance.type = Constants.TYPE_ACTIVITY_TRANSITION_SLIDE_LEFT
        LUIUtil.fontForAll = Constants.FONT_PATH
    }
}
