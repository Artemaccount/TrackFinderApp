package com.example.myapplication.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {
    fun View.hideKeyboard() {
        context?.let {
            val manager = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(this.windowToken, 0)
        }
    }
}