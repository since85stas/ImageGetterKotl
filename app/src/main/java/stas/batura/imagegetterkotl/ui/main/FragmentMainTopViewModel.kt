package stas.batura.imagegetterkotl.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FragmentMainTopViewModel : ViewModel() {

    var _fabButtonClicked: MutableLiveData<Boolean> = MutableLiveData()
        val fabButtonClicked : LiveData<Boolean>
        get() = _fabButtonClicked
    init {
        _fabButtonClicked.value = false
    }


}