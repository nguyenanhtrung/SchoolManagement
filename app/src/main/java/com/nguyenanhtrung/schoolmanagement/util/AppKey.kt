package com.nguyenanhtrung.schoolmanagement.util

class AppKey private constructor(){

    companion object {
        const val USER_COMMONS_PATH = "user_commons"
        const val USER_DETAILS_PATH = "user_details"
        const val USER_TYPES_PATH_FIRE_STORE = "user_types"
        const val FEATURES_PATH = "features"
        const val FEATURE_ACCOUNTS_PATH = "feature_accounts"
        const val FEATURE_COMMONS_PATH = "feature_commons"
        const val ID_PATH_FIRE_STORE = "custom_ids"
        const val USER_STATUS_PATH_FIRE_STORE = "user_status"
        const val SCHOOL_ROOMS_PATH = "school_rooms"
        //
        const val USER_AVATAR_PATH_FIELD = "avatar_path"
        const val USER_NAME_FIELD = "name"
        const val USER_TYPE_ID_FIELD = "type_id"
        const val USER_ID_FIELD = "id"
        const val PROFILE_STATUS_FIELD = "profile_status"

        //Profile common field
        const val PROFILE_COMMONS_PATH = "profile_commons"
        const val PROFILE_IMAGE_PATH_FIELD = "profile_image_path"
        const val PHONE_NUMBER_FIELD_PROFILE_PATH = "phone_number"
        const val PROFILE_USER_ID_FIELD = "user_id"
        const val PROFILE_USER_TYPE_ID_FIELD = "user_type_id"
        const val NAME_FIELD_PROFILE_COMMONS = "name"
        const val GENDER_FIELD_PROFILE_PATH = "gender"


        //Profile detail field
        const val PROFILE_DETAILS_PATH = "profile_details"
        const val BIRTHDAY_FIELD_PROFILE_PATH = "birthday"
        const val ADDRESS_FIELD_PROFILE_PATH = "address"
        const val EMAIL_FIELD_PROFILE_PATH = "email"
        //
        const val PASSWORD_FIELD_USER_DETAILS_PATH = "password"
        const val EMAIL_FIELD_USER_DETAILS_PATH = "email"
        //

        //
        const val NAME_FIELD_SCHOOL_ROOMS = "roomName"
        const val ROOM_ID_FIELD_SCHOOL_ROOMS = "roomId"
        const val ROOM_NUMBER_FIELD_SCHOOL_ROOMS = "roomNumber"
        const val IS_OFFICE_ROOM_FIELD_SCHOOL_ROOMS = "isOfficeRoom"
        const val CAN_MODIFY_FIELD_SCHOOL_ROOMS = "canModify"
        //
        const val ID_TASK_ACCOUNT = 600
        const val ID_TASK_PROFILES = 601
        const val ID_TASK_SCHOOL_ROOMS = 605

        const val PROFILE_IMAGES_PATH_STORAGE = "profile_images"

        //
        

    }
}