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

    private val _imageBit : MutableLiveData<Bitmap> = MutableLiveData()
    val imagetBit : LiveData<Bitmap>
        get() = _imageBit

    private val _buttonCliked:MutableLiveData<Boolean> = MutableLiveData()
    val buttonClicked: LiveData<Boolean>
        get() = _buttonCliked

    init {
//        getNewImageFromInternet("")
        _buttonCliked.value = false
    }


    private fun getNewImageFromInternet (properties : String) {
        _imageStatus.value = ImageApiStatus.LOADING
        try {
            RetrofitClient.getBitmapFrom("cat") {
                print(it.toString())
                if (it != null) {
                    _imageStatus.value = ImageApiStatus.DONE
                    _imageBit.value = it
                } else {
                    _imageStatus.value = ImageApiStatus.ERROR
                }
            }
        } catch (e:Exception) {
            print(e.toString())
            _imageStatus.value = ImageApiStatus.ERROR
        } finally {
            _buttonCliked.value = false
        }
    }

   fun onLoadImageClicked() {
        if ( !_buttonCliked.value!! ) {
            _buttonCliked.value = true
            getNewImageFromInternet("")
        }
    }

}
