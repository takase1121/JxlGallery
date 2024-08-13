package com.github.takase1121.jxl_gallery

import android.app.Application
import com.google.android.material.color.DynamicColors

class JxlGalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}