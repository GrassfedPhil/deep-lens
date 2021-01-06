package com.phil.deeplens.service

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import org.springframework.stereotype.Service
import java.io.File

@Service
class CSVParser(val csvMapper: CsvMapper) {
    final inline fun <reified T>parseToObject(file: File): List<T> {
        val schema = csvMapper.schemaWithHeader()
        return csvMapper.readerFor(T::class.java).with(schema)
            .readValues<T>(file)
            .readAll()
            .orEmpty()
    }

}