package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.schoolroom

import com.google.firebase.firestore.FirebaseFirestore
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomItem
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SchoolRoomRemoteDataSourceImp @Inject constructor(private val firestore: FirebaseFirestore) :
    SchoolRoomRemoteDataSource {

    companion object {
        const val LIMIT = 15L
    }


    override suspend fun getSchoolRoomsAsync(): Resource<MutableList<out Item>> {
        val querySnapshot = firestore.collection(AppKey.SCHOOL_ROOMS_PATH)
            .orderBy(AppKey.ROOM_ID_FIELD_SCHOOL_ROOMS)
            .limit(LIMIT)
            .get()
            .await()
        if (querySnapshot.isEmpty) {
            return Resource.empty(R.string.title_empty_rooms)
        }
        val schoolRooms = querySnapshot.documents.map {
            SchoolRoomItem(
                SchoolRoom(
                    it.id,
                    it[AppKey.ROOM_ID_FIELD_SCHOOL_ROOMS] as Long,
                    it[AppKey.ROOM_NUMBER_FIELD_SCHOOL_ROOMS] as String,
                    it[AppKey.NAME_FIELD_SCHOOL_ROOMS] as String,
                    it[AppKey.IS_OFFICE_ROOM_FIELD_SCHOOL_ROOMS] as Boolean
                )
            )
        }
        return Resource.success(schoolRooms.toMutableList())
    }
}