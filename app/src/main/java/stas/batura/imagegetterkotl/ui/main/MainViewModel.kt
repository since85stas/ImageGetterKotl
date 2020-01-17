package stas.batura.imagegetterkotl.ui.main

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.text.TextWatcher
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import stas.batura.imagegetterkotl.data.net.RetrofitClient
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import android.text.Editable
import androidx.annotation.NonNull
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



enum class ImageApiStatus { LOADING, ERROR, DONE }

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val _app:Application = app

    private val _imageStatus :MutableLiveData<ImageApiStatus> = MutableLiveData()
    val imageStatus: LiveData<ImageApiStatus>
        get() = _imageStatus

    private val _imageBit : MutableLiveData<Bitmap> = MutableLiveData()
    val imagetBit : LiveData<Bitmap>
        get() = _imageBit

    private val _buttonCliked:MutableLiveData<Boolean> = MutableLiveData()
    val buttonClicked: LiveData<Boolean>
        get() = _buttonCliked

    private val _shareButtonCliked:MutableLiveData<Boolean> = MutableLiveData()
    val shareButtonCliked: LiveData<Boolean>
        get() = _shareButtonCliked

    private val _saysCheckBoxEnable:MutableLiveData<Boolean> = MutableLiveData()
    val saysCheckBoxEnable: LiveData<Boolean>
        get() = _saysCheckBoxEnable

    private val _saysEditText: MutableLiveData<String> = MutableLiveData()
    val saysEditText:LiveData<String>
        get() = _saysEditText

    var observableField: ObservableField<String> = ObservableField()



    init {
        _buttonCliked.value = false
        _shareButtonCliked.value = false
        _saysCheckBoxEnable.value = false
    }


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    /*
     загрузка простой фотки из интеренета с использованием Corutines
     */
    private fun getNewImageFromInetCorutines() {
        coroutineScope.launch {
            val resultDeffered = RetrofitClient.netApi.retrofitServise.getSimpleCat()
            try {
                _imageStatus.value = ImageApiStatus.LOADING
                val bytes = resultDeffered.await().bytes()
                _imageBit.value = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                _imageStatus.value = ImageApiStatus.DONE

            } catch (e:Exception) {
                Log.d("eee",e.toString())
                _imageStatus.value = ImageApiStatus.ERROR
            } finally {
                Log.d("eee","finally")
            }
        }
    }

    /*
    загрузка фотки с текстом из интеренета с использованием Corutines
    */
    private fun getNewImageSayingFromInetCorutines(string: String) {
        coroutineScope.launch {
            val resultDeffered = RetrofitClient.netApi.retrofitServise.getSayingCat(string)
            try {
                _imageStatus.value = ImageApiStatus.LOADING
                val bytes = resultDeffered.await().bytes()
                _imageBit.value = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                _imageStatus.value = ImageApiStatus.DONE

            } catch (e:Exception) {
                Log.d("eee",e.toString())
                _imageStatus.value = ImageApiStatus.ERROR
            } finally {
                Log.d("eee","finally")
            }
        }
    }

    /*
    вызывается при нажатии на кнопку загрузки
     */
   fun onLoadImageClicked() {
        if ( !_buttonCliked.value!! ) {
            _buttonCliked.value = true
//            getNewImageFromInternet("")
            getNewImageFromInetCorutines()
        }
    }

    /*
   поделиться фоткой
    */
    fun onShareImageClicked() {
        if (!_shareButtonCliked.value!!) {
            _shareButtonCliked.value = true
        }
    }

    fun onSaysCheckBoxClicked() {
        _saysCheckBoxEnable.value = !_saysCheckBoxEnable.value!!
    }

    fun changeEditTextValue(string: String) {
        _saysEditText.value = string
    }


    fun getLocalBitmapUri(): Uri? {
        val uri = saveImageOnStorage()
        return uri
    }

    // Store image to default external storage directory
    private fun saveImageOnStorage() : Uri? {
        // Store image to default external storage directory
        var bmpUri: Uri? = null
        try {
            val file = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                ), "share_image_" + System.currentTimeMillis() + ".png"
            )
            file.getParentFile().mkdirs()
            val out = FileOutputStream(file)
            _imageBit.value!!.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
            bmpUri = FileProvider.getUriForFile(_app.applicationContext,
                _app.applicationContext.packageName +".fileprovider",
                file)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            _shareButtonCliked.value  = false
        }
        return bmpUri
    }

    /*
    удалить файл по следующему пути
     */
    private fun deleteImageOnStorage(uri: Uri) :Boolean {
        val  file = uri.toFile()
        try {

            if (file.delete()) return true else return false
        } catch (exception:Exception) {
            print("wrong file")
        }

        return false;
    }
}
