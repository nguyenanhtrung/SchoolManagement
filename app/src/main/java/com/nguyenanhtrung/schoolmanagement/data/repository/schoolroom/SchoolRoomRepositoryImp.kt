package com.nguyenanhtrung.schoolmanagement.data.repository.schoolroom

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.schoolroom.SchoolRoomLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.schoolroom.SchoolRoomRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.model.CreateSchoolRoomParams
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.NetworkBoundResources
import com.xwray.groupie.kotlinandroidextensions.Item
import javax.inject.Inject

class SchoolRoomRepositoryImp @Inject constructor(
    private val schoolRoomLocalDataSource: SchoolRoomLocalDataSource,
    private val schoolRoomRemoteDataSource: SchoolRoomRemoteDataSource,
    @ApplicationContext private val context: Context
) : SchoolRoomRepository {


    override suspend fun getSchoolRoomsAsync(result: MutableLiveData<Resource<MutableList<out Item>>>) {
        object :
            NetworkBoundResources<Unit, MutableList<out Item>>(context, Unit, result) {

            override suspend fun shouldFetchFromServer(params: Unit): Boolean {
                return !schoolRoomLocalDataSource.isSchoolRoomsSaved(0)
            }

            override fun shouldSaveToLocal(params: Unit): Boolean = true
            override suspend fun saveToLocal(output: MutableList<out Item>) {
                schoolRoomLocalDataSource.saveSchoolRooms(output)
            }

            override suspend fun loadFromLocal(params: Unit): Resource<MutableList<out Item>> {
                return schoolRoomLocalDataSource.getSchoolRooms(0)
            }

            override suspend fun callApi(): Resource<MutableList<out Item>> {
                return schoolRoomRemoteDataSource.getSchoolRoomsAsync()
            }

        }.createCall()
    }

    override suspend fun getPagingSchoolRoomsAsync(
        lastRoomId: Long,
        result: MutableLiveData<Resource<MutableList<out Item>>>
    ) {
        object : NetworkBoundResources<Long, MutableList<out Item>>(context, lastRoomId, result) {

            override fun shouldShowLoading(): Boolean = false
            override fun shouldSaveToLocal(params: Long): Boolean = true
            override fun shouldLoadFromLocal(params: Long): Boolean = true

            override suspend fun shouldFetchFromServer(params: Long): Boolean {
                return schoolRoomLocalDataSource.isSchoolRoomsSaved(params.toInt())
            }

            override suspend fun loadFromLocal(params: Long): Resource<MutableList<out Item>> {
                return schoolRoomLocalDataSource.getSchoolRooms(offset = params.toInt())
            }

            override suspend fun saveToLocal(output: MutableList<out Item>) {
                schoolRoomLocalDataSource.saveSchoolRooms(output)
            }


            override suspend fun callApi(): Resource<MutableList<out Item>> {
                return schoolRoomRemoteDataSource.getPagingSchoolRoomsAsync(params)
            }
        }.createCall()
    }

    override suspend fun createSchoolRoom(
        schoolRoomParams: CreateSchoolRoomParams,
        result: MutableLiveData<Resource<SchoolRoom>>
    ) {
        object : NetworkBoundResources<CreateSchoolRoomParams, SchoolRoom>(
            context,
            schoolRoomParams,
            result
        ) {
            override fun shouldLoadFromLocal(params: CreateSchoolRoomParams): Boolean = false
            override fun shouldSaveToLocal(params: CreateSchoolRoomParams): Boolean = true

            override suspend fun saveToLocal(output: SchoolRoom) {
                schoolRoomLocalDataSource.saveSchoolRoom(output)
            }

            override suspend fun callApi(): Resource<SchoolRoom> {
                return schoolRoomRemoteDataSource.createSchoolRoom(params)
            }
        }.createCall()
    }
}