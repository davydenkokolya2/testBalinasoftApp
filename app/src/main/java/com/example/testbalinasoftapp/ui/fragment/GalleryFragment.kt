package com.example.testbalinasoftapp.ui.fragment

import GalleryAdapter
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testbalinasoftapp.data.models.ImageItem
import com.example.testbalinasoftapp.databinding.FragmentGalleryBinding
import com.example.testbalinasoftapp.domain.types.DrawerType
import com.example.testbalinasoftapp.domain.types.FragmentType
import com.example.testbalinasoftapp.domain.types.ToolbarIconState
import com.example.testbalinasoftapp.ui.viewmodel.AuthViewModel
import com.example.testbalinasoftapp.ui.viewmodel.GalleryViewModel
import com.example.testbalinasoftapp.ui.viewmodel.HostViewModel
import com.example.testbalinasoftapp.ui.viewmodel.PhotoDetailViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val hostViewModel: HostViewModel by viewModel()
    private val photoDetailViewModel: PhotoDetailViewModel by viewModel()
    private val galleryViewModel: GalleryViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()

    private lateinit var galleryAdapter: GalleryAdapter

    private var isLoading = false
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        hostViewModel.updateToolbarIcon(ToolbarIconState.MENU)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            hostViewModel.drawerFragment.collect { drawerType ->
                when (drawerType) {
                    DrawerType.OPEN -> {
                        view.setBackgroundColor(Color.parseColor("#B3B3B3B3"))
                        view.alpha = 0.3f
                    }

                    DrawerType.CLOSE -> {
                        view.setBackgroundColor(Color.alpha(Color.WHITE))
                        view.alpha = 1f
                    }
                }
            }
        }
        binding.fabAdd.setOnClickListener {
            hostViewModel.switchFragment(FragmentType.CAMERA)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        galleryAdapter = GalleryAdapter(mutableListOf(), { selectedItem ->
            hostViewModel.switchFragment(FragmentType.PHOTO_DETAIL)
            photoDetailViewModel.updateCurrentImage(selectedItem)
        }, { item -> showDeleteConfirmationDialog(item) })

        binding.recyclerView.adapter = galleryAdapter

        galleryViewModel.images.observe(viewLifecycleOwner) { items ->
            items?.let {
                galleryAdapter.updateItems(it)
                isLoading = false
            }
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                if (!isLoading && layoutManager.findLastVisibleItemPosition() == galleryAdapter.itemCount - 1) {
                    isLoading = true
                    currentPage++
                    loadMoreImages()
                }
            }
        })

        loadMoreImages()
    }

    private fun loadMoreImages() {
        lifecycleScope.launch {
            galleryViewModel.fetchImages(authViewModel.token.value)
        }
    }

    private fun showDeleteConfirmationDialog(item: ImageItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Подтверждение удаления")
            .setMessage("Вы уверены, что хотите удалить это фото?")
            .setPositiveButton("Да") { _, _ ->
                lifecycleScope.launch {
                    galleryViewModel.deleteImage(
                        item.id,
                        authViewModel.token.value
                    )
                }
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
