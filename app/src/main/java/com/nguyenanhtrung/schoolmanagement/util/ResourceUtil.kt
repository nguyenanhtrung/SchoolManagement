package com.nguyenanhtrung.schoolmanagement.util

import android.content.Context
import android.graphics.drawable.Drawable


class ResourceUtil private constructor(){

    companion object {
        fun getDrawableId(context: Context, drawableName: String): Int {
            val resources = context.resources
            return resources.getIdentifier(drawableName,"drawable",context.packageName)
        }

        fun getDrawable(context: Context,resId: Int): Drawable? {
            val resources = context.resources
            return resources.getDrawable(resId, null)
        }


    }
}