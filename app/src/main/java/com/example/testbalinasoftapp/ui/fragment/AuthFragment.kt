package com.example.testbalinasoftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testbalinasoftapp.databinding.FragmentAuthBinding
import com.example.testbalinasoftapp.domain.FragmentType
import com.example.testbalinasoftapp.domain.ToolbarIconState
import com.example.testbalinasoftapp.ui.viewmodel.HostViewModel
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : Fragment() {

    private val hostViewModel: HostViewModel by viewModel()

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        val editUsername = binding.editUsername
        val editPassword = binding.editPassword
        val editConfirmPassword = binding.editConfirmPassword
        val buttonSubmit = binding.buttonSubmit
        val tabLayout = binding.tabLayout


        // Добавление вкладок в TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Login"))
        tabLayout.addTab(tabLayout.newTab().setText("Register"))

        // Установка обработчика для вкладок
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> { // Login
                        editConfirmPassword.visibility = View.GONE
                        buttonSubmit.text = "Login"
                    }

                    1 -> { // Register
                        editConfirmPassword.visibility = View.VISIBLE
                        buttonSubmit.text = "Register"
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Обработка нажатия кнопки
        buttonSubmit.setOnClickListener {

            // Реализуйте логику для обработки входа или регистрации
            if (tabLayout.selectedTabPosition == 0) {
                hostViewModel.switchFragment(FragmentType.GALLERY)
            } else {
                hostViewModel.switchFragment(FragmentType.GALLERY)

            }
        }
    }
}
