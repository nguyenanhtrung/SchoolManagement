package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user

import android.content.Context
import android.util.ArrayMap
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertype.UserTypeLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.userid.UserIdRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserCloudStore
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserDetail
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import com.nguyenanhtrung.schoolmanagement.util.AppKey.Companion.USER_COMMONS_PATH
import com.xwray.groupie.kotlinandroidextensions.Item
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
        private const val USERS_LIMIT = 8L
    }

    override suspend fun getUserPassword(fireBaseUserId: String): Resource<String> {
        val documentSnapshot = firestore.collection(AppKey.USER_DETAILS_PATH)
            .document(fireBaseUserId)
            .get()
            .await()
        val password = documentSnapshot[AppKey.PASSWORD_FIELD_USER_DETAILS_PATH] as String
        return Resource.success(password)
    }

    override suspend fun getUserDetail(fireBaseUserId: String): Resource<UserDetail> {
        val documentSnapshot = firestore.collection(AppKey.USER_DETAILS_PATH)
            .document(fireBaseUserId)
            .get()
            .await()
        val userDetail = UserDetail(
            documentSnapshot[AppKey.EMAIL_FIELD_USER_DETAILS_PATH] as String,
            documentSnapshot[AppKey.PASSWORD_FIELD_USER_DETAILS_PATH] as String
        )
        return Resource.success(userDetail)
    }

    override suspend fun getUsers(userTypes: Map<String, String>): Resource<MutableList<out Item>> {
        val querySnapshot = firestore.collection(USER_COMMONS_PATH)
            .orderBy(AppKey.USER_ID_FIELD)
            .whereGreaterThan(AppKey.USER_ID_FIELD, 1)
            .limit(USERS_LIMIT)
            .get()
            .await()
        val querySize = querySnapshot.size()
        if (querySize == 0) {
            return Resource.empty(R.string.title_empty_accounts)
        }
        val userItems = mapToUserItems(querySnapshot, userTypes)
        return Resource.success(userItems.toMutableList())
    }




    override suspend fun changeUserPassword(
        fireBaseUserId: String,
        accountName: String,
        newPassword: String
    ): Resource<Unit> {
        val accCurrentPassword = firestore.collection(AppKey.USER_DETAILS_PATH)
            .document(fireBaseUserId)
            .get()
            .await()
        val secondFireBaseAuth = createSecondFirebaseAuth(fireBaseUserId)
        secondFireBaseAuth.signInWithEmailAndPassword(
            accountName,
            accCurrentPassword[AppKey.PASSWORD_FIELD_USER_DETAILS_PATH] as String
        ).await()
        val user = secondFireBaseAuth.currentUser
        user?.updatePassword(newPassword)
            ?.await()
        secondFireBaseAuth.signOut()
        return Resource.success(Unit)
    }

    override suspend fun updateUserInfo(updateInfoParams: UpdateAccountInfoParams): Resource<Unit> {
        val userRef = firestore.collection(USER_COMMONS_PATH)
            .document(updateInfoParams.fireBaseUserId)
        if (updateInfoParams.name.isNotEmpty()) {
            userRef.update(AppKey.USER_NAME_FIELD, updateInfoParams.name).await()
        }
        if (updateInfoParams.typeId.isNotEmpty()) {
            userRef.update(AppKey.USER_TYPE_ID_FIELD, updateInfoParams.typeId).await()
        }
        if (updateInfoParams.accountName.isNotEmpty() && updateInfoParams.password.isNotEmpty()) {
            return changeUserPassword(
                updateInfoParams.fireBaseUserId,
                updateInfoParams.accountName,
                updateInfoParams.password
            )
        }

        return Resource.success(Unit)
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
            firestore.collection(USER_COMMONS_PATH).document(userId).get().await()
        val userCloudStore = userSnapshot.toObject(UserCloudStore::class.java)
            ?: return Resource.failure(R.string.error_parse_data)

        val typeName = getUserAccountType(userCloudStore.typeId)
        val userMapped = User(
            userCloudStore.id,
            userId,
            userCloudStore.name,
            UserType(userCloudStore.typeId, typeName),
            userCloudStore.avatarPath
        )
        return Resource.success(userMapped)
    }

    private suspend fun getUserAccountType(typeId: String): String {
        val userType = userTypeLocalDataSource.getUserTypeById(typeId)
        return userType.name
    }

    override suspend fun createNewUser(createAccountParam: CreateAccountParam): Resource<String> {
        return try {
            val newFirebaseUserId = registerNewUser(createAccountParam)
            updateUserStatus(createAccountParam.id)
            userIdRemoteDataSource.setMaxUserId(createAccountParam.id.toLong())
            return Resource.success(newFirebaseUserId)
        } catch (collisionEx: com.google.firebase.auth.FirebaseAuthUserCollisionException) {
            Resource.failure(R.string.error_registered_email)
        } catch (ex: Exception) {
            Timber.d(ex)
            Resource.failure(R.string.error_create_user)
        }
    }

    private suspend fun registerNewUser(createAccountParam: CreateAccountParam): String {
        val secondFirebaseAuth = createSecondFirebaseAuth(createAccountParam.id)
        secondFirebaseAuth.createUserWithEmailAndPassword(
            createAccountParam.email,
            createAccountParam.password
        ).await()
        val newUserId = secondFirebaseAuth.currentUser?.uid ?: ""
        secondFirebaseAuth.signOut()
        val userInfo = ArrayMap<String, Any>()
        with(userInfo) {
            put(AppKey.USER_ID_FIELD, createAccountParam.id.toLong())
            put(AppKey.USER_AVATAR_PATH_FIELD, "")
            put(AppKey.USER_NAME_FIELD, createAccountParam.name)
            put(AppKey.USER_TYPE_ID_FIELD, createAccountParam.userTypeId)
            put(AppKey.PROFILE_STATUS_FIELD, false)
        }
        firestore.collection(USER_COMMONS_PATH)
            .document(newUserId)
            .set(userInfo)
            .await()
        saveUserAccount(
            newUserId,
            email = createAccountParam.email,
            password = createAccountParam.password
        )
        return newUserId
    }


    private suspend fun saveUserAccount(fireBaseUserId: String, email: String, password: String) {
        val field = ArrayMap<String, String>()
        field[AppKey.EMAIL_FIELD_USER_DETAILS_PATH] = email
        field[AppKey.PASSWORD_FIELD_USER_DETAILS_PATH] = password
        firestore.collection(AppKey.USER_DETAILS_PATH)
            .document(fireBaseUserId)
            .set(field)
            .await()
    }

    private suspend fun updateUserStatus(userId: String) {
        val status = ArrayMap<String, Boolean>()
        status["status"] = false
        firestore.collection(AppKey.USER_STATUS_PATH_FIRE_STORE)
            .document(userId)
            .set(status)
            .await()
    }

    private fun createSecondFirebaseAuth(newUserId: String): FirebaseAuth {
        val firebaseOptions = FirebaseOptions.Builder()
            .setDatabaseUrl("https://schoolmanagement-f6cb0.firebaseio.com/")
            .setApiKey("AIzaSyDeGp0J-M3qGeh95vs4KgpJiwGodcrabvg")
            .setApplicationId("schoolmanagement-f6cb0").build()

        val firebaseApp = FirebaseApp.initializeApp(context, firebaseOptions, newUserId)
        return FirebaseAuth.getInstance(firebaseApp)
    }


    private fun mapToUserItems(
        querySnapshot: QuerySnapshot,
        userTypes: Map<String, String>
    ): List<UserItem> {
        return querySnapshot.map {
            val userTypeId = it[AppKey.USER_TYPE_ID_FIELD] as String
            val userTypeName = userTypes[userTypeId] ?: ""
            UserItem(
                User(
                    it[AppKey.USER_ID_FIELD] as Long,
                    it.id,
                    it[AppKey.USER_NAME_FIELD] as String,
                    UserType(userTypeId, userTypeName),
                    it[AppKey.USER_AVATAR_PATH_FIELD] as String
                )
            )
        }
    }

    override suspend fun getPagingUsers(
        lastUserId: Long,
        userTypes: Map<String, String>
    ): Resource<MutableList<out Item>> {
        val lastDocument =
            firestore.collection(USER_COMMONS_PATH).whereEqualTo(
                AppKey.USER_ID_FIELD,
                lastUserId
            ).get().await().documents[0]
        val querySnapshot = firestore.collection(USER_COMMONS_PATH)
            .orderBy(AppKey.USER_ID_FIELD, Query.Direction.ASCENDING)
            .limit(USERS_LIMIT)
            .startAfter(lastDocument)
            .get()
            .await()
        val querySize = querySnapshot.size()
        if (querySize == 0) {
            return Resource.completed()
        }
        val userItems = mapToUserItems(querySnapshot, userTypes)
        return Resource.success(userItems.toMutableList())
    }
}