package com.nguyenanhtrung.schoolmanagement.util

import android.animation.ObjectAnimator
import android.view.View

class MyAnimationUtils private constructor() {

    companion object {
        fun shake(view: View) {
            val animator = ObjectAnimator.ofFloat(
                view,
                View.TRANSLATION_X,
                0.0f,
                25.0f,
                -25.0f,
                25.0f,
                -25.0f,
                15.0f,
                -15.0f,
                6.0f,
                -6.0f,
                0.0f
            )
            animator.duration = 400
            animator.start()
        }
    }
}