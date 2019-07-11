package com.nguyenanhtrung.schoolmanagement.ui.base

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.util.NetworkLiveData
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity() {


    lateinit var baseViewModel: BaseActivityViewModel

    private lateinit var loadingBar: ProgressBar
    private lateinit var networkLiveData: NetworkLiveData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(inflateLayout())
        injectDependencies(application)
        baseViewModel = createViewModel()
        initUiComponent()
        subscribeLoading()
        subscribeError()
        subscribeNetworkStatus()

        baseViewModel.viewStateLiveData.observe(this, Observer {
            Log.d("LoginState",it.toString())
            baseViewModel.handleViewState(it)
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
            when(it) {
                is ErrorState.NoAction -> showSnackbar(it.message)
                is ErrorState.WithAction -> showSnackbarWithAction(it.message, it.actionName, it.action)
            }
        })
    }

    private fun subscribeLoading() {
        baseViewModel.loadingLiveData.observe(this, Observer {
            when(it) {
                ResultModel.Loading -> showLoading()
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

    private fun showSnackbar(message: String) {
        Snackbar.make(getViewForSnackbar(), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showSnackbarWithAction( message: String, actionName: String, action: () -> Unit) {
        Snackbar.make(getViewForSnackbar(), message,Snackbar.LENGTH_INDEFINITE)
            .setAction(actionName) {
                action()
            }
            .show()
    }

    private fun disableInteraction() {
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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