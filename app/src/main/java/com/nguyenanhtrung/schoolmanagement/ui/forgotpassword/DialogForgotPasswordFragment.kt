package com.nguyenanhtrung.schoolmanagement.ui.forgotpassword

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseDialogFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.login.LoginViewModel
import kotlinx.android.synthetic.main.dialog_forgot_password_fragment.*
import javax.inject.Inject

class DialogForgotPasswordFragment : BaseDialogFragment() {
    companion object {
        const val TAG = "DialogForgotPasswordFragment"

        fun newInstance(): DialogForgotPasswordFragment {
            return DialogForgotPasswordFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var dialogViewModel: ForgotPasswordViewModel

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.dialog_forgot_password_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUiEvents()
        subscribeDismissDialog()
    }

    private fun subscribeDismissDialog() {
        dialogViewModel.dismissDialogLiveData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { isDismiss ->
                if (isDismiss) {
                    dismiss()
                }
            }
        })
    }

    private fun setupUiEvents() {
        button_cancel.setOnClickListener {
            dialogViewModel.onClickButtonClose()
        }

        button_confirm.setOnClickListener {

        }
    }

    override fun createDialogViewModel(): BaseViewModel {
        dialogViewModel = ViewModelProviders.of(this, viewModelFactory)[ForgotPasswordViewModel::class.java]
        return dialogViewModel
    }

    override fun bindActivityViewModel(): BaseActivityViewModel {
        return ViewModelProviders.of(requireActivity())[LoginViewModel::class.java]
    }
}