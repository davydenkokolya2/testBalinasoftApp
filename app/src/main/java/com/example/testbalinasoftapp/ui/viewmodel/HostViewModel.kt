package com.example.testbalinasoftapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.testbalinasoftapp.domain.DrawerType
import com.example.testbalinasoftapp.domain.FragmentType
import com.example.testbalinasoftapp.domain.ToolbarIconState
import com.example.testbalinasoftapp.ui.fragment.DrawerFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HostViewModel : ViewModel() {
    // Состояние текущего фрагмента
    private val _currentFragment = MutableStateFlow<FragmentType>(FragmentType.AUTH)
    val currentFragment: StateFlow<FragmentType> = _currentFragment

    private val _toolbarNavigationIcon = MutableStateFlow<ToolbarIconState>(ToolbarIconState.NONE)
    val toolbarNavigationIcon: StateFlow<ToolbarIconState> = _toolbarNavigationIcon

    private val _drawerFragment = MutableStateFlow<DrawerType>(DrawerType.CLOSE)
    val drawerFragment: StateFlow<DrawerType> = _drawerFragment

    fun updateToolbarIcon(state: ToolbarIconState) {
        _toolbarNavigationIcon.value = state
    }

    fun switchFragment(fragmentType: FragmentType) {
        _currentFragment.value = fragmentType
    }

    fun switchDrawerFragment(drawerType: DrawerType) {
        _drawerFragment.value = drawerType
    }
}
