package com.example.testbalinasoftapp.ui.fragment

import GalleryAdapter
import GalleryItem
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testbalinasoftapp.R
import com.example.testbalinasoftapp.databinding.FragmentGalleryBinding
import com.example.testbalinasoftapp.domain.DrawerType
import com.example.testbalinasoftapp.domain.FragmentType
import com.example.testbalinasoftapp.domain.ToolbarIconState
import com.example.testbalinasoftapp.ui.viewmodel.HostViewModel
import com.example.testbalinasoftapp.ui.viewmodel.PhotoDetailViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val hostViewModel: HostViewModel by viewModel()
    private val photoDetailViewModel: PhotoDetailViewModel by viewModel()

    private lateinit var galleryAdapter: GalleryAdapter
    private var isLoading = false

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

        val initialItems = generateDummyGalleryItems(10)

        // Инициализация адаптера
        galleryAdapter = GalleryAdapter(initialItems) { selectedItem ->
            // Переход на экран PhotoDetailFragment
            hostViewModel.switchFragment(FragmentType.PHOTO_DETAIL)
            photoDetailViewModel.updateCurrentImage(
                GalleryItem(
                    selectedItem.imageUrl,
                    selectedItem.date
                )
            )
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3) // 3 элемента в ряд
            adapter = galleryAdapter
        }

        // Инициализация слушателя для бесконечного скроллинга
        setupRecyclerViewScrollListener()

        // Загружаем начальные данные
        loadMoreItems()
    }

    private fun setupRecyclerViewScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Условие для "ленивой" подгрузки данных
                if (!isLoading) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadMoreItems()  // Подгружаем новые элементы
                    }
                }
            }
        })
    }

    private fun loadMoreItems() {
        isLoading = true

        // Имитация задержки для загрузки данных (можно заменить на вызов API)
        binding.recyclerView.postDelayed({
            val newItems = generateDummyGalleryItems(10)  // Загружаем 10 новых элементов
            galleryAdapter.addItems(newItems)
            isLoading = false
        }, 500)
    }

    private fun generateDummyGalleryItems(count: Int): MutableList<GalleryItem> {
        val items = mutableListOf<GalleryItem>()

        for (i in 0 until 0 + count) {
            items.add(
                GalleryItem(
                    imageUrl = "https://via.placeholder.com/150",  // Заглушка для изображения
                    date = "Date $i"
                )
            )
        }
        return items
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
