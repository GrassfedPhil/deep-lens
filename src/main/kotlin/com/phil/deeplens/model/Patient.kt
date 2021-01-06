package com.phil.deeplens.model

data class Patient(
    val patientId: String,
    val age: Int,
    val gender: String,
    val diagnosis: String,
    val anatomicSite: String
)
