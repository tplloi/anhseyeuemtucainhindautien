package com.loitp.viewmodels

import androidx.lifecycle.MutableLiveData
import com.annotation.LogTag
import com.core.base.BaseViewModel
import com.core.utilities.LStoreUtil
import com.service.livedata.SingleLiveEvent
import kotlinx.coroutines.launch

@LogTag("MainViewModel")
class MainViewModel : BaseViewModel() {

    val listChapLiveData: MutableLiveData<List<String>> = MutableLiveData()
    val contentLiveData: MutableLiveData<Map<Int, String>> = MutableLiveData()

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
            val map = HashMap<Int, String>()
            map[position] = content
            contentLiveData.postValue(map)
        }
    }
}
