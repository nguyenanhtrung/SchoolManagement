package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.profile

import android.content.Context
import android.net.Uri
import androidx.collection.ArrayMap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileDetail
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileUpdateParam
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import com.nguyenanhtrung.schoolmanagement.util.FileUtils
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class ProfileRemoteDataSourceImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    @ApplicationContext private val context: Context
) : ProfileRemoteDataSource {

    override suspend fun getProfileDetail(fireBaseUserId: String): Resource<ProfileDetail> {
        val profileDetailTask = firestore.collection(AppKey.USER_PROFILES_PATH)
            .document(fireBaseUserId)
            .get()
            .await()
        val imageUrl = profileDetailTask[AppKey.IMAGE_PATH_FIELD_PROFILE] as String
        val birthday = profileDetailTask[AppKey.BIRTHDAY_FIELD_PROFILE_PATH] as String
        val phoneNumber = profileDetailTask[AppKey.PHONE_NUMBER_FIELD_PROFILE_PATH] as String
        val address = profileDetailTask[AppKey.ADDRESS_FIELD_PROFILE_PATH] as String
        val email = profileDetailTask[AppKey.EMAIL_FIELD_PROFILE_PATH] as String
        val profileDetail = ProfileDetail(
            imageUrl,
            birthday,
            phoneNumber,
            address,
            email
        )
        return Resource.success(profileDetail)
    }


    override suspend fun updateUserProfile(profileUpdateParam: ProfileUpdateParam): Resource<Unit> {
        val mappedProfileFields = ArrayMap<String, String>()
        with(mappedProfileFields) {
            put(AppKey.BIRTHDAY_FIELD_PROFILE_PATH, profileUpdateParam.birthday)
            put(AppKey.PHONE_NUMBER_FIELD_PROFILE_PATH, profileUpdateParam.phoneNumber)
            put(AppKey.ADDRESS_FIELD_PROFILE_PATH, profileUpdateParam.address)
            put(AppKey.EMAIL_FIELD_PROFILE_PATH, profileUpdateParam.email)
        }
        firestore.collection(AppKey.USER_PROFILES_PATH)
            .document(profileUpdateParam.fireBaseUserId)
            .update(mappedProfileFields.toMap())
            .await()
        uploadProfileImage(profileUpdateParam.fireBaseUserId, profileUpdateParam.imageUri)
        updateUserProfileStatus(profileUpdateParam.fireBaseUserId)

        return Resource.success(Unit)
    }

    private suspend fun updateUserProfileStatus(fireBaseUserId: String) {
        firestore.collection(AppKey.USERS_PATH_FIRE_STORE)
            .document(fireBaseUserId)
            .update(AppKey.PROFILE_STATUS_FIELD, true)
            .await()
    }

    private suspend fun uploadProfileImage(fireBaseUserId: String, imageUri: String) {
        val profileImageRef = firebaseStorage.reference.child(AppKey.PROFILE_IMAGES_PATH_STORAGE)
            .child(fireBaseUserId)
        val imageStream = FileUtils.getInputStreamLocalImage(context, imageUri) ?: return
        val uploadImageTask = profileImageRef.putStream(imageStream).await()
        val imageDownloadUri = uploadImageTask.storage.downloadUrl.await()
        val imagePathField = ArrayMap<String, String>()
        imagePathField[AppKey.IMAGE_PATH_FIELD_PROFILE] = imageDownloadUri.toString()
        firestore.collection(AppKey.USER_PROFILES_PATH)
            .document(fireBaseUserId)
            .update(imagePathField.toMap())
            .await()
    }
}