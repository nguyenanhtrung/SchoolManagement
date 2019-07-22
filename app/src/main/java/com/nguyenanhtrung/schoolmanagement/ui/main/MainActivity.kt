package com.nguyenanhtrung.schoolmanagement.ui.main

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivity
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        subscribeToolbarVisibility()
    }

    private fun subscribeToolbarVisibility() {
        mainViewModel.toolbarVisibility.observe(this, Observer {
            when(it) {
                true -> tool_bar_main.visibility = View.VISIBLE
                false -> tool_bar_main.visibility = View.GONE
            }
        })
    }

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun inflateLayout(): Int = R.layout.activity_main

    override fun getLoadingBar(): ProgressBar = progress_bar_loading

    override fun createViewModel(): BaseActivityViewModel {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        return mainViewModel
    }

    override fun getViewForSnackbar(): View = root
}
