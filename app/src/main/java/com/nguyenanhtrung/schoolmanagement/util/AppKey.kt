package com.nguyenanhtrung.schoolmanagement.util

class AppKey private constructor(){

    companion object {
        const val USERS_PATH_FIRE_STORE = "users"
        const val USER_TYPES_PATH_FIRE_STORE = "user_types"
        const val TASKS_PATH_FIRE_STORE = "tasks"
        const val TASK_PERMISSIONS_PATH = "task_permissions"
        const val ID_PATH_FIRE_STORE = "ids"

        //
        const val USER_AVATAR_PATH_FIELD = "avatarPath"
        const val USER_NAME_FIELD = "name"
        const val USER_TYPE_ID_FIELD = "typeId"
        const val USER_ACCOUNT_NAME_FIELD = "userName"
        //
        const val ID_TASK_ACCOUNT = 500

    }
}