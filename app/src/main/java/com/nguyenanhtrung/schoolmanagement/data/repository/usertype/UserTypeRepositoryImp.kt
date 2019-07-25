package com.nguyenanhtrung.schoolmanagement.data.repository.usertype

import android.content.Context
import android.util.LruCache
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertype.UserTypeLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserTypeEntity
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.Status
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserType
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.usertype.UserTypeRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.NetworkBoundResources
import javax.inject.Inject

class UserTypeRepositoryImp @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userTypeLocalDataSource: UserTypeLocalDataSource,
    private val userTypeRemoteDataSource: UserTypeRemoteDataSource
) : UserTypeRepository {


    override fun getUserTypes(): Map<String, String> {
        return userTypeCache.snapshot()
    }

    private val userTypeCache by lazy {
        LruCache<String, String>(8)
    }

    override suspend fun loadUserTypes(
        shouldLoadLocal: Boolean,
        result: MutableLiveData<Resource<List<UserType>>>
    ) {
        object : NetworkBoundResources<Unit, List<UserType>>(context, Unit, result) {
            override suspend fun shouldFetchFromServer(params: Unit): Boolean {
                return !userTypeLocalDataSource.checkUserTypesSaved()
            }

            override fun shouldSaveToLocal(params: Unit): Boolean = true
            override fun shouldLoadFromLocal(params: Unit): Boolean = shouldLoadLocal

            override suspend fun callApi(): Resource<List<UserType>> {
                val userTypesResource = userTypeRemoteDataSource.getUserTypes()
                if (userTypesResource.status == Status.SUCCESS) {
                    val userTypes = userTypesResource.data
                    userTypes?.let {
                        saveToMemoryCache(userTypes)
                    }
                }
                return userTypeRemoteDataSource.getUserTypes()
            }

            override suspend fun loadFromLocal(params: Unit): Resource<List<UserType>> {
                val userTypes = userTypeLocalDataSource.getUserTypes()
                saveToMemoryCache(userTypes)
                return Resource.success(userTypes)
            }

            override suspend fun saveToLocal(output: List<UserType>) {
                val userTypeEntities = output.map {
                    UserTypeEntity(it.id, it.name)
                }
                userTypeLocalDataSource.saveUserTypes(userTypeEntities)
            }
        }.createCall()
    }

    private fun saveToMemoryCache(userTypes: List<UserType>) {
        if (userTypeCache.maxSize() < userTypes.size) {
            userTypeCache.resize(userTypes.size)
        }
        if (userTypeCache.size() == 0) {
            userTypes.forEach {
                userTypeCache.put(it.id, it.name)
            }
        } else {
            userTypeCache.evictAll()
            userTypes.forEach {
                userTypeCache.put(it.id, it.name)
            }
        }
    }
}