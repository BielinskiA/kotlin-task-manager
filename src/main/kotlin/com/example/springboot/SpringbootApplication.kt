package com.example.springboot

import com.example.springboot.Tables.TaskFileTable
import com.example.springboot.Tables.Users
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class SpringbootApplication

fun main (args: Array<String>) {
	runApplication<SpringbootApplication>(*args)
}