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
import androidx.navigation.fragment.navArgs
import com.nguyenanhtrung.schoolmanagement.MyApplication
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

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun createFragmentViewModel(): BaseViewModel = updateViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(
            com.nguyenanhtrung.schoolmanagement.R.layout.fragment_update_profile,
            container,
            false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        onReceivedArgs()
    }

    private fun onReceivedArgs() {
        val profile = args.profile
        updateViewModel.profile = profile
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
                .setRationale(com.nguyenanhtrung.schoolmanagement.R.string.message_request_permission)
                .setPositiveButtonText(getString(com.nguyenanhtrung.schoolmanagement.R.string.title_agree))
                .setNegativeButtonText(getString(com.nguyenanhtrung.schoolmanagement.R.string.title_decline))
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
        edit_text_birthday.disableEdit(input_layout_birthday)
        subscribeBasicProfileInfo()
        subscribeProfileImagePicked()
        setupClickEventButtonPickImage()
        setupClickEventInputBirthday()
        updateViewModel.loadBasicProfileInfo()
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
        inflater.inflate(com.nguyenanhtrung.schoolmanagement.R.menu.fragment_profile_update, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}