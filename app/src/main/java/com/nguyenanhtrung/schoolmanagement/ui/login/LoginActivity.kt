package com.nguyenanhtrung.schoolmanagement.ui.login

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivity
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.forgotpassword.DialogForgotPasswordFragment
import com.nguyenanhtrung.schoolmanagement.ui.main.MainActivity
import com.nguyenanhtrung.schoolmanagement.util.clearErrorWhenFocus
import com.nguyenanhtrung.schoolmanagement.util.getString
import com.nguyenanhtrung.schoolmanagement.util.openActivity
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val loginViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        setupUiEvents()
        subscribeEmailError()
        subscribePasswordError()
        subscribeForgotPasswordDialog()
        subscribeLoginResult()
        subscribeOpenMainScreen()

    }

    private fun subscribeOpenMainScreen() {
        loginViewModel.mainScreenLiveData.observe(this, Observer {
            openActivity(MainActivity::class.java)
            finish()
        })
    }

    private fun subscribeLoginResult() {
        loginViewModel.loginResultLiveData.observe(this, Observer {
            it.data?.let { isLoginSuccess ->
                loginViewModel.onCheckLogin(isLoginSuccess)
            }
        })
    }

    private fun subscribeForgotPasswordDialog() {
        loginViewModel.showForgotPasswordDialog.observe(this, Observer {
            it.getContentIfNotHandled()?.let { isShow ->
                if (isShow) {
                    showForgotPasswordDialog()
                }
            }
        })
    }

    private fun handleInputError(errorState: ErrorState, input: TextInputLayout) {
        when (errorState) {
            is ErrorState.NoAction -> input.error = getString(errorState.messageId)
            is ErrorState.Empty -> {
                if (input.error != null) {
                    input.error = null
                }
            }
        }
    }

    private fun subscribePasswordError() {
        loginViewModel.passwordErrorLiveData.observe(this, Observer {
            handleInputError(it, input_layout_password)
        })
    }

    private fun subscribeEmailError() {
        loginViewModel.emailErrorLiveData.observe(this, Observer {
            handleInputError(it, input_layout_email)
        })
    }

    private fun setupUiEvents() {
        button_login.setOnClickListener {
            loginViewModel.onClickButtonLogin(
                edit_text_email.getString(),
                edit_text_password.getString()
            )
        }

        edit_text_email.clearErrorWhenFocus(input_layout_email)
        edit_text_password.clearErrorWhenFocus(input_layout_password)

        text_forgot_password.setOnClickListener {
            loginViewModel.onClickTextForgotPassword()
        }

    }

    private fun showForgotPasswordDialog() {
        val dialogForgotPassword = DialogForgotPasswordFragment.newInstance()
        dialogForgotPassword.show(supportFragmentManager, DialogForgotPasswordFragment.TAG)
    }

    override fun getLoadingBar(): ProgressBar = progress_bar_loading

    override fun createViewModel(): BaseActivityViewModel {
        return loginViewModel
    }

    override fun inflateLayout(): Int = R.layout.activity_login

    override fun getViewForSnackbar(): View = root
}