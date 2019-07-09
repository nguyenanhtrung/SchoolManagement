package com.nguyenanhtrung.schoolmanagement.ui.base

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory

    private lateinit var fragmentViewModel: BaseViewModel
    private lateinit var activityViewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies(requireActivity().application)
        fragmentViewModel = createFragmentViewModel(viewModelFactory)
        activityViewModel = bindActivityViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflateLayout(inflater, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeLoading()
        subscribeError()
    }

    private fun subscribeError() {
        fragmentViewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            activityViewModel.showError(it)
        })
    }

    private fun subscribeLoading() {
        fragmentViewModel.loadingLiveData.observe(viewLifecycleOwner, Observer {
            activityViewModel.setLoading(it)
        })
    }


    protected abstract fun injectDependencies(application: Application)
    protected abstract fun createFragmentViewModel(viewModelFactory: ViewModelProvider.AndroidViewModelFactory): BaseViewModel
    protected abstract fun bindActivityViewModel(): BaseViewModel
    protected abstract fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View?
}