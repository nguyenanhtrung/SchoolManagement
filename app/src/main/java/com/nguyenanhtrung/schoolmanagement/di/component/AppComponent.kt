package com.nguyenanhtrung.schoolmanagement.di.component

import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.di.module.*
import com.nguyenanhtrung.schoolmanagement.ui.accountdetail.AccountDetailFragment
import com.nguyenanhtrung.schoolmanagement.ui.accountmangement.AccountManagementFragment
import com.nguyenanhtrung.schoolmanagement.ui.createaccount.CreateAccountFragment
import com.nguyenanhtrung.schoolmanagement.ui.dashboard.DashboardFragment
import com.nguyenanhtrung.schoolmanagement.ui.filtersheet.FilterBottomSheetDialogFragment
import com.nguyenanhtrung.schoolmanagement.ui.flowstatus.DialogFlowStatusFragment
import com.nguyenanhtrung.schoolmanagement.ui.forgotpassword.DialogForgotPasswordFragment
import com.nguyenanhtrung.schoolmanagement.ui.login.LoginActivity
import com.nguyenanhtrung.schoolmanagement.ui.main.MainActivity
import com.nguyenanhtrung.schoolmanagement.ui.profiles.ProfilesFragment
import com.nguyenanhtrung.schoolmanagement.ui.splash.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class,
        AppModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        RepositoryModule::class]
)
interface AppComponent {
    fun inject(application: MyApplication)
    fun inject(loginActivity: LoginActivity)
    fun inject(forgotPasswordFragment: DialogForgotPasswordFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(splashActivity: SplashActivity)
    fun inject(dashboardFragment: DashboardFragment)
    fun inject(accountManagementFragment: AccountManagementFragment)
    fun inject(createAccountFragment: CreateAccountFragment)
    fun inject(flowStatusFragment: DialogFlowStatusFragment)
    fun inject(accountDetailFragment: AccountDetailFragment)
    fun inject(profilesFragment: ProfilesFragment)
    fun inject(filterBottomSheetDialogFragment: FilterBottomSheetDialogFragment)
}