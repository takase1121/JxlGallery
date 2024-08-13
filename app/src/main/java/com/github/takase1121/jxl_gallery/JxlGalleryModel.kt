package com.github.takase1121.jxl_gallery

import android.app.Application
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.memory.MemoryCache
import com.awxkee.jxlcoder.coil.AnimatedJxlDecoder

class JxlGalleryModel(application: Application) : AndroidViewModel(application) {
    val overlay = MutableLiveData(false)
    val uri = MutableLiveData<List<Uri>>(emptyList())
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
}