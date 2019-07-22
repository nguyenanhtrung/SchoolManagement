package com.nguyenanhtrung.schoolmanagement.ui.splash

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.Status
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivity
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.login.LoginActivity
import com.nguyenanhtrung.schoolmanagement.ui.main.MainActivity
import com.nguyenanhtrung.schoolmanagement.util.openActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.include_error_state.*
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val splashViewModel by lazy {
        ViewModelProviders.of(this@SplashActivity, viewModelFactory)[SplashViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        //
        setupUiEvents()
        subscribeCheckLogin()
        subscribeNavigateToLoginScreen()
        subscribeNavigateToMainScreen()
        subscribeUserType()
    }

    private fun subscribeUserType() {
        splashViewModel.userTypeLiveData.observe(this, Observer {
            if (it.status == Status.COMPLETE || it.status == Status.SUCCESS) {
                splashViewModel.onCompletedLoadUserTypes()
            }
        })
    }


    private fun setupUiEvents() {
        button_retry.setOnClickListener {
            splashViewModel.onClickButtonRetry()
        }
    }

    override fun inflateLayout(): Int = R.layout.activity_splash

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun showError(message: String) {
        view_switcher.displayedChild = 1
        if (view_switcher.currentView != layout_error_state) {
            view_switcher.showNext()
        }
        text_error_content.text = message
    }

    override fun clearError() {
        view_switcher.displayedChild = 0
        if (view_switcher.currentView != layout_main) {
            view_switcher.showPrevious()
        }
    }

    override fun getLoadingBar(): ProgressBar = progress_loading

    override fun createViewModel(): BaseActivityViewModel = splashViewModel

    override fun getViewForSnackbar(): View = view_switcher

    private fun subscribeNavigateToMainScreen() {
        splashViewModel.navigateMainScreen.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                openActivity(MainActivity::class.java)
                finish()
            }
        })
    }

    private fun subscribeNavigateToLoginScreen() {
        splashViewModel.navigateLoginScreen.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                openActivity(LoginActivity::class.java)
                finish()
            }
        })
    }

    private fun subscribeCheckLogin() {
        splashViewModel.checkLoginLiveData.observe(this, Observer {
            splashViewModel.onCheckLoginState(it.data)
        })
    }



}
