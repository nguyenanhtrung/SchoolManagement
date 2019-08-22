package com.nguyenanhtrung.schoolmanagement.ui.profiledetail

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.Gender
import com.nguyenanhtrung.schoolmanagement.data.local.model.Profile
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileDetail
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.baseprofile.BaseProfileFragment
import com.nguyenanhtrung.schoolmanagement.ui.baseprofile.BaseProfileViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.disableEdit
import com.nguyenanhtrung.schoolmanagement.util.loadImageIfEmptyPath
import kotlinx.android.synthetic.main.fragment_profile_detail.*
import javax.inject.Inject

class ProfileDetailFragment : BaseProfileFragment() {

    private val profileArgs: ProfileDetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val detailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ProfileDetailViewModel::class.java]
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel
    override fun createBaseProfileViewModel(): BaseProfileViewModel = detailViewModel

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun getProfileArg(): Profile {
        return profileArgs.profile
    }

    override fun bindImageViewProfile(): ImageView = image_profile_detail

    override fun bindButtonPickImage(): ImageButton = button_change_profile_image

    override fun bindTextProfileName(): TextView = text_profile_name

    override fun bindTextUserTypeName(): TextView = text_user_type_and_id

    override fun bindToggleGroupGender(): MaterialButtonToggleGroup = toggle_group_gender

    override fun bindInputLayoutBirthday(): TextInputLayout = input_layout_birthday

    override fun bindEditTextBirthday(): TextInputEditText = edit_text_birthday

    override fun bindInputLayoutPhone(): TextInputLayout = input_layout_phone

    override fun bindEditTextPhone(): TextInputEditText = edit_text_phone

    override fun bindInputLayoutAddress(): TextInputLayout = input_layout_address

    override fun bindEditTextAddress(): TextInputEditText = edit_text_address

    override fun bindInputLayoutEmail(): TextInputLayout = input_layout_email

    override fun bindEditTextEmail(): TextInputEditText = edit_text_email

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_profile_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeProfileDetail()
    }

    private fun subscribeProfileDetail() {
        detailViewModel.profileDetail.observe(this, Observer {
            it.data?.let { profileDetail ->
                showProfileDetail(profileDetail)
            }
        })
    }

    private fun showProfileDetail(profileDetail: ProfileDetail) {
        with(profileDetail) {
            edit_text_birthday.setText(birthday)
            edit_text_phone.setText(phoneNumber)
            edit_text_address.setText(address)
            edit_text_email.setText(email)
            showSelectedGender(gender)
        }
    }

    private fun showSelectedGender(gender: Gender) {
        when(gender) {
            Gender.MALE -> toggle_group_gender.check(R.id.button_male_gender)
            Gender.FEMALE -> toggle_group_gender.check(R.id.button_female_gender)
        }
    }

    override fun setupUiEvents() {
        super.setupUiEvents()
        setGenderSelection(isEnabled = false)
        edit_text_birthday.isEnabled = false
        edit_text_birthday.disableEdit(input_layout_email)
        edit_text_address.disableEdit(input_layout_address)
        edit_text_phone.disableEdit(input_layout_phone)
        edit_text_email.disableEdit(input_layout_email)
        detailViewModel.loadProfileDetail()
    }

    private fun setGenderSelection(isEnabled: Boolean) {
        button_male_gender.isClickable = isEnabled
        button_female_gender.isClickable = isEnabled
        toggle_group_gender.isEnabled = isEnabled
    }

}