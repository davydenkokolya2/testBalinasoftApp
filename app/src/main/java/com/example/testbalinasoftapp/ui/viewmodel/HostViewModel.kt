package com.example.testbalinasoftapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.testbalinasoftapp.domain.types.DrawerType
import com.example.testbalinasoftapp.domain.types.FragmentType
import com.example.testbalinasoftapp.domain.types.ToolbarIconState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HostViewModel : ViewModel() {
    private val _currentFragment = MutableStateFlow(FragmentType.AUTH)
    val currentFragment: StateFlow<FragmentType> = _currentFragment

    private val _toolbarNavigationIcon = MutableStateFlow(ToolbarIconState.NONE)
    val toolbarNavigationIcon: StateFlow<ToolbarIconState> = _toolbarNavigationIcon

    private val _drawerFragment = MutableStateFlow(DrawerType.CLOSE)
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
