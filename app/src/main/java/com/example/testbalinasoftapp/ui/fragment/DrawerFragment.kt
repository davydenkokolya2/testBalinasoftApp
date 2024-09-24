package com.example.testbalinasoftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.testbalinasoftapp.databinding.FragmentDrawerBinding
import com.example.testbalinasoftapp.domain.types.DrawerType
import com.example.testbalinasoftapp.domain.types.FragmentType
import com.example.testbalinasoftapp.ui.viewmodel.AuthViewModel
import com.example.testbalinasoftapp.ui.viewmodel.DrawerViewModel
import com.example.testbalinasoftapp.ui.viewmodel.HostViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DrawerFragment : Fragment() {

    private var _binding: FragmentDrawerBinding? = null
    private val binding get() = _binding!!

    private val hostViewModel: HostViewModel by viewModel()
    private val drawerViewModel: DrawerViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrawerBinding.inflate(inflater, container, false)

        authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { loginResponse ->
                lifecycleScope.launch {
                    drawerViewModel.loadUserName(loginResponse.data.userId)
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            drawerViewModel.userName.collect { username ->
                binding.username.text = username
            }
        }
        binding.mapSection.setOnClickListener {
            hostViewModel.switchFragment(FragmentType.MAP)
            hostViewModel.switchDrawerFragment(DrawerType.CLOSE)
        }

        binding.photosSection.setOnClickListener {
            hostViewModel.switchFragment(FragmentType.GALLERY)
            hostViewModel.switchDrawerFragment(DrawerType.CLOSE)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
