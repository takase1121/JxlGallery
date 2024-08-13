package com.github.takase1121.jxl_gallery

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JxlGalleryModel: ViewModel() {
    val overlay = MutableLiveData(false)
    val uri = MutableLiveData<List<Uri>>(emptyList())
}