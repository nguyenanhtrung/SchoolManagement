package com.nguyenanhtrung.schoolmanagement.util

import android.content.Context
import android.net.Uri
import java.io.InputStream

class FileUtils private constructor() {

    companion object {

        private fun parseToUri(path: String): Uri {
            return Uri.parse(path)
        }

        fun getInputStreamLocalImage(context: Context, imageUri: String): InputStream? {
            val uri = parseToUri(imageUri)
            return context.contentResolver.openInputStream(uri)
        }
    }
}