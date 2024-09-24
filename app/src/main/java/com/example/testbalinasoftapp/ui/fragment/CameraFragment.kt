package com.example.testbalinasoftapp.ui.fragment

import CameraViewModel
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.testbalinasoftapp.data.models.ImageUploadRequest
import com.example.testbalinasoftapp.databinding.FragmentCameraBinding
import com.example.testbalinasoftapp.domain.Unix.convertImageToBase64
import com.example.testbalinasoftapp.domain.types.ToolbarIconState
import com.example.testbalinasoftapp.ui.viewmodel.AuthViewModel
import com.example.testbalinasoftapp.ui.viewmodel.GalleryViewModel
import com.example.testbalinasoftapp.ui.viewmodel.HostViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.ZoneOffset


class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestMultiplePermissionsLauncher: ActivityResultLauncher<Array<String>>

    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val cameraViewModel: CameraViewModel by viewModel()
    private val galleryViewModel: GalleryViewModel by viewModel()
    private val hostViewModel: HostViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        hostViewModel.updateToolbarIcon(ToolbarIconState.BACK)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        requestMultiplePermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                when {
                    permissions[Manifest.permission.CAMERA] == true -> {
                        openCamera()
                    }

                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                        getLocationAndUploadPhoto()
                    }

                    else -> {
                        Toast.makeText(context, "Permissions denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                    if (imageBitmap != null) {
                        uploadPhotoWithLocation(
                            imageBitmap,
                            cameraViewModel.latitude.value ?: 0.0,
                            cameraViewModel.longitude.value ?: 0.0
                        )
                    } else {
                        Log.e("CameraFragment", "Image data is null")
                        Toast.makeText(context, "Не удалось получить изображение", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        checkPermissionsAndOpenCameraAndLocation()
        return binding.root
    }

    private fun getLocationAndUploadPhoto() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    cameraViewModel.updateLocation(it.latitude, it.longitude)
                    openCamera()
                } ?: run {
                    Log.e("CameraFragment", "Не удалось получить местоположение.")
                }
            }
            .addOnFailureListener {
                Log.e("CameraFragment", "Failed to get location: ${it.message}")
            }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadPhotoWithLocation(imageFile: Bitmap, lat: Double, lng: Double) {
        // Конвертируйте фото в base64
        val imageBase64 = convertImageToBase64(imageFile)
        val currentDateTime = LocalDateTime.now() // Получаем текущее время
        val unixTime = currentDateTime.toEpochSecond(ZoneOffset.UTC)
        // Отправляем запрос на сервер

        lifecycleScope.launch {
            galleryViewModel.uploadImage(
                authViewModel.token.value,
                ImageUploadRequest(imageBase64, unixTime, lat, lng)
            )
        }
    }

    private fun checkPermissionsAndOpenCameraAndLocation() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                // Разрешения предоставлены, открываем камеру и получаем местоположение
                getLocationAndUploadPhoto()


            }

            else -> {
                // Запрашиваем оба разрешения
                requestMultiplePermissionsLauncher.launch(
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
                )
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            takePictureLauncher.launch(cameraIntent)
        } else {
            Toast.makeText(context, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
