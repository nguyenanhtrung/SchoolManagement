package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.task

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserTaskItem
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserTask
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import com.nguyenanhtrung.schoolmanagement.util.ResourceUtil
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class UserTaskRemoteDataSourceImp @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore
) : UserTaskRemoteDataSource {


    override suspend fun loadUserTasksAsync(userTypeId: String): Resource<List<UserTaskItem>> {
        val commonFeaturesTask = firestore.collection(AppKey.FEATURE_COMMONS_PATH)
            .get()
        val accountFeaturesTask = firestore.collection(AppKey.FEATURE_ACCOUNTS_PATH)
            .document(userTypeId)
            .collection(AppKey.FEATURES_PATH)
            .get()
        val commonFeatures = commonFeaturesTask.await().documents
        val accountFeatures = accountFeaturesTask.await().documents

        val features = mutableListOf<UserTask>()
        features.addAll(commonFeatures.map {
            UserTask(
                it.id,
                it["name"] as String,
                getIconId(it.id)
            )
        })

        features.addAll(accountFeatures.map {
            UserTask(
                it.id,
                it["name"] as String,
                getIconId(it.id)
            )
        })

        return Resource.success(features.map {
            UserTaskItem(it)
        })
    }

    private fun getIconId(taskId: String): Int {
        return try {
            val convertedTaskId = taskId.trim().toInt()
            ResourceUtil.getDrawableId(context, "ic_task_$convertedTaskId")
        } catch (ex: Exception) {
            Timber.e(ex)
            R.drawable.ic_task_490
        }
    }
}