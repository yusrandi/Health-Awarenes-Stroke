package com.use.steps.models

data class Laporan(
    val id: Int? = 0,
    val user_id: Int,
    val cfs: String,
    val result: String,
    val user: User
)