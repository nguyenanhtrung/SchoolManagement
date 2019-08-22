package com.nguyenanhtrung.schoolmanagement.ui.baseprofile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Profile
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.updateprofile.ProfileUpdateFragment
import com.nguyenanhtrung.schoolmanagement.util.*
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_update_profile.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

abstract class BaseProfileFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 248
        const val REQUEST_CODE_PICK_IMAGE = 199
    }

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private lateinit var imageViewProfile: ImageView
    private lateinit var buttonPickImage: ImageButton
    private lateinit var textProfileName: TextView
    private lateinit var textUserTypeName: TextView
    private lateinit var toggleGroupGender: MaterialButtonToggleGroup
    private lateinit var inputLayoutBirthday: TextInputLayout
    private lateinit var editTextBirthday: TextInputEditText
    private lateinit var inputLayoutPhone: TextInputLayout
    private lateinit var editTextPhone: TextInputEditText
    private lateinit var inputLayoutAddress: TextInputLayout
    private lateinit var editTextAddress: TextInputEditText
    private lateinit var inputLayoutEmail: TextInputLayout
    private lateinit var editTextEmail: TextInputEditText

    private val baseProfileViewModel by lazy {
        createBaseProfileViewModel()
    }

    private val baseActivityViewModel by lazy {
        bindActivityViewModel()
    }


    override fun createFragmentViewModel(): BaseViewModel = baseProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseProfileViewModel.profile = getProfileArg()
    }

    private fun bindViews() {
        imageViewProfile = bindImageViewProfile()
        buttonPickImage = bindButtonPickImage()
        textProfileName = bindTextProfileName()
        textUserTypeName = bindTextUserTypeName()
        toggleGroupGender = bindToggleGroupGender()
        inputLayoutBirthday = bindInputLayoutBirthday()
        editTextBirthday = bindEditTextBirthday()
        inputLayoutPhone = bindInputLayoutPhone()
        editTextPhone = bindEditTextPhone()
        inputLayoutAddress = bindInputLayoutAddress()
        editTextAddress = bindEditTextAddress()
        inputLayoutEmail = bindInputLayoutEmail()
        editTextEmail = bindEditTextEmail()
    }

    override fun setupUiEvents() {
        bindViews()
        edit_text_birthday.disableEdit(input_layout_birthday)
        setupClearErrorWhenFocus()
        setupClickEventButtonPickImage()
        subscribeBasicProfileInfo()
        subscribeBirthdayInputError()
        subscribePhoneInputError()
        subscribeAddressInputError()
        subscribeEmailInputError()
        subscribeImageSelectedError()
        subscribeProfileImagePicked()
        setupClickEventInputBirthday()
        baseProfileViewModel.loadBasicProfileInfo()
    }

    private fun setupClickEventButtonPickImage() {
        buttonPickImage.setOnClickListener {
            requestPermissionsPickImage()
        }
    }

    private fun setupClickEventInputBirthday() {
        editTextBirthday.setOnClickListener {
            ViewUtils.showDatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val correctMonth = month + 1
                    val dateResult = DateUtils.formatDate(year, correctMonth, dayOfMonth)
                    editTextBirthday.setText(dateResult)
                })
        }
    }

    private fun subscribeProfileImagePicked() {
        baseProfileViewModel.profileImage.observe(viewLifecycleOwner, Observer {
            imageViewProfile.loadImageIfEmptyPath(it)
        })
    }

    private fun subscribeEmailInputError() {
        baseProfileViewModel.emailInputError.observe(viewLifecycleOwner, Observer {
            inputLayoutEmail.setErrorWithState(it)
        })
    }

    private fun subscribeAddressInputError() {
        baseProfileViewModel.addressInputError.observe(viewLifecycleOwner, Observer {
            inputLayoutAddress.setErrorWithState(it)
        })
    }

    private fun subscribeImageSelectedError() {
        baseProfileViewModel.imageSelectedError.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ErrorState.NoAction -> baseActivityViewModel.showError(it)
            }
        })
    }

    private fun subscribeBasicProfileInfo() {
        baseProfileViewModel.basicProfileInfo.observe(viewLifecycleOwner, Observer {
            textProfileName.text = it.name
            textUserTypeName.text = it.userTypeName
            imageViewProfile.loadImageIfEmptyPath(it.profileImagePath)
        })
    }

    private fun subscribeBirthdayInputError() {
        baseProfileViewModel.birthdayInputError.observe(viewLifecycleOwner, Observer {
            inputLayoutBirthday.setErrorWithState(it)
        })
    }

    private fun subscribePhoneInputError() {
        baseProfileViewModel.phoneInputError.observe(viewLifecycleOwner, Observer {
            inputLayoutPhone.setErrorWithState(it)
        })
    }

    private fun setupClearErrorWhenFocus() {
        editTextBirthday.clearErrorWhenFocus(inputLayoutBirthday)
        editTextPhone.clearErrorWhenFocus(inputLayoutPhone)
        editTextAddress.clearErrorWhenFocus(inputLayoutAddress)
        editTextEmail.clearErrorWhenFocus(inputLayoutEmail)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUris = Matisse.obtainResult(data)
            if (imageUris.isEmpty()) {
                return
            }
            baseProfileViewModel.onProfileImagePicked(imageUris[0].toString())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            showImagePicker()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    private fun requestPermissionsPickImage() {
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(
                this,
                ProfileUpdateFragment.REQUEST_CODE_PERMISSIONS,
                *permissions
            )
                .setRationale(com.nguyenanhtrung.schoolmanagement.R.string.message_request_permission)
                .setPositiveButtonText(getString(com.nguyenanhtrung.schoolmanagement.R.string.title_agree))
                .setNegativeButtonText(getString(com.nguyenanhtrung.schoolmanagement.R.string.title_decline))
                .build()
        )
    }

    private fun showImagePicker() {
        Matisse.from(this)
            .choose(MimeType.ofAll())
            .countable(true)
            .maxSelectable(1)
            .thumbnailScale(0.85f)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .imageEngine(Glide4Engine())
            .forResult(ProfileUpdateFragment.REQUEST_CODE_PICK_IMAGE)
    }


    abstract fun createBaseProfileViewModel(): BaseProfileViewModel
    abstract fun getProfileArg(): Profile
    abstract fun bindImageViewProfile(): ImageView
    abstract fun bindButtonPickImage(): ImageButton
    abstract fun bindTextProfileName(): TextView
    abstract fun bindTextUserTypeName(): TextView
    abstract fun bindToggleGroupGender(): MaterialButtonToggleGroup
    abstract fun bindInputLayoutBirthday(): TextInputLayout
    abstract fun bindEditTextBirthday(): TextInputEditText
    abstract fun bindInputLayoutPhone(): TextInputLayout
    abstract fun bindEditTextPhone(): TextInputEditText
    abstract fun bindInputLayoutAddress(): TextInputLayout
    abstract fun bindEditTextAddress(): TextInputEditText
    abstract fun bindInputLayoutEmail(): TextInputLayout
    abstract fun bindEditTextEmail(): TextInputEditText
}