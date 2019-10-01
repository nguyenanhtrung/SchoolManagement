package com.nguyenanhtrung.schoolmanagement.ui.updateprofile

import android.app.Application
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.baseprofile.BaseProfileFragment
import com.nguyenanhtrung.schoolmanagement.ui.baseprofile.BaseProfileViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.disableInput
import com.nguyenanhtrung.schoolmanagement.util.getString
import kotlinx.android.synthetic.main.fragment_update_profile.*
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject


class ProfileUpdateFragment : BaseProfileFragment(), EasyPermissions.PermissionCallbacks {

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

    override fun createBaseProfileViewModel(): BaseProfileViewModel = updateViewModel

    override fun getProfileArg(): Profile {
        return args.profile
    }

    override fun bindImageViewProfile(): ImageView = image_profile

    override fun bindButtonPickImage(): ImageButton = button_pick_image

    override fun bindTextProfileName(): TextView = text_profile_name

    override fun bindTextUserTypeName(): TextView = text_profile_user_type

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

    override fun setupUiEvents() {
        super.setupUiEvents()
        edit_text_birthday.disableInput(input_layout_birthday)
    }

    private fun onReceivedArgs() {
        updateViewModel.indexProfile = args.posProfileItem
    }

    private fun subscribeStateUpdateProfile() {
        if (updateViewModel.indexProfile < 0) {
            return
        }
        updateViewModel.stateUpdateProfile.observe(this, Observer {
            it.data?.let { imageUri ->
                disableProfileInput()
                setGenderSelection(false)
                val menuItem = updateMenu.findItem(R.id.item_update_profile)
                menuItem.isVisible = false
                notifyProfileUpdated(imageUri)
                navigateToSuccessDialog()
            }
        })
    }

    private fun notifyProfileUpdated(imageUri: String) {
        if (imageUri.isNotEmpty()) {
            val profileUpdated = mainViewModel.mutableProfileUpdated
            profileUpdated.value = Event(imageUri)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_profile_update, menu)
        updateMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.item_update_profile -> {
            updateViewModel.onClickConfirmUpdateItem(
                ProfileUpdateInput(
                    name = edit_text_name.getString(),
                    birthday = edit_text_birthday.getString(),
                    phoneNumber = edit_text_phone.getString(),
                    address = edit_text_address.getString(),
                    email = edit_text_email.getString(),
                    genderId = toggle_group_gender.checkedButtonId
                )
            )
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}