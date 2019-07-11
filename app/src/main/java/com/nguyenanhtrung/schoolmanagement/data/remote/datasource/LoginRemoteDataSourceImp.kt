package com.nguyenanhtrung.schoolmanagement.data.remote.datasource

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.LoginState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.util.NetworkBoundResources
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRemoteDataSourceImp @Inject constructor(private val firebaseAuth: FirebaseAuth) : LoginRemoteDataSource {

    override suspend fun loginAsync(
        loginPair: Pair<String,String>,
        result: MutableLiveData<Resource<Boolean>>
    ) {
        object : NetworkBoundResources<Pair<String, String>, Boolean>(loginPair, result) {

            override suspend fun callApi(): Resource<Boolean> {
                val taskSnapshot = firebaseAuth.signInWithEmailAndPassword(params.first, params.second).await()
                if (taskSnapshot != null) {
                    return Resource.success(true)
                }
                return Resource.failure(R.string.error_wrong_login)
            }
        }.apply {
            createCall()
        }

    }

    override suspend fun checkAlreadyLoginAsync(result: MutableLiveData<Resource<LoginState>>) {
        object : NetworkBoundResources<Unit, LoginState>(Unit, result) {
            override suspend fun callApi(): Resource<LoginState> {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    return Resource.success(LoginState.ALREADY_LOGIN)
                }
                return Resource.success(LoginState.FIRST_LOGIN)
            }
        }.apply {
            createCall()
        }
    }


}