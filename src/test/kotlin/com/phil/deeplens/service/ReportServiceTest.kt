package com.phil.deeplens.service

import com.phil.deeplens.model.ClinicalTrial
import com.phil.deeplens.model.Patient
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import com.github.stefanbirkner.systemlambda.SystemLambda.*
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ReportService::class])
internal class ReportServiceTest {
    @Autowired
    lateinit var reportService: ReportService

    val trial1 = ClinicalTrial("1", "Title 1", "Description", "3", "condition", "site1", "1|2", ">18")
    val trial2 = ClinicalTrial("2", "Title 2", "Description", "3", "condition", "site2", "1|2", ">18")

    val patient1 = Patient("1", 55, "male", "one", "Site1")
    val patient2 = Patient("2", 95, "female", "two", "site2")
    val patient3 = Patient("3", 15, "female", "two", "site2")

    @Test
    fun `report will generate system output for provided trials and patients`() {
        val lines = tapSystemOut {
            reportService.reportResults(
                listOf(
                    Pair(trial1, listOf(patient1, patient2)),
                    Pair(trial2, listOf(patient3)),
                    Pair(trial2, emptyList())
                )
            )
        }.lines().dropLast(1)

        assertThat(lines).containsExactly(
            "The following 2 patients were identified for trial 1:",
            " -- Patient 1",
            " -- Patient 2",
            "The following patient was identified for trial 2:",
            " -- Patient 3",
            "No patients were identified for trial 2"
        )
    }
}