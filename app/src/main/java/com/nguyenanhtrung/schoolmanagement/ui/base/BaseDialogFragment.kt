package com.nguyenanhtrung.schoolmanagement.ui.base

import android.app.Application
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.DimenRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject


abstract class BaseDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var dialogViewModel: BaseViewModel
    private lateinit var activityViewModel: BaseActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies(requireActivity().application)
        dialogViewModel = createDialogViewModel()
        activityViewModel = bindActivityViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflateLayout(inflater, container)
        setDefaultBackgroundDialog()
        return view
    }

    override fun onResume() {
        setSizeDialog()
        super.onResume()
    }

    protected fun setSizeDialog() {
        setDialogSizeByMode()
    }

    protected fun setDialogSizeByDimens(@DimenRes widthId: Int, @DimenRes heightId: Int) {
        val width = resources.getDimensionPixelSize(widthId)
        val height = resources.getDimensionPixelSize(heightId)
        val myDialog = dialog
        if (myDialog != null && myDialog.window != null) {
            val window = myDialog.window
            window?.setLayout(width, height)
        }
    }

    protected fun setDialogSizeByPercentage(widthPercent: Int, heightPercent: Int) {
        val window = dialog?.window
        val size = Point()
        // Store dimensions of the screen in `size`
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)

        // Set the width of the dialog proportional to 75% of the screen width
        window?.setLayout(
            (size.x * (widthPercent / 100)).toInt(),
            (size.y * (heightPercent / 100)).toInt()
        )
        window?.setGravity(Gravity.CENTER)
    }

    protected fun setDialogSizeByMode(
        widthParam: Int = WindowManager.LayoutParams.MATCH_PARENT,
        heightParam: Int = WindowManager.LayoutParams.WRAP_CONTENT
    ) {
        // Get existing layout params for the window
        val params = dialog?.window?.attributes
        // Assign window properties to fill the parent
        params?.width = widthParam
        params?.height = heightParam
        dialog?.window?.attributes = params as android.view.WindowManager.LayoutParams
    }

    private fun setDefaultBackgroundDialog() {
        val myDialog = dialog
        if (myDialog?.window != null) {
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    protected abstract fun injectDependencies(application: Application)
    protected abstract fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View?
    protected abstract fun createDialogViewModel(): BaseViewModel
    protected abstract fun bindActivityViewModel(): BaseActivityViewModel
}