package com.nguyenanhtrung.schoolmanagement.ui.profiledetail

import android.app.Application
import android.os.Bundle
import android.view.*
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
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.baseprofile.BaseProfileFragment
import com.nguyenanhtrung.schoolmanagement.ui.baseprofile.BaseProfileViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.getString
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

    private lateinit var detailMenu: Menu

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel
    override fun createBaseProfileViewModel(): BaseProfileViewModel = detailViewModel

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

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun getProfileArg(): Profile {
        return profileArgs.profile
    }


    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_profile_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        subscribeProfileDetail()
        detailViewModel.loadProfileDetail()
        subscribeStateModifyProfile()
        subscribeSaveProfileModification()

    }


    private fun subscribeSaveProfileModification() {
        detailViewModel.saveProfileModification.observe(this, Observer {
            it.data?.let { newImagePath ->
                detailViewModel.onSuccessSaveProfileModification(newImagePath)
                mainViewModel.showMessage(R.string.success_modify_profile_info)
                if (newImagePath.isNotEmpty()) {
                    mainViewModel.mutableProfileUpdated.value = Event(newImagePath)
                }
            }
        })
    }

    private fun subscribeStateModifyProfile() {
        detailViewModel.stateModifyProfileInfo.observe(this, Observer {
            when (it) {
                ModificationState.Edit -> {
                    setGenderSelection(isEnabled = true)
                    enableProfileInput()
                    showMenuItemSave()
                }
                ModificationState.Save -> {
                    disableProfileInput()
                    setGenderSelection(isEnabled = false)
                    showMenuItemEdit()
                }
            }
        })
    }

    private fun showMenuItemSave() {
        val editItem = detailMenu.findItem(R.id.item_edit_profile_detail)
        editItem.isVisible = false
        val saveItem = detailMenu.findItem(R.id.item_save_profile_detail)
        saveItem.isVisible = true
    }

    private fun showMenuItemEdit() {
        val editItem = detailMenu.findItem(R.id.item_edit_profile_detail)
        editItem.isVisible = true
        val saveItem = detailMenu.findItem(R.id.item_save_profile_detail)
        saveItem.isVisible = false
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        detailViewModel.initStateModifyProfileInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_profile_detail, menu)
        detailMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.item_edit_profile_detail -> {
            detailViewModel.onClickButtonEditProfile()
            true
        }
        R.id.item_save_profile_detail -> {
            detailViewModel.onClickButtonSaveModifiedProfile(
                ProfileDetail(
                    edit_text_birthday.getString(),
                    edit_text_phone.getString(),
                    edit_text_address.getString(),
                    edit_text_email.getString(),
                    getGenderSelected()
                )
            )
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun getGenderSelected(): Gender = when (toggle_group_gender.checkedButtonId) {
        R.id.button_male_gender -> Gender.MALE
        R.id.button_female_gender -> Gender.FEMALE
        else -> Gender.MALE
    }

    private fun subscribeProfileDetail() {
        detailViewModel.profileDetailResult.observe(this, Observer {
            it.data?.let { profileDetail ->
                detailViewModel.profileDetail = profileDetail
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
        when (gender) {
            Gender.MALE -> toggle_group_gender.check(R.id.button_male_gender)
            Gender.FEMALE -> toggle_group_gender.check(R.id.button_female_gender)
        }
    }

    override fun setupUiEvents() {
        super.setupUiEvents()
        subscribeVisibilityButtonResetImage()
        button_reset_profile_image.setOnClickListener {
            detailViewModel.onClickButtonResetProfileImage()
        }
    }

    private fun subscribeVisibilityButtonResetImage() {
        detailViewModel.stateResetProfileImage.observe(viewLifecycleOwner, Observer {
            when (it) {
                Visibility.SHOW -> button_reset_profile_image.visibility = View.VISIBLE
                Visibility.HIDE -> button_reset_profile_image.visibility = View.GONE
                else -> Visibility.SHOW
            }
        })
    }

}