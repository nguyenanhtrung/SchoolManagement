package com.nguyenanhtrung.schoolmanagement.domain.user

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileStatus
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.usertype.UserTypeRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class GetUsersByProfileStatusUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userTypeRepository: UserTypeRepository
) : BaseUseCase<Pair<Long, ProfileStatus>, MutableList<ProfileItem>>() {

    private val userTypes by lazy {
        userTypeRepository.getUserTypes()
    }

    init {
        userTypes
    }


    override suspend fun execute(
        params: Pair<Long, ProfileStatus>,
        resultLiveData: MutableLiveData<Resource<MutableList<ProfileItem>>>
    ) {
        val lastUserId = params.first
        if (lastUserId < 0) {
            userRepository.getUsersByProfileStatus(Pair(params.second, userTypes), resultLiveData)
        } else {
            userRepository.getPagingUserByProfileStatus(
                lastUserId,
                Pair(params.second, userTypes),
                resultLiveData
            )
        }
    }
}