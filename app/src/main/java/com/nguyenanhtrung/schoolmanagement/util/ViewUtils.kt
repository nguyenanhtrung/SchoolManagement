package com.nguyenanhtrung.schoolmanagement.util

import android.app.DatePickerDialog
import android.content.Context
import java.util.*

class ViewUtils private constructor() {

    companion object {

        fun showDatePickerDialog(
            context: Context,
            onDateSetListener: DatePickerDialog.OnDateSetListener
        ) {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]

            val datePickerDialog = DatePickerDialog(
                context, onDateSetListener,
                year, month, day
            )

            val datePicker = datePickerDialog.datePicker
            val maxDate = DateUtils.getCurrentDate()
            datePicker.maxDate = maxDate
            datePickerDialog.show()

        }
    }
}