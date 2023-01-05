package com.asmirestoran.pesomakanan.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.ConnectivityManager
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.FileProvider
import com.asmirestoran.pesomakanan.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.net.URLConnection
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.min


class Utils {

    companion object {

        fun showToast(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun getDate(): String {
            val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            return formatter.format(Calendar.getInstance().time)
        }

        fun getCurrentDate(): Date? {
            return Calendar.getInstance().time
        }

        fun getDateWithTime(): String? {
            val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
            return formatter.format(Calendar.getInstance().time)
        }

        fun getCurrentTime(): IntArray {
            val cal = Calendar.getInstance()
            cal.time = Date()
            val hours = cal.get(Calendar.HOUR_OF_DAY)
            val mins = cal.get(Calendar.MINUTE)
            return intArrayOf(hours, mins)
        }

        fun isNetworkConnected(context: Context): Boolean {
            val connMgr =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connMgr.activeNetworkInfo
            if (activeNetworkInfo != null) { // connected to the internet
                Toast.makeText(context, activeNetworkInfo.typeName, Toast.LENGTH_SHORT).show()
                return if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    true
                } else activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
            }
            return false
        }

        fun showSnack(activity: Activity, message: String?) {
            val snackBar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                message!!, BaseTransientBottomBar.LENGTH_LONG
            )
            snackBar.show()
        }

        fun hideKeyboardFrom(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun capitalizeWord(str: String): String? {
            return str.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
        }

        fun diffBetweenDates(strStartDate: String?, strEndDate: String?): Int {
            if (TextUtils.isEmpty(strStartDate) || TextUtils.isEmpty(strEndDate)) {
                return -1
            }
            val startDate: Date? = convertStringToDate(strStartDate)
            val endDate: Date? = convertStringToDate(strEndDate)
            if (startDate == null || endDate == null) {
                return -1
            }
            val msDiff = startDate.time - endDate.time
            return TimeUnit.MILLISECONDS.toDays(msDiff).toInt()
        }

        private fun convertStringToDate(strDate: String?): Date? {
            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("dd-MM-yyyy")
            var date: Date? = null
            try {
                date = format.parse(strDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return date
        }

        fun isDateNotValid(date: String?): Boolean {
            return date != null && (date.contains("D") || date.contains("M") || date.contains("Y"))
        }

        fun shareFile(context: Context, file: File) {
            val intentShareFile = Intent(Intent.ACTION_SEND)
            val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
            intentShareFile.type = URLConnection.guessContentTypeFromName(file.name)
            intentShareFile.putExtra(Intent.EXTRA_STREAM, uri)
            /*intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing the report");
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Please chae");
        */context.startActivity(Intent.createChooser(intentShareFile, "Share the report"))
        }

        fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
            val width = bm.width
            val height = bm.height
            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height
            // CREATE A MATRIX FOR THE MANIPULATION
            val matrix = Matrix()
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight)

            // "RECREATE" THE NEW BITMAP
            val resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false
            )
            bm.recycle()
            return resizedBitmap
        }

        fun showDatePicker(mContext: Context, et: EditText) {
            var year = 0
            var month = 0
            var day = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val myCalendar = Calendar.getInstance()
                year = myCalendar[Calendar.YEAR]
                month = myCalendar[Calendar.MONTH]
                day = myCalendar[Calendar.DAY_OF_MONTH]
            }
            val picker = DatePickerDialog(
                mContext,
                R.style.DialogTheme,
                { _, yearFromPicker, monthFromPicker, dayFromPicker ->
                    val dayStr = if (dayFromPicker.toString().length == 1) {
                        "0$dayFromPicker"
                    } else {
                        dayFromPicker.toString()
                    }
                    val month = monthFromPicker + 1
                    val monthStr = if (month.toString().length == 1) {
                        "0$month"
                    } else {
                        month.toString()
                    }
                    et.setText("$dayStr/$monthStr/$yearFromPicker")
                },
                year,
                month,
                day
            )
            picker.datePicker.minDate = System.currentTimeMillis() - 1000
            picker.show()
        }

        fun showTimePicker(context: Context, et: EditText) {
            val date = getCurrentTime()
            TimePickerDialog(context, { _, hour, min ->
                et.setText(createTimeString(hour, min))
            }, date[0], date[1], false).show()
        }

        private fun createTimeString(_hour: Int, min: Int): String {
            var dateStr = ""
            var hour = _hour
            var ampm = ""
            if (hour < 10) {
                dateStr += "0$hour"
                ampm = "AM"
            } else {
                if (hour > 12) {
                    hour -= 12
                    if (hour < 10) {
                        dateStr += "0$hour"
                    }
                    ampm = "PM"
                } else {
                    ampm = "AM"
                }
                dateStr += "$hour"
            }
            dateStr += ":"
            if (min < 10)
                dateStr += "0$min"
            else
                dateStr += "$min"

            return "$dateStr $ampm"
        }
    }


    fun showOrderCreationDialog(context: Context) {
        val appCompatDialog = AppCompatDialog(context)
    }


}

