package com.example.testbalinasoftapp.ui.fragment

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.testbalinasoftapp.databinding.FragmentMapBinding
import com.example.testbalinasoftapp.domain.types.DrawerType
import com.example.testbalinasoftapp.ui.viewmodel.GalleryViewModel
import com.example.testbalinasoftapp.ui.viewmodel.HostViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val hostViewModel: HostViewModel by viewModel()
    private val galleryViewModel: GalleryViewModel by viewModel()

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
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

        Configuration.getInstance()
            .load(requireContext(), requireContext().getSharedPreferences("osm_pref", 0))

        mapView = binding.osmMap
        mapView.setMultiTouchControls(true)

        val startPoint = GeoPoint(0.5, 0.5)
        mapView.controller.setZoom(10.0)
        mapView.controller.setCenter(startPoint)

        val marker = Marker(mapView)
        marker.position = startPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapView.overlays.add(marker)
        lifecycleScope.launch {
            galleryViewModel.fetchLocalImages().map { addMarkerWithPhoto(it.lat, it.lng, it.url) }
        }
    }

    private fun addMarkerWithPhoto(lat: Double, lon: Double, photoUrl: String) {
        val marker = Marker(mapView)
        marker.position = GeoPoint(lat, lon)
        Glide.with(this)
            .asBitmap()
            .load(photoUrl)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    marker.icon = BitmapDrawable(resources, resource)
                    mapView.overlays.add(marker)
                    mapView.invalidate()
                }
            })
        marker.setOnMarkerClickListener { _, _ ->
            true
        }
        mapView.overlays.add(marker)
        mapView.invalidate()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
