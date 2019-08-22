package com.nguyenanhtrung.schoolmanagement.ui.updateprofile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Application
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState
import com.nguyenanhtrung.schoolmanagement.data.local.model.FlowStatusInfo
import com.nguyenanhtrung.schoolmanagement.data.local.model.Status
import com.nguyenanhtrung.schoolmanagement.data.local.model.Visibility
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.*
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_update_profile.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import javax.inject.Inject


class ProfileUpdateFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 248
        const val REQUEST_CODE_PICK_IMAGE = 199
    }

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private val args: ProfileUpdateFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val updateViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ProfileUpdateViewModel::class.java]
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    private lateinit var updateMenu: Menu

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun createFragmentViewModel(): BaseViewModel = updateViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(
            R.layout.fragment_update_profile,
            container,
            false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        onReceivedArgs()
        subscribeStateUpdateProfile()
    }

    private fun onReceivedArgs() {
        val profile = args.profile
        updateViewModel.profile = profile
        updateViewModel.indexProfile = args.posProfileItem
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun requestPermissionsPickImage() {
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(this, REQUEST_CODE_PERMISSIONS, *permissions)
                .setRationale(R.string.message_request_permission)
                .setPositiveButtonText(getString(R.string.title_agree))
                .setNegativeButtonText(getString(R.string.title_decline))
                .build()
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            showImagePicker()
        }
    }

    private fun showImagePicker() {
        Matisse.from(this)
            .choose(MimeType.ofAll())
            .countable(true)
            .maxSelectable(1)
            .thumbnailScale(0.85f)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .imageEngine(Glide4Engine())
            .forResult(REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            val imageUris = Matisse.obtainResult(data)
            if (imageUris.isEmpty()) {
                return
            }
            updateViewModel.onProfileImagePicked(imageUris[0].toString())
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun setupUiEvents() {
        setupClearErrorWhenFocus()
        edit_text_birthday.disableEdit(input_layout_birthday)
        edit_text_birthday.isClickable = false
        subscribeBasicProfileInfo()
        subscribeBirthdayInputError()
        subscribePhoneInputError()
        subscribeAddressInputError()
        subscribeEmailInputError()
        subscribeImageSelectedError()
        subscribeProfileImagePicked()
        setupClickEventButtonPickImage()
        setupClickEventInputBirthday()
        updateViewModel.loadBasicProfileInfo()
    }

    private fun disableEditAllInput() {
        image_profile.visibility = View.GONE
        edit_text_birthday.disableInput(input_layout_birthday)

        edit_text_address.disableInput(input_layout_address)
        edit_text_phone.disableInput(input_layout_phone)
        edit_text_email.disableInput(input_layout_email)
    }

    private fun subscribeStateUpdateProfile() {
        if (updateViewModel.indexProfile < 0) {
            return
        }
        updateViewModel.stateUpdateProfile.observe(this, Observer {
            it.data?.let { imageUri ->
                disableEditAllInput()
                val menuItem = updateMenu.findItem(R.id.item_update_profile)
                menuItem.isVisible = false
                notifyProfileUpdated(imageUri)
                navigateToSuccessDialog()
            }
        })
    }

    private fun notifyProfileUpdated(imageUri: String) {
        val indexProfile = updateViewModel.indexProfile
        if (indexProfile >= 0 && imageUri.isNotEmpty()) {
            val profileUpdated = mainViewModel.mutableProfileUpdated
            profileUpdated.value = Pair(indexProfile, imageUri)
        }
    }

    private fun navigateToSuccessDialog() {
        findNavController().navigate(
            ProfileUpdateFragmentDirections.actionProfileUpdateFragmentToDialogFlowStatusDest(
                FlowStatusInfo(
                    Status.SUCCESS,
                    R.string.title_success_update_profile,
                    R.string.title_success_update_profile,
                    R.string.title_update
                )
            )
        )
    }

    private fun subscribeImageSelectedError() {
        updateViewModel.imageSelectedError.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ErrorState.NoAction -> mainViewModel.showError(it)
            }
        })
    }

    private fun subscribeEmailInputError() {
        updateViewModel.emailInputError.observe(viewLifecycleOwner, Observer {
            input_layout_email.setErrorWithState(it)
        })
    }

    private fun subscribeAddressInputError() {

        updateViewModel.addressInputError.observe(viewLifecycleOwner, Observer {
            input_layout_address.setErrorWithState(it)
        })
    }

    private fun subscribePhoneInputError() {
        updateViewModel.phoneInputError.observe(viewLifecycleOwner, Observer {
            input_layout_phone.setErrorWithState(it)
        })
    }

    private fun setupClearErrorWhenFocus() {
        edit_text_birthday.clearErrorWhenFocus(input_layout_birthday)
        edit_text_phone.clearErrorWhenFocus(input_layout_phone)
        edit_text_address.clearErrorWhenFocus(input_layout_address)
        edit_text_email.clearErrorWhenFocus(input_layout_email)
    }

    private fun subscribeBirthdayInputError() {
        updateViewModel.birthdayInputError.observe(viewLifecycleOwner, Observer {
            input_layout_birthday.setErrorWithState(it)
        })
    }

    private fun setupClickEventInputBirthday() {
        edit_text_birthday.setOnClickListener {
            ViewUtils.showDatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val correctMonth = month + 1
                    val dateResult = DateUtils.formatDate(year, correctMonth, dayOfMonth)
                    edit_text_birthday.setText(dateResult)
                })
        }
    }

    private fun subscribeProfileImagePicked() {
        updateViewModel.profileImage.observe(viewLifecycleOwner, Observer {
            image_profile.loadImageIfEmptyPath(it)
        })
    }

    private fun setupClickEventButtonPickImage() {
        button_pick_image.setOnClickListener {
            requestPermissionsPickImage()
        }
    }


    private fun subscribeBasicProfileInfo() {
        updateViewModel.basicProfileInfo.observe(viewLifecycleOwner, Observer {
            text_profile_name.text = it.name
            text_profile_user_type.text = it.userTypeName
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_profile_update, menu)
        updateMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.item_update_profile -> {
            updateViewModel.onClickConfirmUpdateItem(
                birthday = edit_text_birthday.getString(),
                phoneNumber = edit_text_phone.getString(),
                address = edit_text_address.getString(),
                email = edit_text_email.getString(),
                genderId = toggle_group_gender.checkedButtonId
            )
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}