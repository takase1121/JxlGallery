package com.github.takase1121.jxl_gallery

import android.app.Application
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.memory.MemoryCache
import com.awxkee.jxlcoder.coil.AnimatedJxlDecoder

class JxlGalleryModel(application: Application) : AndroidViewModel(application) {
    private val _overlay = MutableLiveData(false)
    private val _imageList = MutableLiveData<List<Uri>>(emptyList())
    private val _position = MutableLiveData(1)

    val overlay: LiveData<Boolean> get() = _overlay
    val imageList: LiveData<List<Uri>> get() = _imageList
    val position: LiveData<Int> get() = _position

    val imageLoader = ImageLoader.Builder(application)
        .memoryCache { MemoryCache.Builder(application).maxSizePercent(0.25).build() }
        .crossfade(true).components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
            add(AnimatedJxlDecoder.Factory(application))
        }.build()

    fun setOverlay(value: Boolean) {
        _overlay.value = value
    }

    fun setImageList(value: List<Uri>) {
        _imageList.value = value
        _position.value = 1
    }

    fun setPosition(value: Int) {
        _position.value = value
    }
}