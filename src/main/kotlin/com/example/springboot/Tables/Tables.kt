package com.example.springboot.Tables

import com.example.springboot.Tables.Comments.defaultExpression
import com.example.springboot.Tables.Comments.references
import com.example.springboot.Tables.ConfigServer.defaultExpression
import com.example.springboot.Tables.Tasks.autoIncrement
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import org.mariadb.jdbc.client.column.BlobColumn
import org.mariadb.jdbc.plugin.codec.BlobCodec
import java.sql.Blob

object Users : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 50)
    val surname: Column<String> = varchar("surname", 50)
    val mail: Column<String> = varchar("mail", 100)
    val password: Column<String> = varchar("password", 100)
    val photo
    = blob("photo")
    val created = datetime("created").defaultExpression(CurrentDateTime)
    override val primaryKey = PrimaryKey(id)
}

object Tasks : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val userId: Column<Int> = integer("user_id").references(Users.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val task: Column<String> = varchar("task", 100)
    val create = datetime("create").defaultExpression(CurrentDateTime)
    val finish = datetime("finish").nullable()
    override val primaryKey = PrimaryKey(id)
}

object Comments : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val userId: Column<Int> = integer("user_id").references(Users.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val taskId: Column<Int> = integer("task_id").references(Tasks.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val comment: Column<String> = text("comment")
    val create = datetime("create").defaultExpression(CurrentDateTime)
    override val primaryKey = PrimaryKey(id)
}

object ConfigServer : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    var key: Column<String?> = text("key").nullable()
    var value: Column<String> = text("value")
    val create = datetime("create").defaultExpression(CurrentDateTime)
    override val primaryKey = PrimaryKey(id)

}

object TaskFileTable : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val userId: Column<Int> = integer("userId").references(Users.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val taskId: Column<Int> = integer("taskId").references(Tasks.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val fileName: Column<String> = text("fileName")
    val file = blob("file")
    val type = text("type")
    val create = datetime("create").defaultExpression(CurrentDateTime)
    override val primaryKey = PrimaryKey(id)
}