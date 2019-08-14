package com.nguyenanhtrung.schoolmanagement.data.local.model

enum class Gender {
    MALE, FEMALE;

    companion object {
        fun getGenreById(id: Int): Gender {
            return when (id) {
                MALE.ordinal -> MALE
                FEMALE.ordinal -> FEMALE
                else -> MALE
            }
        }
    }


}