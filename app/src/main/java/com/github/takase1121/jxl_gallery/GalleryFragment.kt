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

        model.imageList.observe(viewLifecycleOwner) {
            binding.pager.adapter = ImagePagerAdapter(model)
            binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    model.setPosition(position + 1)
                }
            })

            binding.seekbar.valueFrom = 1F
            binding.seekbar.stepSize = 1F
            binding.seekbar.value = 1F
            val list = model.imageList.value ?: emptyList()
            if (list.size > 1) {
                binding.seekbar.valueFrom = 1F
                binding.seekbar.valueTo = list.size.toFloat()
            }

            var debounceSeek: Job? = null
            binding.seekbar.addOnChangeListener { _, value, _ ->
                debounceSeek?.cancel()
                debounceSeek = lifecycleScope.launch {
                    delay(100)
                    model.setPosition(value.toInt())
                }
            }

            binding.total.text = list.size.toString()
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
        model.position.observe(viewLifecycleOwner) { position ->
            if (binding.seekbar.value.toInt() != position)
                binding.seekbar.value = position.toFloat()
            if (binding.pager.currentItem != (position - 1))
                binding.pager.currentItem = position - 1
            binding.current.text = position.toString()
        }
        return binding.root
    }
}