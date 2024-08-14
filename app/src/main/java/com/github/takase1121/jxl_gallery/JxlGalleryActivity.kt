package com.github.takase1121.jxl_gallery

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentActivity
import com.github.takase1121.jxl_gallery.databinding.JxlGalleryActivityBinding

class JxlGalleryActivity : FragmentActivity() {
    private lateinit var binding: JxlGalleryActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = JxlGalleryActivityBinding.inflate(layoutInflater)
        val model: JxlGalleryModel by viewModels()
        model.imageList.observe(this) { list ->
            if (list.isEmpty()) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, FileSelectorFragment())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, GalleryFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
        setContentView(binding.root)
        hideSystemBars()
    }

    // Enable immersive mode.
    private fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}