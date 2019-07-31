package com.nguyenanhtrung.schoolmanagement.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nguyenanhtrung.schoolmanagement.ui.accountdetail.AccountDetailViewModel
import com.nguyenanhtrung.schoolmanagement.ui.accountmangement.AccountManagementViewModel
import com.nguyenanhtrung.schoolmanagement.ui.createaccount.CreateAccountViewModel
import com.nguyenanhtrung.schoolmanagement.ui.dashboard.DashboardViewModel
import com.nguyenanhtrung.schoolmanagement.ui.filtersheet.FilterSheetViewModel
import com.nguyenanhtrung.schoolmanagement.ui.flowstatus.FlowStatusViewModel
import com.nguyenanhtrung.schoolmanagement.ui.forgotpassword.ForgotPasswordViewModel
import com.nguyenanhtrung.schoolmanagement.ui.login.LoginViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.ui.profiles.ProfilesViewModel
import com.nguyenanhtrung.schoolmanagement.ui.splash.SplashViewModel
import com.nguyenanhtrung.schoolmanagement.util.AppViewModelFactory
import com.nguyenanhtrung.schoolmanagement.util.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(appViewModelFactory: AppViewModelFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    abstract fun bindForgotPasswordViewModel(forgotPasswordViewModel: ForgotPasswordViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(dashboardViewModel: DashboardViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountManagementViewModel::class)
    abstract fun bindAccountManagementViewModel(accountManagementViewModel: AccountManagementViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateAccountViewModel::class)
    abstract fun bindCreateAccountViewModel(createAccountViewModel: CreateAccountViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FlowStatusViewModel::class)
    abstract fun bindFlowStatusViewModel(flowStatusViewModel: FlowStatusViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountDetailViewModel::class)
    abstract fun bindAccountDetailViewModel(accountDetailViewModel: AccountDetailViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfilesViewModel::class)
    abstract fun bindProfilesViewModel(profilesViewModel: ProfilesViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FilterSheetViewModel::class)
    abstract fun bindFilterSheetViewModel(filterSheetViewModel: FilterSheetViewModel) : ViewModel
}