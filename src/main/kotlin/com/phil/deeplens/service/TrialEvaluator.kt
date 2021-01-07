package com.phil.deeplens.service

import com.phil.deeplens.model.ClinicalTrial
import com.phil.deeplens.model.Patient
import org.springframework.stereotype.Service

@Service
class TrialEvaluator {
    fun evaluate(trials: List<ClinicalTrial>, patients: List<Patient>) {
        trials.forEach { trial ->
            println("Processing trial id ${trial.trialId} for condition ${trial.condition} with ${patients.size} potential patients")
            patients.forEach { patient ->
                evaluatePatientForTrial(trial, patient)
            }
        }
    }

    private fun evaluatePatientForTrial(trial: ClinicalTrial, patient: Patient) {
        println("Processing patient ${patient.patientId}, age ${patient.age}, gender ${patient.gender}, with diagnosis ${patient.diagnosis}, for trial ${trial.trialId}")
    }
}