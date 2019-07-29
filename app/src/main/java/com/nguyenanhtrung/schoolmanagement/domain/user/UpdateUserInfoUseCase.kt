package com.nguyenanhtrung.schoolmanagement.domain.user

import androidx.collection.ArrayMap
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(private val userRepository: UserRepository) :
    BaseUseCase<Pair<String, ArrayMap<String, String>>, Unit>() {


    override suspend fun execute(
        params: Pair<String, ArrayMap<String, String>>,
        resultLiveData: MutableLiveData<Resource<Unit>>
    ) {
        userRepository.updateUserInfo(resultLiveData, params)
    }

}