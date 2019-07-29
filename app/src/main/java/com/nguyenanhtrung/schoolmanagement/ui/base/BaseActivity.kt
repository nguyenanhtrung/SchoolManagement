package com.nguyenanhtrung.schoolmanagement.ui.base

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState
import com.nguyenanhtrung.schoolmanagement.util.NetworkLiveData


abstract class BaseActivity : AppCompatActivity() {


    lateinit var baseViewModel: BaseActivityViewModel

    private lateinit var loadingBar: ProgressBar
    private lateinit var networkLiveData: NetworkLiveData
    private val snackBar by lazy {
       Snackbar.make(getViewForSnackbar(), "", Snackbar.LENGTH_LONG)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(inflateLayout())
        injectDependencies(application)
        baseViewModel = createViewModel()
        initUiComponent()
        subscribeMessage()
        subscribeLoading()
        subscribeError()
        subscribeNetworkStatus()

        baseViewModel.viewStateLiveData.observe(this, Observer {
            baseViewModel.handleViewState(it)
        })

    }

    private fun subscribeMessage() {
        baseViewModel.messageLiveData.observe(this, Observer {
            showMessage(getString(it))
        })
    }

    private fun subscribeNetworkStatus() {
        networkLiveData = NetworkLiveData(applicationContext)
        networkLiveData.observe(this, Observer {
            baseViewModel.onNetworkStatusChanged(it)
        })
    }

    private fun subscribeError() {
        baseViewModel.errorLiveData.observe(this, Observer {
            when (it) {
                is ErrorState.NoAction -> showMessage(getString(it.messageId))
                is ErrorState.WithAction -> showErrorWithAction(
                    getString(it.messageId),
                    it.action
                )
                is ErrorState.Empty -> clearError()
            }
        })
    }

    protected open fun clearError() {
        snackBar.dismiss()
    }

    private fun subscribeLoading() {
        baseViewModel.loadingLiveData.observe(this, Observer {
            when (it) {
                true -> showLoading()
                else -> hideLoading()
            }
        })
    }

    private fun initUiComponent() {
        loadingBar = getLoadingBar()
    }

    private fun showLoading() {
        if (::loadingBar.isInitialized) {
            loadingBar.visibility = View.VISIBLE
            disableInteraction()
        }
    }

    protected open fun showMessage(message: String) {
        snackBar.setText(message)
        snackBar.show()
    }


    protected open fun showErrorWithAction(message: String, action: () -> Unit) {
        snackBar.setText(message)
        snackBar.setAction(getString(R.string.retry)) {
            action()
        }
        snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        snackBar.show()
    }

    private fun disableInteraction() {
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun enableInteraction() {
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun hideLoading() {
        if (::loadingBar.isInitialized) {
            loadingBar.visibility = View.GONE
            enableInteraction()
        }
    }

    protected fun hideKeyboard(editText: TextInputEditText) {
        val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    protected fun showKeyboard(editText: TextInputEditText) {
        if (editText.requestFocus()) {
            val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }


    protected abstract fun getLoadingBar(): ProgressBar
    protected abstract fun createViewModel(): BaseActivityViewModel
    protected abstract fun getViewForSnackbar(): View
    protected abstract fun injectDependencies(application: Application)
    protected abstract fun inflateLayout(): Int
}