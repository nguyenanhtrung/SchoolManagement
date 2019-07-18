package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserStatus
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserCloudStore
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.AppKey.Companion.USERS_PATH_FIREBASE
import com.nguyenanhtrung.schoolmanagement.util.AppKey.Companion.USER_TYPES_PATH_FIREBASE
import com.nguyenanhtrung.schoolmanagement.util.NetworkBoundResources
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteDataSourceImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : UserRemoteDataSource {


    override suspend fun loadUserInfoAsync(result: MutableLiveData<Resource<User>>) {
        object : NetworkBoundResources<Unit, User>(context, Unit, result = result) {
            override suspend fun callApi(): Resource<User> {
                val currentUser =
                    firebaseAuth.currentUser ?: return Resource.failure(R.string.error_not_found_user)
                val userId = currentUser.uid
                val userSnapshot = firestore.collection(USERS_PATH_FIREBASE).document(userId).get().await()
                val userCloudStore = userSnapshot.toObject(UserCloudStore::class.java)
                    ?: return Resource.failure(R.string.error_parse_data)
                val userMapped = User(
                    userCloudStore.name,
                    mapUserStatus(userCloudStore.status),
                    getUserAccountType(userCloudStore.typeId),
                    userCloudStore.typeId,
                    userCloudStore.avatarPath
                )
                return Resource.success(userMapped)
            }
        }.createCall()
    }

    private suspend fun getUserAccountType(typeId: String): String {
        val accountTypeSnapshot =
            firestore.collection(USER_TYPES_PATH_FIREBASE).document(typeId).get().await()
        return accountTypeSnapshot.get("name") as String
    }

    private fun mapUserStatus(status: String): UserStatus {
        return when (status) {
            "online" -> UserStatus.ONLINE
            "offline" -> UserStatus.OFFLINE
            else -> UserStatus.OFFLINE
        }
    }


}