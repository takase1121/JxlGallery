package com.github.takase1121.jxl_gallery

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.dispose
import coil.load
import com.github.panpf.zoomimage.view.zoom.OnViewTapListener
import com.github.takase1121.jxl_gallery.databinding.GalleryViewBinding

class ImagePagerAdapter(private val model: JxlGalleryModel) :
    RecyclerView.Adapter<ImagePagerAdapter.ViewHolder>() {
    class ViewHolder(private var loader: ImageLoader, val binding: GalleryViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var uri: Uri? = null

        fun load() {
            if (uri == null) return
            binding.photo.dispose()
            binding.photo.setImageDrawable(null)
            binding.photo.load(uri, loader) {
                crossfade(true)
                listener(onStart = {
                    Log.v("JxlGallery", "Loading image with URI $uri")
                    binding.progress.visibility = View.VISIBLE
                }, onSuccess = { _, _ ->
                    binding.progress.visibility = View.GONE
                }, onCancel = {
                    binding.progress.visibility = View.GONE
                }, onError = { _, error ->
                    Toast.makeText(
                        binding.root.context,
                        binding.root.context.getString(R.string.image_load_failed) + error.throwable.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.photo.setImageResource(R.drawable.baseline_error_outline_24)
                    binding.progress.visibility = View.GONE
                })
            }
        }
    }

    private val items: List<Uri> = model.imageList.value ?: emptyList()
    private var first: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = GalleryViewBinding.inflate(inflater, parent, false)
        return ViewHolder(model.imageLoader, view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.uri = items[position]
        holder.binding.photo.onViewTapListener = OnViewTapListener { _, _ ->
            val value = model.overlay.value
            if (items.size > 1) model.setOverlay(!(value ?: false))
        }
        if (first) {
            holder.load()
            first = false
        }
    }
}