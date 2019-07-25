package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user

import android.content.Context
import android.util.ArrayMap
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertype.UserTypeLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.model.CreateAccountParam
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserItem
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.userid.UserIdRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserCloudStore
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import com.nguyenanhtrung.schoolmanagement.util.AppKey.Companion.USERS_PATH_FIRE_STORE
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class UserRemoteDataSourceImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context,
    private val userTypeLocalDataSource: UserTypeLocalDataSource,
    private val userIdRemoteDataSource: UserIdRemoteDataSource
) : UserRemoteDataSource {

    companion object {
        private const val USERS_LIMIT = 10L
    }

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
            userCloudStore.avatarPath,
            userCloudStore.userName
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
            registerNewUser(email, password)
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
            updateUserStatus(createAccountParam.id)
            return userIdRemoteDataSource.setMaxUserId(createAccountParam.id.toLong())
        } catch (collisionEx: com.google.firebase.auth.FirebaseAuthUserCollisionException) {
            Resource.failure(R.string.error_registered_email)
        } catch (ex: Exception) {
            Timber.d(ex)
            Resource.failure(R.string.error_create_user)
        }
    }

    private suspend fun registerNewUser(email: String, password: String) {
        val secondFirebaseAuth = createSecondFirebaseAuth()
        secondFirebaseAuth.createUserWithEmailAndPassword(email, password).await()
        secondFirebaseAuth.signOut()
    }

    private suspend fun updateUserStatus(userId: String) {
        val status = ArrayMap<String, Boolean>()
        status["status"] = false

        firestore.collection(AppKey.USER_STATUS_PATH_FIRE_STORE)
            .document(userId)
            .set(status)
            .await()
    }

    private fun createSecondFirebaseAuth(): FirebaseAuth {
        val firebaseOptions = FirebaseOptions.Builder()
            .setDatabaseUrl("https://schoolmanagement-f6cb0.firebaseio.com/")
            .setApiKey("AIzaSyDeGp0J-M3qGeh95vs4KgpJiwGodcrabvg")
            .setApplicationId("schoolmanagement-f6cb0").build()

        val firebaseApp = FirebaseApp.initializeApp(context, firebaseOptions, "SecondAuth")
        return FirebaseAuth.getInstance(firebaseApp)
    }

    override suspend fun getUsers(): Resource<List<UserItem>> {
        val querySnapshot = firestore.collection(USERS_PATH_FIRE_STORE)
            .orderBy(FieldPath.documentId(), Query.Direction.ASCENDING)
            .limit(USERS_LIMIT)
            .get()
            .await()
        val querySize = querySnapshot.size()
        if (querySize == 0) {
            return Resource.failure(R.string.title_empty_accounts)
        }
        val userItems = mapToUserItems(querySnapshot)
        return Resource.success(userItems)
    }

    private suspend fun mapToUserItems(querySnapshot: QuerySnapshot): List<UserItem> {
        return querySnapshot.map {
            val userTypeId = it["typeId"] as String
            val userType = userTypeLocalDataSource.getUserTypeById(userTypeId)
            UserItem(
                User(
                    it.id,
                    it["name"] as String,
                    userType.name,
                    userTypeId,
                    it["avatarPath"] as String,
                    it["userName"] as String
                )
            )
        }

    }
}