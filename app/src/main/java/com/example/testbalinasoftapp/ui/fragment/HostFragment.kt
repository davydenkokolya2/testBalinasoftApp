import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.testbalinasoftapp.R
import com.example.testbalinasoftapp.databinding.FragmentHostBinding
import com.example.testbalinasoftapp.domain.types.DrawerType
import com.example.testbalinasoftapp.domain.types.FragmentType
import com.example.testbalinasoftapp.domain.types.ToolbarIconState
import com.example.testbalinasoftapp.ui.fragment.AuthFragment
import com.example.testbalinasoftapp.ui.fragment.CameraFragment
import com.example.testbalinasoftapp.ui.fragment.DrawerFragment
import com.example.testbalinasoftapp.ui.fragment.GalleryFragment
import com.example.testbalinasoftapp.ui.fragment.MapFragment
import com.example.testbalinasoftapp.ui.fragment.PhotoDetailFragment
import com.example.testbalinasoftapp.ui.viewmodel.HostViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HostFragment : Fragment() {

    private var _binding: FragmentHostBinding? = null
    private val binding get() = _binding!!

    private val hostViewModel: HostViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            hostViewModel.currentFragment.collect { fragmentType ->
                when (fragmentType) {
                    FragmentType.AUTH -> showAuthFragment()
                    FragmentType.MAP -> showMapFragment()
                    FragmentType.PHOTO_DETAIL -> showPhotoDetailFragment()
                    FragmentType.GALLERY -> showGalleryFragment()
                    FragmentType.CAMERA -> showCameraFragment()
                }
            }
        }

        lifecycleScope.launch {
            hostViewModel.drawerFragment.collect { state ->
                when (state) {
                    DrawerType.OPEN -> showDrawerFragment()
                    DrawerType.CLOSE -> hideDrawerFragment()
                }
            }
        }

        lifecycleScope.launch {
            hostViewModel.toolbarNavigationIcon.collect { state ->
                updateToolbarIcon(state)
            }
        }

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbar.setNavigationOnClickListener {
            when (hostViewModel.toolbarNavigationIcon.value) {
                ToolbarIconState.BACK -> hostViewModel.switchFragment(FragmentType.GALLERY)
                ToolbarIconState.MENU -> if (hostViewModel.drawerFragment.value == DrawerType.CLOSE)
                    hostViewModel.switchDrawerFragment(DrawerType.OPEN)
                else
                    hostViewModel.switchDrawerFragment(DrawerType.CLOSE)

                ToolbarIconState.NONE -> TODO()
            }
        }
    }

    private fun updateToolbarIcon(state: ToolbarIconState) {
        when (state) {
            ToolbarIconState.NONE -> {
                binding.toolbar.navigationIcon = null
            }

            ToolbarIconState.MENU -> {
                binding.toolbar.navigationIcon =
                    ContextCompat.getDrawable(requireActivity(), R.drawable.ic_menu)
            }

            ToolbarIconState.BACK -> {
                binding.toolbar.navigationIcon = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_back
                )
            }
        }
    }

    private fun showCameraFragment() {
        val cameraFragment = CameraFragment()
        parentFragmentManager.commit {
            replace(R.id.fragmentContainer, cameraFragment)
        }
    }

    private fun showAuthFragment() {
        val authFragment = AuthFragment()
        parentFragmentManager.commit {
            replace(R.id.fragmentContainer, authFragment)
        }
    }

    private fun showMapFragment() {
        val mapFragment = MapFragment()
        parentFragmentManager.commit {
            replace(R.id.fragmentContainer, mapFragment)
        }
    }

    private fun hideDrawerFragment() {
        val fragmentManager = parentFragmentManager
        val drawerFragment = fragmentManager.findFragmentByTag("DrawerFragment") as? DrawerFragment
        if (drawerFragment != null) {
            fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_to_left, R.anim.exit_to_left)
                .remove(drawerFragment)
                .commit()
        }
    }

    private fun showDrawerFragment() {
        val drawerFragment = DrawerFragment()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
            .add(R.id.fragmentContainer, drawerFragment, "DrawerFragment")
            .commit()
    }

    private fun showPhotoDetailFragment() {
        val photoDetailFragment = PhotoDetailFragment()
        parentFragmentManager.commit {
            replace(R.id.fragmentContainer, photoDetailFragment)
        }
    }

    private fun showGalleryFragment() {
        val existingFragment = parentFragmentManager.findFragmentByTag("GalleryFragment")

        if (existingFragment != null) {
            parentFragmentManager.commit {
                parentFragmentManager.fragments.forEach { fragment ->
                    if (fragment != existingFragment) hide(fragment)
                }
                show(existingFragment)
            }
        } else {
            val galleryFragment = GalleryFragment()
            parentFragmentManager.commit {
                replace(R.id.fragmentContainer, galleryFragment, "GalleryFragment")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
