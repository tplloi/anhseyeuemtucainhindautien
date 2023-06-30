package com.roy93group.app

import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseApplication
import com.loitpcore.core.common.Constants
import com.loitpcore.core.utilities.LPrefUtil
import com.loitpcore.core.utilities.LUIUtil
import com.loitpcore.data.ActivityData

/**
 * Created by Loitp on 2022.10.11
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */

//TODO ad

//done
//pkg name 2022.10.11
//valid gg drive 2022.10.11
//keystore
//ic_launcher
//isFullData
//leak canary

@LogTag("LApplication")
class LApplication : BaseApplication() {

    companion object {
        fun isFullData(): Boolean {
            val app = LPrefUtil.getGGAppSetting()
            return app.config?.isFullData == true
        }
    }

    override fun onCreate() {
        super.onCreate()

        setupApp()
    }

    private fun setupApp() {
        ActivityData.instance.type = Constants.TYPE_ACTIVITY_TRANSITION_SLIDE_LEFT
        LUIUtil.fontForAll = Constants.FONT_PATH
    }
}
