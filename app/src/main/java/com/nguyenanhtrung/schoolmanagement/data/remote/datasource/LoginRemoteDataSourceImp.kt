package com.nguyenanhtrung.schoolmanagement.data.remote.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.util.NetworkBoundResources
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRemoteDataSourceImp @Inject constructor(private val firebaseAuth: FirebaseAuth) : LoginRemoteDataSource {

    override suspend fun loginAsync(
        loginPair: Pair<String,String>,
        result: MutableLiveData<ResultModel<Boolean>>
    ) {
        val networkBoundResource = object : NetworkBoundResources<Pair<String, String>, Boolean>(loginPair, result) {
            override fun shouldFetchFromServer(params: Pair<String, String>): Boolean = true

            override suspend fun callApi(): ResultModel<Boolean> {
                val taskSnapshot = firebaseAuth.signInWithEmailAndPassword(params.first, params.second).await()
                if (taskSnapshot != null) {
                    Log.d("Login","LoginCall")
                    return ResultModel.Success(true)
                }
                return ResultModel.Failure("Có lỗi xảy ra từ máy chủ")
            }

            override fun shouldSaveToLocal(params: Pair<String, String>): Boolean = false

            override suspend fun saveToLocal(output: Boolean){}

            override suspend fun loadFromLocal(params: Pair<String, String>): ResultModel<Boolean> = ResultModel.Loading
        }

        networkBoundResource.createCall()
    }


}