package com.nguyenanhtrung.schoolmanagement.ui.splash

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivity
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val splashViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SplashViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        //
        subscribeCheckLogin()
    }

    private fun subscribeCheckLogin() {
        splashViewModel.checkLoginLiveData.observe(this, Observer {
            splashViewModel.onCheckLoginState(it.data)
        })
    }

    override fun inflateLayout(): Int = R.layout.activity_splash

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun getLoadingBar(): ProgressBar = progress_loading

    override fun createViewModel(): BaseActivityViewModel = splashViewModel

    override fun getViewForSnackbar(): View = root_layout

}
