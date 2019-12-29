package stas.batura.imagegetterkotl.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.main_fragment.*
import stas.batura.imagegetterkotl.data.net.RetrofitClient
import java.lang.Exception

enum class ImageApiStatus { LOADING, ERROR, DONE }

class MainViewModel : ViewModel() {

    private val _imageStatus :MutableLiveData<ImageApiStatus> = MutableLiveData()
    val imageStatus: LiveData<ImageApiStatus>
        get() = _imageStatus

    private val _imageBit : MutableLiveData<Bitmap> =MutableLiveData()
    val imagetBit : LiveData<Bitmap>
        get() = _imageBit

    init {
        getNewImageFromInternet("")
    }


    private fun getNewImageFromInternet (properties : String) {
        _imageStatus.value = ImageApiStatus.LOADING
        try {
            RetrofitClient.getBitmapFrom("cat") {
                print(it.toString())
                _imageStatus.value = ImageApiStatus.DONE
                _imageBit.value = it
            }

        } catch (e:Exception) {
            print(e.toString())
            _imageStatus.value = ImageApiStatus.ERROR
        }
    }
}
