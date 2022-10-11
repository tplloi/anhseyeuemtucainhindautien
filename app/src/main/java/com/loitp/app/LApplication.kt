package com.loitp.app

import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseApplication
import com.loitpcore.core.common.Constants
import com.loitpcore.core.utilities.LUIUtil
import com.loitpcore.data.ActivityData

//TODO keystore
//TODO ic_laucher
//TODO pkg name
//TODO valid gg drive
//TODO pkg name

//done

@LogTag("LApplication")
class LApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        //config activity transition default
        ActivityData.instance.type = Constants.TYPE_ACTIVITY_TRANSITION_SLIDE_LEFT

        //config font
        LUIUtil.fontForAll = Constants.FONT_PATH
    }
}
