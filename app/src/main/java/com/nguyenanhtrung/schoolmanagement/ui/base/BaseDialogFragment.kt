package com.nguyenanhtrung.schoolmanagement.ui.base

import android.app.Application
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.annotation.DimenRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.util.clearErrorWhenFocus
import kotlinx.android.synthetic.main.dialog_forgot_password_fragment.*
import javax.inject.Inject


abstract class BaseDialogFragment : DialogFragment() {

    companion object {
        const val DEFAULT_PERCENT_WIDTH = 0.85f
    }

    private lateinit var dialogViewModel: BaseViewModel
    private lateinit var activityViewModel: BaseActivityViewModel
    private lateinit var progressLoading: ProgressBar


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
        setCancelableOutside()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressLoading = getProgressLoading()
        edit_text_email.clearErrorWhenFocus(input_layout_email)
        subscribeLoading()
        dialogViewModel.viewStateLiveData.observe(this, Observer {
            dialogViewModel.handleViewState(it)
        })
    }

    private fun disableInteraction() {
        dialog?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun enableInteraction() {
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun subscribeLoading() {
        dialogViewModel.loadingLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                true -> showLoading()
                else -> hideLoading()
            }
        })
    }

    private fun showLoading() {
        if (::progressLoading.isInitialized) {
            disableInteraction()
            progressLoading.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        if (progressLoading.visibility == View.VISIBLE) {
            enableInteraction()
            progressLoading.visibility = View.GONE
        }
    }

    private fun setCancelableOutside() {
        val myDialog  = dialog
        myDialog?.setCanceledOnTouchOutside(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setDialogAnimation()
    }

    private fun setDialogAnimation() {
        val myDialog = dialog
        if (myDialog != null) {
            val myWindow = myDialog.window
            myWindow?.attributes?.windowAnimations = R.style.DialogAnimation
        }
    }

    override fun onResume() {
        setSizeDialog()
        super.onResume()
    }

    protected fun setSizeDialog() {
        setDialogSizeByPercentage(DEFAULT_PERCENT_WIDTH)
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

    protected fun setDialogSizeByPercentage(widthPercent: Float) {
        val window = dialog?.window
        val size = Point()
        // Store dimensions of the screen in `size`
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)

        // Set the width of the dialog proportional to 75% of the screen width
        window?.setLayout(
            (size.x * (widthPercent)).toInt(),
            (WindowManager.LayoutParams.WRAP_CONTENT)
        )
        window?.setGravity(Gravity.CENTER)
    }

    protected fun setDialogSizeByMode(
        widthParam: Int = WindowManager.LayoutParams.WRAP_CONTENT,
        heightParam: Int = WindowManager.LayoutParams.WRAP_CONTENT
    ) {
        // Get existing layout params for the window
        val params = dialog?.window?.attributes
        // Assign window properties to fill the parent
        params?.width = widthParam
        params?.height = heightParam
        dialog?.window?.attributes = params as WindowManager.LayoutParams
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
    protected abstract fun getProgressLoading(): ProgressBar
}