package com.nguyenanhtrung.schoolmanagement.util

class AppKey private constructor(){

    companion object {
        const val USERS_PATH_FIRE_STORE = "users"
        const val USER_TYPES_PATH_FIRE_STORE = "user_types"
        const val TASKS_PATH_FIRE_STORE = "tasks"
        const val TASK_PERMISSIONS_PATH = "task_permissions"
        const val ID_PATH_FIRE_STORE = "ids"
        const val USER_STATUS_PATH_FIRE_STORE = "user_status"
        const val USER_PROFILES_PATH = "profiles"
        const val AUTHENTICATION_PATH = "authentications"
        //
        const val USER_AVATAR_PATH_FIELD = "avatarPath"
        const val USER_NAME_FIELD = "name"
        const val USER_TYPE_ID_FIELD = "typeId"
        const val USER_ACCOUNT_NAME_FIELD = "userName"
        const val USER_ID_FIELD = "id"
        const val PROFILE_STATUS_FIELD = "profileStatus"
        //
        const val BIRTHDAY_FIELD_PROFILE_PATH = "birthday"
        const val PHONE_NUMBER_FIELD_PROFILE_PATH = "phoneNumber"
        const val ADDRESS_FIELD_PROFILE_PATH = "address"
        const val EMAIL_FIELD_PROFILE_PATH = "email"
        const val IMAGE_PATH_FIELD_PROFILE = "imagePath"
        //
        const val ID_TASK_ACCOUNT = 501
        const val ID_TASK_PROFILES = 500

        const val PROFILE_IMAGES_PATH_STORAGE = "profile_images"

    }
}