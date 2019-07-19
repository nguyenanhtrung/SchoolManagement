package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.task

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserTaskItem
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserTask
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import com.nguyenanhtrung.schoolmanagement.util.NetworkBoundResources
import com.nguyenanhtrung.schoolmanagement.util.ResourceUtil
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class UserTaskRemoteDataSourceImp @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore
) : UserTaskRemoteDataSource {


    override suspend fun loadUserTasksAsync(userTypeId: String): Resource<List<UserTaskItem>> {
        val taskResult = firestore.collection(AppKey.TASKS_PATH_FIRE_STORE)
            .document(userTypeId)
            .collection(AppKey.TASK_PERMISSIONS_PATH)
            .get().await()
        val mappedTasks = taskResult.map {
            UserTask(
                it.id,
                it["name"] as String,
                getIconId(it.id)
            )
        }.reversed()
        return Resource.success(mappedTasks.map {
            UserTaskItem(it)
        })
    }

    private fun getIconId(taskId: String): Int {
        return try {
            val convertedTaskId = taskId.toInt()
            ResourceUtil.getDrawableId(context, "ic_task_$convertedTaskId")
        } catch (ex: Exception) {
            Timber.e(ex)
            R.drawable.ic_task_490
        }
    }
}