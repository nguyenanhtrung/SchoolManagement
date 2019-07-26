package com.nguyenanhtrung.schoolmanagement.data.local.datasource.user

import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.user.UserDao
import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserInfoEntity
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import javax.inject.Inject

class UserLocalDataSourceImp @Inject constructor(private val userDao: UserDao) : UserLocalDataSource {


    override suspend fun saveUserInfo(user: User) {
        val userInfoEntity = UserInfoEntity(
            user.id,
            user.firebaseUserId,
            user.name,
            user.avatarPath,
            user.typeId,
            user.typeName,
            user.accountName
        )
        userDao.insertData(userInfoEntity)
    }

    override suspend fun getUserInfo(userId: String): Resource<User> {
        if (userId.isEmpty()) {
            return Resource.failure(R.string.error_not_found_user)
        }
        val userInfoEntity = userDao.getUserInfo(userId)
        val mappedUser = User(
            userInfoEntity.id,
            userInfoEntity.firebaseUserId,
            userInfoEntity.name,
            userInfoEntity.typeName,
            userInfoEntity.typeId,
            userInfoEntity.avatarPath,
            userInfoEntity.accountName
        )
        return Resource.success(mappedUser)
    }

    override suspend fun checkExistUserInfo(userId: String): Boolean {
        return userDao.checkExistUserInfo(userId) == 1
    }
}