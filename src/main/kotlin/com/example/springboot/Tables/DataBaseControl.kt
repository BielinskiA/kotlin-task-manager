package com.example.springboot.Tables

import org.jetbrains.exposed.sql.Database
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


object DataBaseControl {
    val connect = Database.connect(
        "jdbc:mariadb://10.1.2.85:3306/nowa", driver = "org.mariadb.jdbc.Driver",
        user = "root", password = "praktykanci2024"
    )

}
