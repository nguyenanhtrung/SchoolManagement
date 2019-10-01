package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.profile

import android.content.Context
import androidx.collection.ArrayMap
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import com.nguyenanhtrung.schoolmanagement.util.FileUtils
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRemoteDataSourceImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    @ApplicationContext private val context: Context
) : ProfileRemoteDataSource {

    companion object {
        private const val PROFILES_LIMIT = 10L
    }

    override suspend fun saveProfileModified(profileModificationParams: ProfileModificationParams): Resource<String> {
        firestore.collection(AppKey.USER_COMMONS_PATH)
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

    override suspend fun getProfiles(
        lastUserId: Long,
        userTypes: Map<String, String>,
        profileFilter: ProfileFilter
    ): Resource<MutableList<out Item>> {
        val querySnapshot = getProfilesQuery(profileFilter)
        val querySize = querySnapshot.size()
        if (querySize == 0) {
            return Resource.empty(R.string.title_empty_accounts)
        }
        val profileItems = mapToProfileItems(querySnapshot, userTypes)
        return Resource.success(profileItems.toMutableList())
    }

    private suspend fun getProfilesQuery(
        profileFilter: ProfileFilter,
        lastDocumentSnapshot: DocumentSnapshot? = null
    ): QuerySnapshot {
        val querySnapshot = when (profileFilter) {
            ProfileFilter.All -> firestore.collection(AppKey.PROFILE_COMMONS_PATH)
                .orderBy(AppKey.PROFILE_USER_ID_FIELD)
                .whereGreaterThan(AppKey.PROFILE_USER_ID_FIELD, 0)
                .limit(PROFILES_LIMIT)
            ProfileFilter.Updated -> firestore.collection(AppKey.PROFILE_COMMONS_PATH)
                .orderBy(AppKey.PROFILE_USER_ID_FIELD)
                .whereGreaterThan(AppKey.PROFILE_USER_ID_FIELD, 0)
                .limit(PROFILES_LIMIT)
                .whereEqualTo(AppKey.PROFILE_STATUS_FIELD, true)
            ProfileFilter.NoUpdate -> firestore.collection(AppKey.PROFILE_COMMONS_PATH)
                .orderBy(AppKey.PROFILE_USER_ID_FIELD)
                .whereGreaterThan(AppKey.PROFILE_USER_ID_FIELD, 0)
                .limit(PROFILES_LIMIT)
                .whereEqualTo(AppKey.PROFILE_STATUS_FIELD, false)
        }
        if (lastDocumentSnapshot == null) {
            return querySnapshot.get().await()
        }
        return querySnapshot.startAfter(lastDocumentSnapshot).get().await()
    }

    private fun mapToProfileItems(
        querySnapshot: QuerySnapshot,
        userTypes: Map<String, String>
    ): List<ProfileItem> {
        return querySnapshot.map {
            val userTypeId = it[AppKey.PROFILE_USER_TYPE_ID_FIELD]
            val userTypeName = userTypes[userTypeId] ?: ""
            ProfileItem(
                Profile(
                    it.id,
                    it[AppKey.USER_ID_FIELD] as Long,
                    it[AppKey.USER_NAME_FIELD] as String,
                    it[AppKey.PROFILE_STATUS_FIELD] as Boolean,
                    userTypeName,
                    it[AppKey.USER_AVATAR_PATH_FIELD] as String,
                    it[AppKey.PROFILE_IMAGE_PATH_FIELD] as String
                )
            )
        }
    }

    override suspend fun getProfileDetail(fireBaseUserId: String): Resource<ProfileDetail> {
        val profileDetailTask = firestore.collection(AppKey.USER_COMMONS_PATH)
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
        val profileCommon = ArrayMap<String, Any>()
        with(profileCommon) {
            put(AppKey.NAME_FIELD_PROFILE_COMMONS, profileUpdateParam.name)
            put(AppKey.PHONE_NUMBER_FIELD_PROFILE_PATH, profileUpdateParam.phoneNumber)
            put(AppKey.GENDER_FIELD_PROFILE_PATH, profileUpdateParam.gender.ordinal.toLong())
        }
        firestore.collection(AppKey.PROFILE_COMMONS_PATH)
            .document(profileUpdateParam.fireBaseUserId)
            .set(profileCommon)
            .await()

        val profileDetail = ArrayMap<String, Any>()
        with(profileDetail) {
            put(AppKey.BIRTHDAY_FIELD_PROFILE_PATH, profileUpdateParam.birthday)
            put(AppKey.EMAIL_FIELD_PROFILE_PATH, profileUpdateParam.email)
            put(AppKey.ADDRESS_FIELD_PROFILE_PATH, profileUpdateParam.address)
        }
        firestore.collection(AppKey.PROFILE_DETAILS_PATH)
            .document(profileUpdateParam.fireBaseUserId)
            .set(profileDetail)
            .await()


        updateUserProfileStatus(profileUpdateParam.fireBaseUserId)
        val imageUri =
            uploadProfileImage(profileUpdateParam.fireBaseUserId, profileUpdateParam.imageUri)

        return Resource.success(imageUri)
    }

    private suspend fun updateUserProfileStatus(fireBaseUserId: String) {
        firestore.collection(AppKey.USER_COMMONS_PATH)
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
        val imagePathField = ArrayMap<String, Any>()
        imagePathField[AppKey.PROFILE_IMAGE_PATH_FIELD] = imageDownloadUri.toString()
        firestore.collection(AppKey.USER_COMMONS_PATH)
            .document(fireBaseUserId)
            .update(imagePathField)
            .await()
        return imageDownloadUri.toString()
    }
}