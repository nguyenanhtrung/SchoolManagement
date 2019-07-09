package com.nguyenanhtrung.schoolmanagement.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory

    private lateinit var fragmentViewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflateLayout(inflater, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    protected abstract fun createFragmentViewModel(viewModelFactory: ViewModelProvider.AndroidViewModelFactory): BaseViewModel
    protected abstract fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View?
}