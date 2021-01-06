package com.phil.deeplens.config

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    fun csvMapper(): CsvMapper {
        val csvMapper = CsvMapper()
        csvMapper.registerModule(KotlinModule())
        csvMapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        return csvMapper

    }
}