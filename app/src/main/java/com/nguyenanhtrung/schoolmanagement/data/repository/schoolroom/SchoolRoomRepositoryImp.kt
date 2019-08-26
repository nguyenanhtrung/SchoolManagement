package com.nguyenanhtrung.schoolmanagement.data.repository.schoolroom

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.schoolroom.SchoolRoomLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomItem
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.schoolroom.SchoolRoomRemoteDataSource
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
}