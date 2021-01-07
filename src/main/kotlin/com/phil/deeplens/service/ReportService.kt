package com.phil.deeplens.service

import com.phil.deeplens.model.ClinicalTrial
import com.phil.deeplens.model.Patient
import org.springframework.stereotype.Service

@Service
class ReportService {
    fun reportResults(evaluationResults: List<Pair<ClinicalTrial, List<Patient>>>) {
        evaluationResults.forEach { evaluation ->
            val trial = evaluation.first
            val patients = evaluation.second
            when {
                patients.size == 1 -> println("The following patient was identified for trial ${trial.trialId}:")
                patients.size > 1 -> println("The following ${patients.size} patients were identified for trial ${trial.trialId}:")
                else -> println("No patients were identified for trial ${trial.trialId}")
            }
            patients.forEach {
                println(" -- Patient ${it.patientId}")
            }
        }

    }
}