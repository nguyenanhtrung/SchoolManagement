package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.usertype

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserTypeRemoteDataSourceImp @Inject constructor(private val remoteDatabase: FirebaseFirestore) : UserTypeRemoteDataSource {

}