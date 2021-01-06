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


    val trials = listOf(
        ClinicalTrial("1", "Title 1", "Description", "3", "condition", "site", "1|2", ">18"),
        ClinicalTrial("2", "Title 2", "Description", "3", "condition", "site", "1|2", ">18")
    )

    val patients = listOf(
        Patient("1", 55, "male", "diag", "site"),
        Patient("2", 95, "female", "diag", "site")
    )

    @Test
    fun `evaluate will log trial id, title, and potential patients`() {
        val lines = tapSystemOut {
            trialEvaluator.evaluate(trials, patients)
        }.lines().dropLast(1)


        assertThat(lines).containsExactlyInAnyOrder(
            "Processing trial id 1 for condition condition with 2 potential patients",
            "Processing trial id 2 for condition condition with 2 potential patients"
        )
    }
}