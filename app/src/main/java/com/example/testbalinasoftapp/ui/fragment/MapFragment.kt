package com.example.testbalinasoftapp.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.testbalinasoftapp.databinding.FragmentMapBinding
import com.example.testbalinasoftapp.domain.DrawerType
import com.example.testbalinasoftapp.domain.FragmentType
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

        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osm_pref", 0))

        mapView = binding.osmMap
        mapView.setMultiTouchControls(true)  // Включаем мультитач жесты

        // Устанавливаем стартовую точку на карте
        val startPoint = GeoPoint(37.7749, -122.4194)  // Пример: координаты Сан-Франциско
        mapView.controller.setZoom(10.0)
        mapView.controller.setCenter(startPoint)

        // Добавляем маркер на карту
        val marker = Marker(mapView)
        marker.position = startPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "San Francisco"
        mapView.overlays.add(marker)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()  // Вызываем для корректной работы карты
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()  // Вызываем для корректной работы карты
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
