package com.example.testbalinasoftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.testbalinasoftapp.data.models.LoginResponse
import com.example.testbalinasoftapp.databinding.FragmentAuthBinding
import com.example.testbalinasoftapp.domain.types.FragmentType
import com.example.testbalinasoftapp.ui.viewmodel.AuthViewModel
import com.example.testbalinasoftapp.ui.viewmodel.HostViewModel
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val hostViewModel: HostViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { loginResponse ->
                // Успешный вход, можно сохранить пользователя в БД или перейти на другой экран
                handleLoginSuccess(loginResponse)
            }.onFailure { error ->
                // Обработка ошибки (например, показать тост)
                handleLoginError(error.message)
            }
        }
        // Обработка нажатия кнопки
        buttonSubmit.setOnClickListener {

            // Реализуйте логику для обработки входа или регистрации
            if (tabLayout.selectedTabPosition == 0) {
                authViewModel.login(
                    binding.editUsername.text.toString(),
                    binding.editPassword.text.toString()
                )
            } else {
                if (binding.editPassword.text.toString() == binding.editConfirmPassword.text.toString())
                    authViewModel.register(
                        binding.editUsername.text.toString(),
                        binding.editPassword.text.toString()
                    )
                else
                    handleLoginError("Incorrect password")
            }
        }
    }

    private fun handleLoginSuccess(loginResponse: LoginResponse) {
        hostViewModel.switchFragment(FragmentType.GALLERY)
        // Обработка успешного входа
        Toast.makeText(
            requireContext(),
            "Login successful: ${loginResponse.data.token}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleLoginError(errorMessage: String?) {
        // Обработка ошибки входа
        Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
    }
}
