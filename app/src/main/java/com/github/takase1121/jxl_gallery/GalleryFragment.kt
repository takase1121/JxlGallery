package com.github.takase1121.jxl_gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.github.takase1121.jxl_gallery.databinding.GalleryFragmentBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {
    private lateinit var binding: GalleryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = GalleryFragmentBinding.inflate(inflater, container, false)
        binding.pager.offscreenPageLimit = 1
        val model: JxlGalleryModel by activityViewModels()

        model.uri.observe(viewLifecycleOwner) {
            binding.pager.adapter = ImagePagerAdapter(model)
            binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.seekbar.value = (position + 1).toFloat()
                }
            })
            val list = model.uri.value ?: emptyList()
            if (list.size > 1) {
                binding.seekbar.valueFrom = 1F
                binding.seekbar.valueTo = list.size.toFloat()
                binding.seekbar.stepSize = 1F
                binding.seekbar.value = 1F
            }
            var debounceSeek: Job? = null
            binding.seekbar.addOnChangeListener { _, value, _ ->
                debounceSeek?.cancel()
                debounceSeek = lifecycleScope.launch {
                    delay(100)
                    binding.pager.currentItem = value.toInt() - 1
                }
            }
        }
        model.overlay.observe(viewLifecycleOwner) { value ->
            if (value) {
                binding.seek.animate().alpha(1.0f)
                binding.seek.visibility = View.VISIBLE
            } else {
                binding.seek.animate().alpha(0.0f).withEndAction {
                    binding.seek.visibility = View.GONE
                }
            }
        }
        return binding.root
    }
}