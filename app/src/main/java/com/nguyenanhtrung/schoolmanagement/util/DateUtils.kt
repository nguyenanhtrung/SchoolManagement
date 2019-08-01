package com.nguyenanhtrung.schoolmanagement.util

import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

class DateUtils private constructor() {
    companion object {
        private const val DATE_PATTERN = "dd/MM/yyyy"

        fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
            val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
            val localDate = LocalDate.of(year, month, dayOfMonth)
            return dateFormatter.format(localDate)
        }

        fun getCurrentDate(): Long {
            val localDate = LocalDate.now()
            return localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        }
    }
}