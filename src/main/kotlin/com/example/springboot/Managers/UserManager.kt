package com.example.springboot.Managers

import com.example.springboot.Tables.DataBaseControl
import com.example.springboot.Tables.Users
import com.example.springboot.Tables.Users.created
import com.example.springboot.Tables.Users.mail
import com.example.springboot.Tables.Users.name
import com.example.springboot.Tables.Users.password
import com.example.springboot.Tables.Users.surname
import com.example.springboot.endpoints.NewUserData
import com.example.springboot.endpoints.UserData
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class UserManager {
    fun getUserById(id: Int): List<UserData> {
        return transaction(DataBaseControl.connect) {
            Users.selectAll().where(Users.id.eq(id)).map {
                UserData(
                    it[Users.id],
                    it[Users.name],
                    it[Users.surname],
                    it[Users.mail],
                    it[Users.password],
                    it[Users.photo],
                    it[Users.created]
                )
            }
        }
    }

    fun emailExists(email: String): Boolean {
        return transaction(DataBaseControl.connect) {
            !Users.selectAll().where{ Users.mail eq email }.empty()
        }
    }

    fun isPasswordStrong(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+\$).{8,}\$"
        val pattern = Pattern.compile(passwordPattern)
        return pattern.matcher(password).matches()
    }

    fun saveUser(userData: NewUserData) {
        transaction(DataBaseControl.connect) {
            Users.insert {
                it[name] = userData.name
                it[surname] = userData.surname
                it[mail] = userData.mail
                it[password] = userData.password
                it[created] = CurrentDateTime
            }
        }
    }
}
