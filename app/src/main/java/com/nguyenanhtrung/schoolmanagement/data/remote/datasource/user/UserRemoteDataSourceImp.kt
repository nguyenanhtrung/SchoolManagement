package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user

import android.util.ArrayMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertype.UserTypeLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.model.CreateAccountParam
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserCloudStore
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import com.nguyenanhtrung.schoolmanagement.util.AppKey.Companion.USERS_PATH_FIRE_STORE
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteDataSourceImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userTypeLocalDataSource: UserTypeLocalDataSource
) : UserRemoteDataSource {

    override suspend fun sendResetPassword(email: String): Resource<Int> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            return Resource.success(R.string.title_send_password_success)
        } catch (ex: Exception) {
            Resource.exception(ex)
        }
    }


    override suspend fun getUserId(): String {
        val currentUser =
            firebaseAuth.currentUser ?: return ""
        return currentUser.uid
    }

    override suspend fun loadUserInfoAsync(): Resource<User> {
        val currentUser =
            firebaseAuth.currentUser ?: return Resource.failure(R.string.error_not_found_user)
        val userId = currentUser.uid
        val userSnapshot =
            firestore.collection(USERS_PATH_FIRE_STORE).document(userId).get().await()
        val userCloudStore = userSnapshot.toObject(UserCloudStore::class.java)
            ?: return Resource.failure(R.string.error_parse_data)

        val typeName = getUserAccountType(userCloudStore.typeId)
        val userMapped = User(
            userId,
            userCloudStore.name,
            typeName,
            userCloudStore.typeId,
            userCloudStore.avatarPath
        )
        return Resource.success(userMapped)
    }

    private suspend fun getUserAccountType(typeId: String): String {
        val userType = userTypeLocalDataSource.getUserTypeById(typeId)
        return userType.name
    }

    override suspend fun createNewUser(createAccountParam: CreateAccountParam): Resource<Unit> {
        return try {
            val email = createAccountParam.email
            val password = createAccountParam.password
            firebaseAuth.signInWithEmailAndPassword(email, password).await()

            val userInfo = ArrayMap<String, String>()
            with(userInfo) {
                put(AppKey.USER_AVATAR_PATH_FIELD, "")
                put(AppKey.USER_NAME_FIELD, createAccountParam.name)
                put(AppKey.USER_TYPE_ID_FIELD, createAccountParam.userTypeId)
                put(AppKey.USER_ACCOUNT_NAME_FIELD, email)
            }
            firestore.collection(USERS_PATH_FIRE_STORE)
                .document(createAccountParam.id)
                .set(userInfo)
                .await()
            return Resource.success(Unit)
        } catch (ex: Exception) {
            Resource.failure(R.string.error_create_user)
        }


    }

}