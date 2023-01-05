package com.asmirestoran.pesomakanan

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter


object FloatConvertors {

    @BindingAdapter("android:text")
    @JvmStatic
    fun setText(view: TextView, value: Float) {
        var setValue = view.text.isEmpty()
        try {
            if (!setValue) {
                setValue = view.text.toString().toFloat() != value
            }
        } catch (e: NumberFormatException) {
        }
        if (setValue) {
            view.text = value.toString()
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    @JvmStatic
    fun getText(view: TextView): Float {
        return try {
            view.text.toString().toFloat()
        } catch (e: NumberFormatException) {
            0f
        }
    }
}

object IntConvertors {

    @BindingAdapter("android:text")
    @JvmStatic
    fun setText(view: TextView, value: Int) {
        var setValue = view.text.isEmpty()
        try {
            if (!setValue) {
                setValue = view.text.toString().toInt() != value
            }
        } catch (e: NumberFormatException) {
        }
        if (setValue) {
            view.text = value.toString()
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    @JvmStatic
    fun getText(view: TextView): Int {
        return try {
            view.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }
}
