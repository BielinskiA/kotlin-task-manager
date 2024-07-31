package com.example.springboot

import RandomUserResponse
import com.example.springboot.RestTemplate.RestTemplateObject
import com.example.springboot.Tables.DataBaseControl
import com.example.springboot.Tables.Users
import com.example.springboot.endpoints.hash
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.getForObject


@Component
class ScheduledTasks {
    private val logger = LoggerFactory.getLogger(ScheduledTasks::class.java)
    private val mapper = jacksonObjectMapper()

    @Scheduled(fixedRate = 900000)
    fun fetchAndSaveRandomUser() {
        try {
            val response = RestTemplateObject.restTemplate.getForObject<String>("https://randomuser.me/api/")
            val randomUserResponse = mapper.readValue<RandomUserResponse>(response)

            val photoUrl = randomUserResponse.results.first().picture.large
            val photosBytes: ByteArray? = RestTemplateObject.restTemplate.exchange(photoUrl, HttpMethod.GET, null, ByteArray::class.java).body

            if (photosBytes != null) {
                val userData = randomUserResponse.results.first()
                val name = userData.name.first
                val surname = userData.name.last
                val mail = userData.email
                val password = userData.login.password

                val userId = transaction(DataBaseControl.connect) {
                    Users.insert {
                        it[Users.name] = name
                        it[Users.surname] = surname
                        it[Users.mail] = mail
                        it[Users.password] = hash(password)
                        it[Users.photo] = ExposedBlob(photosBytes)
                    }[Users.id]
                }

                logger.info("Użytkownik o ID: $userId")
            } else {
                logger.warn("Nie udało się pobrać zdjęcia użytkownika.")
            }
        } catch (e: Exception) {
            logger.error("Nie udało się pobrać użytkownika", e)
        }
    }
}


