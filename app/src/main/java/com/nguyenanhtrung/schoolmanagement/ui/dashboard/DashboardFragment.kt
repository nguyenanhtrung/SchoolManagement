package com.nguyenanhtrung.schoolmanagement.ui.dashboard

import android.app.Application
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.LoadingItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserTaskItem
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserTask
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.MyGridDividerItemDecoration
import com.nguyenanhtrung.schoolmanagement.util.loadImageIfEmptyPath
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.include_search_view.*
import javax.inject.Inject

class DashboardFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val tasksAdapter by lazy {
        GroupAdapter<ViewHolder>()
    }

    private val dashboardViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[DashboardViewModel::class.java]
    }
    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun injectDependencies(application: Application) {
        val myApp  = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun createFragmentViewModel(): BaseViewModel = dashboardViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel.hideToolbar()
    }

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun setupUiEvents() {
        edit_text_search.inputType = InputType.TYPE_NULL
        setupTasksRecyclerView()
        subscribeUserInfo()
        subscribeUserTasks()
        dashboardViewModel.loadUserInfo()
    }

    private fun subscribeUserTasks() {
        dashboardViewModel.userTaskLiveData.observe(viewLifecycleOwner, Observer {
            it.data?.let { userTasks ->
                showUserTasks(userTasks)
            }
        })
    }

    private fun showUserTasks(userTasks: MutableList<UserTaskItem>) {
        recycler_view_tasks.layoutManager = GridLayoutManager(requireContext(), 3)
        recycler_view_tasks.addItemDecoration(MyGridDividerItemDecoration(32, 3))
        if (tasksAdapter.itemCount > 0) {
            tasksAdapter.removeGroup(0)
        }
        tasksAdapter.addAll(userTasks)

    }

    private fun setupTasksRecyclerView() {
        recycler_view_tasks.layoutManager = LinearLayoutManager(requireActivity())
        recycler_view_tasks.adapter = tasksAdapter
        tasksAdapter.add(LoadingItem())
    }

    private fun subscribeUserInfo() {
        dashboardViewModel.userInfoLiveData.observe(viewLifecycleOwner, Observer {
            showUserInfo(it.data)
            dashboardViewModel.loadUserTasks()
        })
    }

    private fun showUserInfo(user: User?) {
        user?.let {
            text_account_name.text = it.name
            text_account_type.text = it.typeName
            circle_image_user.loadImageIfEmptyPath(it.avatarPath)
        }
    }
}