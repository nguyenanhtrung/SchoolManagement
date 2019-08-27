package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.schoolroom

import androidx.collection.arrayMapOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.entity.SchoolRoomEntity
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomItem
import com.nguyenanhtrung.schoolmanagement.data.remote.model.CreateSchoolRoomParams
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
        val schoolRooms = mapToSchoolRoomItem(querySnapshot)
        return Resource.success(schoolRooms.toMutableList())
    }

    private fun mapToSchoolRoomItem(querySnapshot: QuerySnapshot): MutableList<SchoolRoomItem> {
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
        return schoolRooms.toMutableList()
    }

    override suspend fun getPagingSchoolRoomsAsync(lastRoomId: Long): Resource<MutableList<out Item>> {
        val lastDocumentSnapshot = firestore.collection(AppKey.SCHOOL_ROOMS_PATH)
            .whereEqualTo(AppKey.ROOM_ID_FIELD_SCHOOL_ROOMS, lastRoomId)
            .get().await()
        val querySnapshot = firestore.collection(AppKey.SCHOOL_ROOMS_PATH)
            .orderBy(AppKey.ROOM_ID_FIELD_SCHOOL_ROOMS)
            .startAfter(lastDocumentSnapshot)
            .limit(LIMIT)
            .get()
            .await()

        if (querySnapshot.isEmpty) {
            return Resource.completed()
        }
        return Resource.success(mapToSchoolRoomItem(querySnapshot))
    }

    override suspend fun createSchoolRoom(schoolRoomParams: CreateSchoolRoomParams): Resource<SchoolRoom> {
        val fields = arrayMapOf<String, Any>()
        val newRoomId = schoolRoomParams.roomId + 1
        fields[AppKey.ROOM_ID_FIELD_SCHOOL_ROOMS] = newRoomId
        fields[AppKey.NAME_FIELD_SCHOOL_ROOMS] = schoolRoomParams.roomName
        fields[AppKey.ROOM_NUMBER_FIELD_SCHOOL_ROOMS] = schoolRoomParams.roomNumber
        fields[AppKey.IS_OFFICE_ROOM_FIELD_SCHOOL_ROOMS] = schoolRoomParams.isOfficeRoom

        val taskSnapshot = firestore.collection(AppKey.SCHOOL_ROOMS_PATH)
            .document()
        val newDocumentId = taskSnapshot.id
        taskSnapshot.set(fields)
            .await()


        val schoolRoom = SchoolRoom(
            newDocumentId,
            newRoomId,
            schoolRoomParams.roomNumber,
            schoolRoomParams.roomName,
            schoolRoomParams.isOfficeRoom
        )
        return Resource.success(schoolRoom)
    }

}