package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.profile

import android.content.Context
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import com.nguyenanhtrung.schoolmanagement.util.FileUtils
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRemoteDataSourceImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    @ApplicationContext private val context: Context
) : ProfileRemoteDataSource {

    override suspend fun saveProfileModified(profileModificationParams: ProfileModificationParams): Resource<String> {
        firestore.collection(AppKey.USER_PROFILES_PATH)
            .document(profileModificationParams.firebaseUserId)
            .update(profileModificationParams.modifiedFields)
            .await()

        if (profileModificationParams.imagePath.isEmpty()) {
            return Resource.success("")
        }

        val newImageUri = uploadProfileImage(
            profileModificationParams.firebaseUserId,
            profileModificationParams.imagePath
        )
        return Resource.success(newImageUri)
    }

    override suspend fun getProfileDetail(fireBaseUserId: String): Resource<ProfileDetail> {
        val profileDetailTask = firestore.collection(AppKey.USER_PROFILES_PATH)
            .document(fireBaseUserId)
            .get()
            .await()
        val birthday = profileDetailTask[AppKey.BIRTHDAY_FIELD_PROFILE_PATH] as String
        val phoneNumber = profileDetailTask[AppKey.PHONE_NUMBER_FIELD_PROFILE_PATH] as String
        val address = profileDetailTask[AppKey.ADDRESS_FIELD_PROFILE_PATH] as String
        val email = profileDetailTask[AppKey.EMAIL_FIELD_PROFILE_PATH] as String
        val genderId = profileDetailTask[AppKey.GENDER_FIELD_PROFILE_PATH] as Long

        val gender = Gender.getGenreById(genderId.toInt())
        val profileDetail = ProfileDetail(
            birthday,
            phoneNumber,
            address,
            email,
            gender
        )
        return Resource.success(profileDetail)
    }


    override suspend fun updateUserProfile(profileUpdateParam: ProfileUpdateParam): Resource<String> {
        val mappedProfileFields = ArrayMap<String, Any>()
        with(mappedProfileFields) {
            put(AppKey.BIRTHDAY_FIELD_PROFILE_PATH, profileUpdateParam.birthday)
            put(AppKey.PHONE_NUMBER_FIELD_PROFILE_PATH, profileUpdateParam.phoneNumber)
            put(AppKey.ADDRESS_FIELD_PROFILE_PATH, profileUpdateParam.address)
            put(AppKey.EMAIL_FIELD_PROFILE_PATH, profileUpdateParam.email)
            put(AppKey.GENDER_FIELD_PROFILE_PATH, profileUpdateParam.gender.ordinal.toLong())
        }
        firestore.collection(AppKey.USER_PROFILES_PATH)
            .document(profileUpdateParam.fireBaseUserId)
            .set(mappedProfileFields.toMap())
            .await()
        updateUserProfileStatus(profileUpdateParam.fireBaseUserId)
        val imageUri =
            uploadProfileImage(profileUpdateParam.fireBaseUserId, profileUpdateParam.imageUri)

        return Resource.success(imageUri)
    }

    private suspend fun updateUserProfileStatus(fireBaseUserId: String) {
        firestore.collection(AppKey.USERS_PATH_FIRE_STORE)
            .document(fireBaseUserId)
            .update(AppKey.PROFILE_STATUS_FIELD, true)
            .await()
    }

    private suspend fun uploadProfileImage(fireBaseUserId: String, imageUri: String): String {
        val profileImageRef = firebaseStorage.reference.child(AppKey.PROFILE_IMAGES_PATH_STORAGE)
            .child(fireBaseUserId)
        val imageStream = FileUtils.getInputStreamLocalImage(context, imageUri) ?: return ""
        val uploadImageTask = profileImageRef.putStream(imageStream).await()
        val imageDownloadUri = uploadImageTask.storage.downloadUrl.await()
        val imagePathField = ArrayMap<String, String>()
        imagePathField[AppKey.PROFILE_IMAGE_PATH_FIELD] = imageDownloadUri.toString()
        firestore.collection(AppKey.USERS_PATH_FIRE_STORE)
            .document(fireBaseUserId)
            .update(imagePathField.toMap())
            .await()
        return imageDownloadUri.toString()
    }
}