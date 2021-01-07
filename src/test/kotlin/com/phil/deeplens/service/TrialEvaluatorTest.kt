package com.phil.deeplens.service

import com.phil.deeplens.model.ClinicalTrial
import com.phil.deeplens.model.Patient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import com.github.stefanbirkner.systemlambda.SystemLambda.*


@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class TrialEvaluatorTest {
    @Autowired
    lateinit var trialEvaluator: TrialEvaluator


    val trial1 = ClinicalTrial("1", "Title 1", "Description", "3", "condition", "site", "1|2", ">18")
    val trial2 = ClinicalTrial("2", "Title 2", "Description", "3", "condition", "site", "1|2", ">18")
    val trials = listOf(trial1, trial2)

    val patient1 = Patient("1", 55, "male", "diag1", "site1")
    val patient2 = Patient("2", 95, "female", "diag2", "site2")
    val patients = listOf(patient1, patient2)

    @Test
    fun `evaluate will log trial id, title, and potential patients`() {
        val lines = tapSystemOut {
            trialEvaluator.evaluate(trials, patients)
        }.lines().dropLast(1)


        assertThat(lines).contains(
            "Processing trial id 1 for condition condition with 2 potential patients",
            "Processing trial id 2 for condition condition with 2 potential patients"
        )
    }

    @Test
    fun `evaluate will print patient information`(){
        val lines = tapSystemOut {
            trialEvaluator.evaluate(trials.dropLast(1), patients)
        }.lines().dropLast(1)

        assertThat(lines).containsExactlyInAnyOrder(
            "Processing trial id 1 for condition condition with 2 potential patients",
            "Processing patient ${patient1.patientId}, age ${patient1.age}, gender ${patient1.gender}, with diagnosis ${patient1.diagnosis}, for trial ${trial1.trialId}",
            "Processing patient ${patient2.patientId}, age ${patient2.age}, gender ${patient2.gender}, with diagnosis ${patient2.diagnosis}, for trial ${trial1.trialId}",
        )
    }
}