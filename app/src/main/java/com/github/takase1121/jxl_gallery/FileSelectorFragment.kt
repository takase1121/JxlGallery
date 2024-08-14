package com.github.takase1121.jxl_gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.takase1121.jxl_gallery.databinding.FileSelectorFragmentBinding

class FileSelectorFragment : Fragment() {
    private val model: JxlGalleryModel by activityViewModels()
    private lateinit var binding: FileSelectorFragmentBinding

    private val openFileIntent =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uri ->
            if (uri.isEmpty()) {
                Toast.makeText(requireContext(), "No files selected", Toast.LENGTH_SHORT).show()
            } else {
                model.setImageList(uri)
            }
        }

    private val openDirIntent =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            if (uri != null) {
                requireContext().contentResolver.takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                val tree = DocumentFile.fromTreeUri(requireContext(), uri)
                val list = tree?.let {
                    it.listFiles().filter { f -> f.isFile && f.type?.startsWith("image/") == true }
                        .map { f -> f.uri }
                } ?: emptyList()

                if (list.isEmpty()) {
                    Toast.makeText(requireContext(), "No files selected", Toast.LENGTH_SHORT).show()
                } else {
                    model.setImageList(list)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FileSelectorFragmentBinding.inflate(inflater, container, false)
        binding.openfile.setOnClickListener { _ ->
            openFileIntent.launch(arrayOf("image/*"))
        }
        binding.opendir.setOnClickListener { _ ->
            openDirIntent.launch(null)
        }
        return binding.root
    }
}