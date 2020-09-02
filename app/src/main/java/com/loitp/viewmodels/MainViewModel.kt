package com.loitp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.core.base.BaseViewModel
import com.service.model.UserTest

class MainViewModel(application: Application) : BaseViewModel(application) {
    private val logTag = javaClass.simpleName

    val userTestListLiveData: MutableLiveData<ArrayList<UserTest>?> = MutableLiveData()

    fun addUserList(userTestList: ArrayList<UserTest>, isRefresh: Boolean?) {
        //LLog.d(TAG, "addUserList size: ${userTestList.size}, isRefresh: $isRefresh")
        var currentUserTestList = userTestListLiveData.value
        if (isRefresh == true) {
            currentUserTestList?.clear()
        }
        if (currentUserTestList == null) {
            currentUserTestList = ArrayList()
        }
        currentUserTestList.addAll(userTestList)
        //LLog.d(TAG, "addUserList currentUserTestList " + LApplication.gson.toJson(currentUserTestList))
        userTestListLiveData.post(currentUserTestList)
    }
}