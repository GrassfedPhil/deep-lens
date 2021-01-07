package com.phil.deeplens.service

import com.phil.deeplens.model.ClinicalTrial
import com.phil.deeplens.model.Patient
import org.springframework.stereotype.Service

@Service
class TrialEvaluator {
    fun evaluate(trials: List<ClinicalTrial>, patients: List<Patient>): List<Pair<ClinicalTrial, List<Patient>>> {
        return trials.map { trial ->
            println("Processing trial id ${trial.trialId} for condition ${trial.condition} with ${patients.size} potential patients")
            val eligiblePatients = patients.mapNotNull { patient ->
                evaluatePatientForTrial(trial, patient)
            }
            Pair(trial, eligiblePatients)
        }
    }

    private fun evaluatePatientForTrial(trial: ClinicalTrial, patient: Patient): Patient? {
        println("Processing patient ${patient.patientId}, age ${patient.age}, gender ${patient.gender}, with diagnosis ${patient.diagnosis}, for trial ${trial.trialId}")
        if (checkAgeRequirement(trial.ageRequirement, patient.age)
            && checkDiagnosis(trial.diagnoses, patient.diagnosis)
            && checkAnatomicSite(trial.anatomicSite, patient.anatomicSite)
        ) {
            return patient
        }
        return null
    }

    private fun checkAnatomicSite(trialAnatomicSite: String, patientAnatomicSite: String): Boolean {
        return trialAnatomicSite.equals(patientAnatomicSite, ignoreCase = true)
    }

    private fun checkDiagnosis(trialDiagnosis: String, patientDiagnosis: String): Boolean {
        val diagnoses = trialDiagnosis.split("|").map { it.toLowerCase() }
        return diagnoses.contains(patientDiagnosis.toLowerCase())
    }

    private fun checkAgeRequirement(ageRequirement: String, age: Int): Boolean {
        val (operator, ageReq) = Regex("(\\D*)(\\d*)").find(ageRequirement)!!.destructured
        return when (operator) {
            ">" -> age > ageReq.toInt()
            "<" ->  age < ageReq.toInt()
            ">=" -> age >= ageReq.toInt()
            "<=" -> age <= ageReq.toInt()
            else -> {
                println("OPERATOR DID NOT MATCH!!")
                false
            }
        }
    }
}