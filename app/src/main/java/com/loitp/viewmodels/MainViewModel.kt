package com.loitp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.annotation.LogTag
import com.core.base.BaseViewModel
import com.core.utilities.LStoreUtil
import com.loitp.R
import com.service.model.UserTest
import kotlinx.coroutines.launch

@LogTag("MainViewModel")
class MainViewModel : BaseViewModel() {

    val listChapLiveData: MutableLiveData<List<String>> = MutableLiveData()
    val contentLiveData: MutableLiveData<String> = MutableLiveData()

    fun loadListChap() {
        ioScope.launch {
            showLoading(true)

            val string = LStoreUtil.readTxtFromAsset(assetFile = "db.sqlite")
//            logD("loadListChap string $string")
            val listChap = string.split("#")
            listChapLiveData.postValue(listChap)

            showLoading(false)
        }
    }

    fun loadContain(position: Int) {
        ioScope.launch {
            val content = LStoreUtil.readTxtFromAsset(assetFile = "asyetcndt$position.db")
            contentLiveData.postValue(content)
        }
    }
}
