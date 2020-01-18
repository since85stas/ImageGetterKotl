package stas.batura.imagegetterkotl.ui.main

import android.text.Editable
import android.text.TextWatcher

class EditTextWatcher : TextWatcher {

    private var _string:String = ""
    val string:String
        get() = _string

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _string = s.toString()
    }
}