package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.LoginState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.NetworkBoundResources
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class LoginRemoteDataSourceImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext
    private val context: Context
) : LoginRemoteDataSource {

    override suspend fun logOut(result: MutableLiveData<Resource<Unit>>) {
        firebaseAuth.signOut()
        result.value = Resource.success(Unit)
    }

    override suspend fun loginAsync(
        loginPair: Pair<String, String>,
        result: MutableLiveData<Resource<Boolean>>
    ) {
        object : NetworkBoundResources<Pair<String, String>, Boolean>(context, loginPair, result) {

            override suspend fun callApi(): Resource<Boolean> {
                try {
                    val taskSnapshot =
                        firebaseAuth.signInWithEmailAndPassword(params.first, params.second).await()
                    if (taskSnapshot != null) {
                        return Resource.success(true)
                    }
                    return Resource.success(false)
                } catch (exception: Exception) {
                    return Resource.failure(R.string.error_wrong_login)
                }
            }

        }.createCall()
    }

        override suspend fun checkAlreadyLoginAsync(result: MutableLiveData<Resource<LoginState>>) {
            object : NetworkBoundResources<Unit, LoginState>(context, Unit, result) {
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