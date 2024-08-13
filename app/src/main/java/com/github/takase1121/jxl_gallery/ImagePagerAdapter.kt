package com.github.takase1121.jxl_gallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.dispose
import coil.load
import com.github.panpf.zoomimage.view.zoom.OnViewTapListener
import com.github.takase1121.jxl_gallery.databinding.GalleryViewBinding

class ImagePagerAdapter(private val model: JxlGalleryModel) :
    RecyclerView.Adapter<ImagePagerAdapter.ViewHolder>() {
    class ViewHolder(val binding: GalleryViewBinding) : RecyclerView.ViewHolder(binding.root)

    private val items: List<Uri> = model.uri.value ?: emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = GalleryViewBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loader = model.imageLoader
        holder.binding.photo.dispose()
        holder.binding.photo.load(items[position], loader) {
            crossfade(true)
            listener(onStart = {
                holder.binding.progress.visibility = View.VISIBLE
            }, onSuccess = { _, _ ->
                holder.binding.progress.visibility = View.GONE
            }, onCancel = {
                holder.binding.progress.visibility = View.GONE
            }, onError = { _, error ->
                Toast.makeText(
                    holder.binding.root.context,
                    holder.binding.root.context.getString(R.string.image_load_failed) + error.throwable.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                holder.binding.photo.setImageResource(R.drawable.baseline_error_outline_24)
                holder.binding.progress.visibility = View.GONE
            })
        }
        holder.binding.photo.onViewTapListener = OnViewTapListener { _, _ ->
            val value = model.overlay.value
            if (items.size > 1) model.overlay.value = !(value ?: false)
        }
    }
}