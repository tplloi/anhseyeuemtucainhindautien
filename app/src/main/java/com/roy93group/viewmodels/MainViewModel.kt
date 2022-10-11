package com.roy93group.viewmodels

import androidx.lifecycle.MutableLiveData
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseViewModel
import com.loitpcore.core.utilities.LStoreUtil
import kotlinx.coroutines.launch

@LogTag("MainViewModel")
class MainViewModel : BaseViewModel() {

    val listChapLiveData: MutableLiveData<List<String>> = MutableLiveData()
    val contentLiveData: MutableLiveData<String> = MutableLiveData()

    fun loadListChap() {
        ioScope.launch {
            showLoading(true)

            val string = LStoreUtil.readTxtFromAsset(assetFile = "db.sqlite")
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
