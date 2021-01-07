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


    val trial1 = ClinicalTrial("1", "Title 1", "Description", "3", "condition", "site1", "ONE|TWO", ">18")
    val trial2 = ClinicalTrial("2", "Title 2", "Description", "3", "condition", "site2", "One|Two", ">18")

    val patient1 = Patient("1", 55, "male", "one", "Site1")
    val patient2 = Patient("2", 95, "female", "two", "sIte2")
    val patient3 = Patient("3", 15, "female", "two", "sitE2")
    val patient4 = Patient("4", 65, "male", "two", "siTe1")

    @Test
    fun `evaluate will log trial id, title, and potential patients`() {
        val trials = listOf(trial1, trial2)
        val patients = listOf(patient1, patient2)
        val lines = tapSystemOut {
            trialEvaluator.evaluate(trials, patients)
        }.lines().dropLast(1)


        assertThat(lines).contains(
            "Processing trial id 1 for condition condition with 2 potential patients",
            "Processing trial id 2 for condition condition with 2 potential patients"
        )
    }

    @Test
    fun `evaluate will print patient information`() {
        val trials = listOf(trial1)
        val patients = listOf(patient1, patient2)
        val lines = tapSystemOut {
            trialEvaluator.evaluate(trials, patients)
        }.lines().dropLast(1)

        assertThat(lines).containsExactlyInAnyOrder(
            "Processing trial id 1 for condition condition with 2 potential patients",
            "Processing patient ${patient1.patientId}, age ${patient1.age}, gender ${patient1.gender}, with diagnosis ${patient1.diagnosis}, for trial ${trial1.trialId}",
            "Processing patient ${patient2.patientId}, age ${patient2.age}, gender ${patient2.gender}, with diagnosis ${patient2.diagnosis}, for trial ${trial1.trialId}",
        )
    }

    @Test
    fun `checkAgeRequirement functions as expected`() {
        val greaterThan = ClinicalTrial("1", "Title 1", "Description", "3", "condition", "site1", "1|2", ">18")
        val greaterThanEqual = ClinicalTrial("1", "Title 1", "Description", "3", "condition", "site1", "1|2", ">=18")
        val lesserThan = ClinicalTrial("1", "Title 1", "Description", "3", "condition", "site1", "1|2", "<18")
        val lesserThanEqual = ClinicalTrial("1", "Title 1", "Description", "3", "condition", "site1", "1|2", "<=18")
        val unknown = ClinicalTrial("1", "Title 1", "Description", "3", "condition", "site1", "1|2", "=18")
        val unknown2 = ClinicalTrial("1", "Title 1", "Description", "3", "condition", "site1", "1|2", "%jjjj")
        val blank = ClinicalTrial("1", "Title 1", "Description", "3", "condition", "site1", "1|2", "")

        val nineteen = Patient("1", 19, "male", "1", "site1")
        val eighteen = Patient("1", 18, "male", "1", "site1")
        val seventeen = Patient("1", 17, "male", "1", "site1")
        val patients = listOf(seventeen, eighteen, nineteen)

        val evaluateGreaterThan = trialEvaluator.evaluate(listOf(greaterThan), patients)
        assertThat(evaluateGreaterThan[0].second).containsExactlyInAnyOrder(nineteen)

        val evaluateGreaterThanEqual = trialEvaluator.evaluate(listOf(greaterThanEqual), patients)
        assertThat(evaluateGreaterThanEqual[0].second).containsExactlyInAnyOrder(nineteen, eighteen)

        val evaluateLesserThan= trialEvaluator.evaluate(listOf(lesserThan), patients)
        assertThat(evaluateLesserThan[0].second).containsExactlyInAnyOrder(seventeen)

        val evaluateLesserThanEqual = trialEvaluator.evaluate(listOf(lesserThanEqual), patients)
        assertThat(evaluateLesserThanEqual[0].second).containsExactlyInAnyOrder(seventeen, eighteen)

        val evaluateUnknown = trialEvaluator.evaluate(listOf(unknown), patients)
        assertThat(evaluateUnknown[0].second).isEmpty()

        val evaluateUnknown2 = trialEvaluator.evaluate(listOf(unknown2), patients)
        assertThat(evaluateUnknown2[0].second).isEmpty()

        val evaluateBlank = trialEvaluator.evaluate(listOf(blank), patients)
        assertThat(evaluateBlank[0].second).isEmpty()

    }

    @Test
    fun `evaluate will evaluate patients and return those that meet the trial criteria`() {
        val trials = listOf(trial1)
        val patients = listOf(patient1, patient2, patient3, patient4)

        val completedRun = trialEvaluator.evaluate(trials, patients)
        assertThat(completedRun).hasSize(1)
        assertThat(completedRun[0].first).isEqualTo(trial1)
        assertThat(completedRun[0].second).containsExactlyInAnyOrder(patient1, patient4)
    }

    @Test
    fun `evaluate will not reject a patient if their age is not provided`(){
        val blankAge = patient1.copy(age = null)

        val evaluated = trialEvaluator.evaluate(listOf(trial1), listOf(blankAge))
        assertThat(evaluated).hasSize(1)
        assertThat(evaluated[0].first).isEqualTo(trial1)
        assertThat(evaluated[0].second).containsExactly(blankAge)

    }
}