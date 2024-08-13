package com.github.takase1121.jxl_gallery

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.awxkee.jxlcoder.coil.AnimatedJxlDecoder

class ImageLoaderSingleton private constructor(context: Context) {
    val imageLoader = ImageLoader.Builder(context)
        .crossfade(true)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
            add(AnimatedJxlDecoder.Factory(context))
        }
        .build()

    companion object {
        @Volatile
        private var instance: ImageLoaderSingleton? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: ImageLoaderSingleton(context).also { instance = it }
            }
    }
}