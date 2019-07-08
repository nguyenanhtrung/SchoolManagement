package com.nguyenanhtrung.schoolmanagement.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nguyenanhtrung.schoolmanagement.data.local.model.ApiResult
import javax.inject.Inject
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputEditText


abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    abstract var baseViewModel: BaseViewModel

    private lateinit var loadingBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUiComponent()
        subscribeLoading()
    }

    private fun subscribeLoading() {
        baseViewModel.loadingLiveData.observe(this, Observer {
            when(it) {
                ApiResult.Loading -> showLoading()
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

    protected fun showKeyboard() {
        
    }


    abstract fun getLoadingBar(): ProgressBar
}