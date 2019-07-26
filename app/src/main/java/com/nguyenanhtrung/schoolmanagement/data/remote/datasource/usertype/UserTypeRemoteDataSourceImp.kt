package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.usertype

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserType
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class UserTypeRemoteDataSourceImp @Inject constructor(
    private val fireStore: FirebaseFirestore
) : UserTypeRemoteDataSource {

    override suspend fun getUserTypes(): Resource<List<UserType>> {
        val userTypesTask = fireStore.collection(AppKey.USER_TYPES_PATH_FIRE_STORE)
            .get()
            .await()
        val mappedUserTypes = userTypesTask.map {
            Timber.d("Name = ${it["name"].toString()}")
            UserType(it.id, it["name"].toString())
        }
        return Resource.success(mappedUserTypes)
    }

}