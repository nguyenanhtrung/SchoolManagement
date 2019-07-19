package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserCloudStore
import com.nguyenanhtrung.schoolmanagement.util.AppKey.Companion.USERS_PATH_FIRE_STORE
import com.nguyenanhtrung.schoolmanagement.util.AppKey.Companion.USER_TYPES_PATH_FIRE_STORE
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteDataSourceImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRemoteDataSource {

    override suspend fun getUserId(): String {
        val currentUser =
            firebaseAuth.currentUser ?: return ""
        return currentUser.uid
    }

    override suspend fun loadUserInfoAsync(): Resource<User> {
        val currentUser =
            firebaseAuth.currentUser ?: return Resource.failure(R.string.error_not_found_user)
        val userId = currentUser.uid
        val userSnapshot = firestore.collection(USERS_PATH_FIRE_STORE).document(userId).get().await()
        val userCloudStore = userSnapshot.toObject(UserCloudStore::class.java)
            ?: return Resource.failure(R.string.error_parse_data)
        val userMapped = User(
            userId,
            userCloudStore.name,
            getUserAccountType(userCloudStore.typeId),
            userCloudStore.typeId,
            userCloudStore.avatarPath
        )
        return Resource.success(userMapped)
    }

    private suspend fun getUserAccountType(typeId: String): String {
        val accountTypeSnapshot =
            firestore.collection(USER_TYPES_PATH_FIRE_STORE).document(typeId).get().await()
        return accountTypeSnapshot.get("name") as String
    }



}