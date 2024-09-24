package com.example.testbalinasoftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.testbalinasoftapp.databinding.FragmentPhotoDetailBinding
import com.example.testbalinasoftapp.domain.Unix.convertUnixToDateWithTime
import com.example.testbalinasoftapp.domain.types.ToolbarIconState
import com.example.testbalinasoftapp.ui.viewmodel.HostViewModel
import com.example.testbalinasoftapp.ui.viewmodel.PhotoDetailViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotoDetailFragment : Fragment() {

    private var _binding: FragmentPhotoDetailBinding? = null
    private val binding get() = _binding!!

    private val photoDetailViewModel: PhotoDetailViewModel by viewModel()
    private val hostViewModel: HostViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoDetailBinding.inflate(inflater, container, false)
        hostViewModel.updateToolbarIcon(ToolbarIconState.BACK)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            photoDetailViewModel.currentImage.collect {item ->
                if (item != null) {
                    Glide.with(binding.ivAvatar.context)
                        .load(item.url)
                        .into(binding.ivAvatar)

                    binding.tvDate.text = convertUnixToDateWithTime(item.date)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
