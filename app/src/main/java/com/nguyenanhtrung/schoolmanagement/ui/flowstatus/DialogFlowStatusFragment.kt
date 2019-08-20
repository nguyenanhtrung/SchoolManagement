package com.nguyenanhtrung.schoolmanagement.ui.flowstatus

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseDialogFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import kotlinx.android.synthetic.main.dialog_flow_status_fragment.*
import javax.inject.Inject

class DialogFlowStatusFragment : BaseDialogFragment() {

    private val args: DialogFlowStatusFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val flowStatusViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[FlowStatusViewModel::class.java]
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.dialog_flow_status_fragment, container, false)
    }

    override fun createDialogViewModel(): BaseViewModel = flowStatusViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel

    override fun getProgressLoading(): ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val statusInfo = args.flowStatusInfo
        flowStatusViewModel.flowStatusInfo = statusInfo
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUiEvents()

    }

    private fun setupUiEvents() {
        subscribeViewsData()
        button_close_dialog.setOnClickListener {
            dismiss()
        }
        flowStatusViewModel.showViewDatas()
    }

    private fun subscribeViewsData() {
        flowStatusViewModel.viewDatas.observe(viewLifecycleOwner, Observer {
            text_status.text = getString(it.titleId)
            text_detail_status.text = getString(it.contentId)

            val buttonNameId = it.buttonNameId
            if (buttonNameId == -1) {
                button_status_action.visibility = View.GONE
            } else {
                button_status_action.text = getString(buttonNameId)
            }
        })
    }

}