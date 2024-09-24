import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {

    private val _latitude = MutableLiveData<Double>()
    private val _longitude = MutableLiveData<Double>()

    val latitude: LiveData<Double> get() = _latitude
    val longitude: LiveData<Double> get() = _longitude

    fun updateLocation(newLatitude: Double, newLongitude: Double) {
        _latitude.value = newLatitude
        _longitude.value = newLongitude
    }
}
