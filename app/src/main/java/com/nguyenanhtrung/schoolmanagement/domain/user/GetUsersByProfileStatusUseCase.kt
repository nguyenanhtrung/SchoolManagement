package com.nguyenanhtrung.schoolmanagement.domain.user

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileFilter
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.profile.ProfileRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.usertype.UserTypeRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import com.xwray.groupie.kotlinandroidextensions.Item
import javax.inject.Inject

class GetUsersByProfileStatusUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userTypeRepository: UserTypeRepository
) : BaseUseCase<Pair<Long, ProfileFilter>, MutableList<out Item>>() {

    private val userTypes by lazy {
        userTypeRepository.getUserTypes()
    }

    init {
        userTypes
    }

    override suspend fun execute(
        params: Pair<Long, ProfileFilter>,
        resultLiveData: MutableLiveData<Resource<MutableList<out Item>>>
    ) {
        val lastUserId = params.first
        if (lastUserId < 0) {
            profileRepository.getUsersByProfileFilter(Pair(params.second, userTypes), resultLiveData)
        } else {
            profileRepository.getPagingUserByProfileFilter(
                lastUserId,
                Pair(params.second, userTypes),
                resultLiveData
            )
        }
    }
}