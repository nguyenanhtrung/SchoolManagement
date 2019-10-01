package com.nguyenanhtrung.schoolmanagement.util

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.AccountType
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

        fun setColorTextAccountType(textAccountType: TextView, typeId: String) {
            when (typeId) {
                AccountType.STUDENT.id -> {
                    textAccountType.setCustomTextColor(R.color.colorTypeStudent)
                    textAccountType.setBackgroundResource(R.drawable.bg_text_student_type)
                }
            }
        }
    }
}