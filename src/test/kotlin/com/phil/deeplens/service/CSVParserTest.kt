package com.phil.deeplens.service

import com.phil.deeplens.config.Config
import com.phil.deeplens.model.ClinicalTrial
import com.phil.deeplens.model.Patient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.core.io.ResourceLoader
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [CSVParser::class])
@Import(Config::class)

internal class CSVParserTest {
    @Autowired
    lateinit var resourceLoader: ResourceLoader

    @Autowired
    lateinit var csvParser: CSVParser

    @Test
    fun `parser will generate data objects`() {
        val trialFile = resourceLoader.getResource("classpath:clinical_trial.csv").file
        val patientFile = resourceLoader.getResource("classpath:patient_feed.csv").file
        val trials = csvParser.parseToObject<ClinicalTrial>(trialFile)
        val patients = csvParser.parseToObject<Patient>(patientFile)


        assertThat(trials).containsExactly(
            ClinicalTrial(
                "trialId",
                "Title",
                "Description",
                "3",
                "condition",
                "site",
                "1|2",
                ">18"
            ))

        assertThat(patients).containsExactly(
            Patient(
                "id",
                56,
                "male",
                "diagnosis",
                "site"
            )
        )
    }

    @Test
    fun `parser will return empty list when file is empty`(){
        val patientFile = resourceLoader.getResource("classpath:empty_patient_feed.csv").file
        val patients = csvParser.parseToObject<Patient>(patientFile)
        assertThat(patients).isEmpty()
    }
}