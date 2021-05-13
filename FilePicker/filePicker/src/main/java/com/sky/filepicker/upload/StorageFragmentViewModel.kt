package com.sky.filepicker.upload

import android.arch.lifecycle.ViewModel
import com.sky.filepicker.model.FileBean

class StorageFragmentViewModel : ViewModel() {

    var path: String? = null

    val storageList = ArrayList<FileBean>()
}