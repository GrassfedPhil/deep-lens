package com.phil.deeplens

import com.phil.deeplens.model.ClinicalTrial
import com.phil.deeplens.model.Patient
import com.phil.deeplens.service.CSVParser
import com.phil.deeplens.service.ReportService
import com.phil.deeplens.service.TrialEvaluator
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.io.File

@SpringBootApplication
class DeepLensApplication


fun main(args: Array<String>) {
    runApplication<DeepLensApplication>(*args)
}

@Component
class Runner(
    val resourceLoader: ResourceLoader,
    val csvParser: CSVParser,
    val trialEvaluator: TrialEvaluator,
    val reportService: ReportService
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (args.size != 2) {
            println("Please provide the absolute path for the trial file and the patient file")
        } else {
            val trialFile = File(args[0]!!)
            val patientFile = File(args[1]!!)
            val clinicalTrials = csvParser.parseToObject<ClinicalTrial>(trialFile)
            val patients = csvParser.parseToObject<Patient>(patientFile)
            val evaluations = trialEvaluator.evaluate(clinicalTrials, patients)
            reportService.reportResults(evaluations)
        }
    }

}

