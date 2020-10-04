package com.loitp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.core.base.BaseViewModel
import com.core.utilities.LStoreUtil
import com.service.model.UserTest
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {
    private val logTag = "loitpp" + javaClass.simpleName

    val listChapLiveData: MutableLiveData<List<String>> = MutableLiveData()

    fun loadListChap(context: Context) {
        ioScope.launch {
            showLoading(true)

            val string = LStoreUtil.readTxtFromAsset(assetFile = "db.sqlite")
            Log.d(logTag, "loadListChap string $string")
            val listChap = string.split("#")
            listChapLiveData.postValue(listChap)

            showLoading(false)
        }
    }
}
