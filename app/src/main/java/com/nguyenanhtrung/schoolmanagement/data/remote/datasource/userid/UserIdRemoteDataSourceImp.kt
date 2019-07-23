package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.userid

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserIdRemoteDataSourceImp @Inject constructor(private val fireStore: FirebaseFirestore)  : UserIdRemoteDataSource {


    override suspend fun getMaxUserId(): Resource<Int> {
        val querySnapshot = fireStore.collection(AppKey.ID_PATH_FIRE_STORE)
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get().await()
        val maxId = querySnapshot.documents[0]["id"] as Int
        return Resource.success(maxId)
    }
}