package com.phil.deeplens.model

data class ClinicalTrial(
    val trialId: String,
    val title: String,
    val description: String,
    val phase: String,
    val condition: String,
    val anatomicSite: String,
    val diagnoses: String,
    val ageRequirement: String
)
