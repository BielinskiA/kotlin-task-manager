package com.example.springboot.endpoints

import com.example.springboot.interceptors.HeaderAuthenticationJwtFilter
import com.example.springboot.security.JWTController
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URLDecoder
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import com.example.springboot.Tables.*
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.sql.Blob


@RestController
class Check {
    val jwtController = JWTController()
    private val uploadDir: Path = Paths.get("uploads")

    init {
        Files.createDirectories(uploadDir)
    }

    @GetMapping("/users")
    fun getUsers(
        @RequestAttribute(name = HeaderAuthenticationJwtFilter.USER) userr: UserData,
        @RequestParam(required = false) id: Int?,
        @RequestParam(required = false) mail: String?,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) surname: String?
    ): Any? {
        return transaction(DataBaseControl.connect) {
            val user = Users.selectAll()

            id?.let {
                val a = user.where(Users.id.eq(id)).firstOrNull()
                return@transaction if (a == null) {
                    ResponseEntity(Message("Nie ma takiego użytkownika"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = user.map {
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
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            name?.let {
                val b = user.where(Users.name.eq(name)).firstOrNull()
                return@transaction if (b == null) {
                    ResponseEntity(Message("Nie ma takiego imienia"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = user.map {
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
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            surname?.let {
                val c = user.where(Users.surname.eq(surname)).firstOrNull()
                return@transaction if (c == null) {
                    ResponseEntity(Message("Nie ma takiego nazwiska"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = user.map {
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
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            mail?.let {
                val d = user.where(Users.mail like "%${URLDecoder.decode(mail, "UTF-8")}%").firstOrNull()
                return@transaction if (d == null) {
                    ResponseEntity(Message("Nie ma takiego maila"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = user.map {
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
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            return@transaction user.map {
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

    @GetMapping("/tasks")
    fun getTasks(
        @RequestAttribute(name = HeaderAuthenticationJwtFilter.USER) user: UserData,
        @RequestParam(required = false) id: Int?,
        @RequestParam(required = false) userId: Int?,
        @RequestParam(required = false) task: String?,
    ): Any {
        return transaction(DataBaseControl.connect) {
            val tasks = Tasks.selectAll()

            id?.let {
                val a = tasks.where(Tasks.id.eq(id)).firstOrNull()
                return@transaction if (a == null) {
                    ResponseEntity(Message("Nie ma takiego id"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = tasks.map {
                        TaskData(
                            it[Tasks.id],
                            it[Tasks.userId],
                            it[Tasks.task],
                            it[Tasks.create],
                            it[Tasks.finish]
                        )
                    }
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            userId?.let {
                val b = tasks.where(Tasks.userId.eq(userId)).firstOrNull()
                return@transaction if (b == null) {
                    ResponseEntity(Message("Nie ma takiego użytkownika"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = tasks.map {
                        TaskData(
                            it[Tasks.id],
                            it[Tasks.userId],
                            it[Tasks.task],
                            it[Tasks.create],
                            it[Tasks.finish]
                        )
                    }
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            task?.let {
                val c = tasks.where(Tasks.task.eq(task)).firstOrNull()
                return@transaction if (c == null) {
                    ResponseEntity(Message("Nie ma takiego zadania"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = tasks.map {
                        TaskData(
                            it[Tasks.id],
                            it[Tasks.userId],
                            it[Tasks.task],
                            it[Tasks.create],
                            it[Tasks.finish]
                        )
                    }
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            return@transaction tasks.map {
                TaskData(
                    it[Tasks.id],
                    it[Tasks.userId],
                    it[Tasks.task],
                    it[Tasks.create],
                    it[Tasks.finish]
                )
            }
        }
    }

    @GetMapping("/comments")
    fun getComments(
        @RequestAttribute(name = HeaderAuthenticationJwtFilter.USER) user: UserData,
        @RequestParam(required = false) id: Int?,
        @RequestParam(required = false) userId: Int?,
        @RequestParam(required = false) taskId: Int?,
        @RequestParam(required = false) comment: String?
    ): Any? {
        return transaction(DataBaseControl.connect) {
            val comments = Comments.selectAll()

            id?.let {
                val a = comments.where(Comments.id.eq(id)).firstOrNull()
                return@transaction if (a == null) {
                    ResponseEntity(Message("Nie ma takiego id"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = comments.map {
                        CommentData(
                            it[Comments.id],
                            it[Comments.userId],
                            it[Comments.taskId],
                            it[Comments.comment],
                            it[Comments.create]
                        )
                    }
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            userId?.let {
                val b = comments.where(Comments.userId.eq(userId)).firstOrNull()
                return@transaction if (b == null) {
                    ResponseEntity(Message("Nie ma takiego id użytkownika"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = comments.map {
                        CommentData(
                            it[Comments.id],
                            it[Comments.userId],
                            it[Comments.taskId],
                            it[Comments.comment],
                            it[Comments.create]
                        )
                    }
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            taskId?.let {
                val c = comments.where(Comments.taskId.eq(taskId)).firstOrNull()
                return@transaction if (c == null) {
                    ResponseEntity(Message("Nie ma takiego zadania"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = comments.map {
                        CommentData(
                            it[Comments.id],
                            it[Comments.userId],
                            it[Comments.taskId],
                            it[Comments.comment],
                            it[Comments.create]
                        )
                    }
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            comment?.let {
                val d = comments.where(Comments.comment like "%$comment%").firstOrNull()
                return@transaction if (d == null) {
                    ResponseEntity(Message("Nie ma takiego komentarza"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = comments.map {
                        CommentData(
                            it[Comments.id],
                            it[Comments.userId],
                            it[Comments.taskId],
                            it[Comments.comment],
                            it[Comments.create]
                        )
                    }
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }



            return@transaction comments.map {
                CommentData(
                    it[Comments.id],
                    it[Comments.userId],
                    it[Comments.taskId],
                    it[Comments.comment],
                    it[Comments.create]
                )
            }
        }
    }

    @GetMapping("tasks/download")
    fun downloadFile(
        @RequestAttribute(name = HeaderAuthenticationJwtFilter.USER) user: UserData,
        @RequestParam(required = false) id: Int?,
        @RequestParam(required = false) userId: Int?,
        @RequestParam(required = false) taskId: Int?,
        @RequestParam(required = false) fileName: String?,
        @RequestParam(required = false) type: String?
    ): Any {
        return transaction(DataBaseControl.connect) {
            val files = TaskFileTable.selectAll()

            id?.let {
                val a = files.where(TaskFileTable.id.eq(id)).firstOrNull()
                return@transaction if (a == null) {
                    ResponseEntity(Message("Nie ma takiego id"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = files.map {
                        TaskFile(
                            it[TaskFileTable.id],
                            it[TaskFileTable.userId],
                            it[TaskFileTable.taskId],
                            it[TaskFileTable.fileName],
                            it[TaskFileTable.file],
                            it[TaskFileTable.type],
                            it[TaskFileTable.create]
                        )
                    }
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            userId?.let {
                val b = files.where(TaskFileTable.userId.eq(userId)).firstOrNull()
                return@transaction if (b == null) {
                    ResponseEntity(Message("Nie ma takiego id użytkownika"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = files.map {
                        TaskFile(
                            it[TaskFileTable.id],
                            it[TaskFileTable.userId],
                            it[TaskFileTable.taskId],
                            it[TaskFileTable.fileName],
                            it[TaskFileTable.file],
                            it[TaskFileTable.type],
                            it[TaskFileTable.create]
                        )
                    }
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            taskId?.let {
                val c = files.where(TaskFileTable.taskId.eq(taskId)).firstOrNull()
                return@transaction if (c == null) {
                    ResponseEntity(Message("Nie ma takiego zadania"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = files.map {
                        TaskFile(
                            it[TaskFileTable.id],
                            it[TaskFileTable.userId],
                            it[TaskFileTable.taskId],
                            it[TaskFileTable.fileName],
                            it[TaskFileTable.file],
                            it[TaskFileTable.type],
                            it[TaskFileTable.create]
                        )
                    }
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            fileName?.let {
                val d = files.where(TaskFileTable.fileName.eq(fileName)).firstOrNull()
                return@transaction if (d == null) {
                    ResponseEntity(Message("Nie ma takiego pliku"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = files.map {
                        TaskFile(
                            it[TaskFileTable.id],
                            it[TaskFileTable.userId],
                            it[TaskFileTable.taskId],
                            it[TaskFileTable.fileName],
                            it[TaskFileTable.file],
                            it[TaskFileTable.type],
                            it[TaskFileTable.create]
                        )
                    }
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            type?.let {
                val d = files.where(TaskFileTable.type.eq(type)).firstOrNull()
                return@transaction if (d == null) {
                    ResponseEntity(Message("Nie ma pliku o takim typie"), HttpStatus.BAD_REQUEST)
                } else {
                    val ok = files.map {
                        TaskFile(
                            it[TaskFileTable.id],
                            it[TaskFileTable.userId],
                            it[TaskFileTable.taskId],
                            it[TaskFileTable.fileName],
                            it[TaskFileTable.file],
                            it[TaskFileTable.type],
                            it[TaskFileTable.create]
                        )
                    }
                    ResponseEntity(ok, HttpStatus.OK)
                }
            }

            return@transaction files.map {
                TaskFile(
                    it[TaskFileTable.id],
                    it[TaskFileTable.userId],
                    it[TaskFileTable.taskId],
                    it[TaskFileTable.fileName],
                    it[TaskFileTable.file],
                    it[TaskFileTable.type],
                    it[TaskFileTable.create]
                )
            }
        }
    }
//////////////////////////////////

    @PostMapping("/users")
    fun addUser(
        @RequestAttribute(name = HeaderAuthenticationJwtFilter.USER) userr: UserData,
        @RequestBody user: NewUserData,
    ): ResponseEntity<*> {
        if (!isEmailValid(user.mail)) {
            return ResponseEntity(Message("Email jest niepoprawny"), HttpStatus.BAD_REQUEST)
        }
        if (emailExist(user.mail)) {
            return ResponseEntity(Message("Konto z takim mailem już istnieje"), HttpStatus.BAD_REQUEST)
        }
        if (!isPasswordStrong(user.password)) {
            return ResponseEntity(Message("Hasło jest za słabe"), HttpStatus.BAD_REQUEST)
        }

        val hash = hash(user.password)
        val u = transaction(DataBaseControl.connect) {
            Users.insert {
                it[name] = user.name
                it[surname] = user.surname
                it[mail] = user.mail
                it[password] = hash
            }[Users.id]
        }
        return ResponseEntity(u, HttpStatus.CREATED)
    }


    @PostMapping("/comments")
    fun addComment(
        @RequestAttribute(name = HeaderAuthenticationJwtFilter.USER) user: UserData,
        @RequestBody commentt: NewCommentData,
    ): Any {
        if (!taskExist(commentt.taskId)) {
            return ResponseEntity(Message("Zadanie o takim ID nie istnieje"), HttpStatus.BAD_REQUEST)
        }

        val c = transaction(DataBaseControl.connect) {
            Comments.insert {
                it[userId] = user.id
                it[taskId] = commentt.taskId
                it[comment] = commentt.comment
            }[Comments.id]
        }
        return ResponseEntity(c, HttpStatus.CREATED)
    }


    @PostMapping("/tasks")
    fun addTask(
        @RequestAttribute(name = HeaderAuthenticationJwtFilter.USER) user: UserData,
        @RequestBody taskk: NewTaskData
    ): Any {
        val t = transaction(DataBaseControl.connect) {
            Tasks.insert {
                it[userId] = user.id
                it[task] = taskk.task
            }[Tasks.id]
        }
        return ResponseEntity(t, HttpStatus.CREATED)
    }


    @PostMapping("/login")
    fun login(@RequestBody user: Login): ResponseEntity<*> {
        val userData = transaction(DataBaseControl.connect) {
            findUserByEmail(user.mail)
        }

        userData?.let {
            if (passwordExist(user.password)) {
                val token = JWTController().generateJwt(it.id)
                return ResponseEntity.ok().header("Authorization", "Bearer $token")
                    .body(Message("Zalogowano prawidłowo"))
            }
        }
        return ResponseEntity(Message("Niepoprawne hasło lub email"), HttpStatus.UNAUTHORIZED)
    }

    @PostMapping("tasks/{id}/upload")
    fun addFile(
        @RequestAttribute(name = HeaderAuthenticationJwtFilter.USER) user: UserData,
        @RequestBody file: ByteArray,
        @PathVariable("id") taskId: Int,
        @RequestHeader("Content-Type") contentType: String,
        @RequestHeader("file-name") fileName: String
    ): Any {
        val task = transaction(DataBaseControl.connect) {
            Tasks.selectAll().where { Tasks.id.eq(taskId) and Tasks.userId.eq(user.id) }
                .map { it[Tasks.id] }
                .firstOrNull()
        }
        if (task == null) {
            return ResponseEntity(
                "Nie ma zadania o takim ID lub zadanie nie należy do użytkownika",
                HttpStatus.BAD_REQUEST
            )
        }
        if (contentType.isEmpty()) {
            return ResponseEntity("Brak typu pliku", HttpStatus.BAD_REQUEST)
        }
        if (fileName.isEmpty()) {
            return ResponseEntity("Plik ten nie ma nadanej nazwy", HttpStatus.BAD_REQUEST)
        }
        val fileId = transaction(DataBaseControl.connect) {
            TaskFileTable.insert {
                it[userId] = user.id
                it[TaskFileTable.taskId] = taskId
                it[TaskFileTable.fileName] = fileName
                it[TaskFileTable.file] = ExposedBlob(file)
                it[TaskFileTable.type] = contentType
            }[TaskFileTable.id]
        }
        return ResponseEntity(Message("ID pliku: $fileId, ID zadania: $taskId"), HttpStatus.OK)
    }
///////////////////////////////////////

    @DeleteMapping("/users")
    fun deleteUser(
        @RequestAttribute(name = HeaderAuthenticationJwtFilter.USER) user: UserData,
        @RequestParam id: Int
    ): Any {
        if (userExist(user.id)) {
            return ResponseEntity(Message("Nie możesz się sam usunąć"), HttpStatus.ACCEPTED)
        }
        if (!(userExist(id))) {
            return ResponseEntity(
                Message("Nie udało się usunąć użytkownika, ponieważ albo został już usunięty, albo nigdy nie istniał"),
                HttpStatus.BAD_REQUEST
            )
        }
        transaction(DataBaseControl.connect) {
            Users.deleteWhere { Users.id.eq(id) }
        }
        return ResponseEntity(Message("Udało się usunąć użytkownika"), HttpStatus.ACCEPTED)
    }


    @DeleteMapping("/tasks")
    fun deleteTask(
        @RequestAttribute(name = HeaderAuthenticationJwtFilter.USER) user: UserData,
        @RequestParam id: Int
    ): Any {
        if (!taskExist(id)) {
            return ResponseEntity(
                Message("Nie udało się usunąć zadania, ponieważ albo zostało już usunięte, albo nigdy nie istniało"),
                HttpStatus.BAD_REQUEST
            )
        }

        transaction(DataBaseControl.connect) {
            Tasks.deleteWhere { Tasks.id.eq(id) and userId.eq(user.id) }
        }
        return ResponseEntity(Message("Udało się usunąć zadanie"), HttpStatus.ACCEPTED)
    }


    @DeleteMapping("/comments")
    fun deleteComment(
        @RequestAttribute(name = HeaderAuthenticationJwtFilter.USER) user: UserData,
        @RequestParam id: Int
    ): Any {
        if (!commentExist(id)) {
            ResponseEntity(
                Message("Nie udało się usunąć komentarza, ponieważ albo został już usunięte, albo nigdy nie istniał"),
                HttpStatus.BAD_REQUEST
            )
        }

        transaction(DataBaseControl.connect) {
            Comments.deleteWhere {
                Comments.id.eq(id) and userId.eq(user.id)
            }
        }
        return ResponseEntity(Message("Udało się usunąć komentarz"), HttpStatus.ACCEPTED)
    }

    @DeleteMapping("tasks/{id1}/file/{id2}")
    fun deleteFile(
        @RequestAttribute(name = HeaderAuthenticationJwtFilter.USER) user: UserData,
        @PathVariable("id1") id1: Int,
        @PathVariable("id2") id2: Int
    ): Any {
        val task = transaction(DataBaseControl.connect) {
            TaskFileTable.selectAll()
                .where(TaskFileTable.id.eq(id2) and TaskFileTable.taskId.eq(id1) and TaskFileTable.userId.eq(user.id)).firstOrNull()
        }

        if (!taskExist(id1)) {
            return ResponseEntity(
                Message("Nie udało się usunąć zadania, ponieważ albo zostało już usunięte, albo nigdy nie istniało"),
                HttpStatus.BAD_REQUEST
            )
        }
        if (!fileExist(id2)) {
            return ResponseEntity(
                Message("Nie udało się usunąć pliku, ponieważ albo został już usunięty, albo nigdy nie istniał"),
                HttpStatus.BAD_REQUEST
            )
        }
        if (task == null) {
            return ResponseEntity(Message("Ten plik nie należy do tego zadania"), HttpStatus.BAD_REQUEST)
        }

        transaction(DataBaseControl.connect) {
            TaskFileTable.deleteWhere {
                TaskFileTable.id.eq(id2) and userId.eq(user.id) and taskId.eq(id1)
            }
        }
        return ResponseEntity(Message("Udało się usunąć plik"), HttpStatus.ACCEPTED)
    }
}
//////////////////////////////////////////

fun userExist(userIdd: Int): Boolean {
    val user = transaction(DataBaseControl.connect) {
        Users.selectAll().where(Users.id.eq(userIdd)).firstOrNull()
    }
    return user != null
}

fun taskExist(taskIdd: Int): Boolean {
    val task = transaction(DataBaseControl.connect) {
        Tasks.selectAll().where(Tasks.id.eq(taskIdd)).map{
            it[Tasks.id]
        }.firstOrNull()
    }
    return (task != null)
}

fun commentExist(commentIdd: Int): Boolean {
    val comment = transaction (DataBaseControl.connect) {
        Comments.selectAll().where(Comments.id.eq(commentIdd)).map{
            it[Comments.id]
        }.firstOrNull()
    }
    return (comment != null)
}

fun emailExist(maill: String): Boolean {
    val userEmail = transaction (DataBaseControl.connect) {
        Users.selectAll().where(Users.mail.eq(maill)).map {
            it[Users.mail]
        }.firstOrNull()
    }
    return (userEmail != null)
}

fun fileExist(fileId: Int): Boolean {
    val file = transaction (DataBaseControl.connect) {
        TaskFileTable.selectAll().where(TaskFileTable.id.eq(fileId) ).map {
            it[TaskFileTable.id]
        }.firstOrNull()
    }
    return (file != null)
}

fun isEmailValid(emaill: String): Boolean {
    val emailPattern1 = "[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z]+"
    val emailPattern2 = "[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z0-9]+\\.+[a-z]+"
    return emaill.matches(emailPattern1.toRegex()) or emaill.matches(emailPattern2.toRegex())
}

fun isPasswordStrong(passwordd: String): Boolean {
    val passwordPattern =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+$).{8,}$"
    return passwordd.matches(passwordPattern.toRegex())
}

fun hash(input: String): String {
    val bytes = input.toByteArray()
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(bytes)
    return hashBytes.joinToString("") { "%02x".format(it) }
}

fun passwordExist(password: String): Boolean{
    val hash = hash(password)
    val exists = transaction (DataBaseControl.connect){
        Users.selectAll().where(Users.password.eq(hash)).map{
            it[Users.password]
        }.firstOrNull()
    }
    return exists != null
}

fun findUserByEmail(email: String): UserData? {
    return transaction(DataBaseControl.connect) {
        Users.selectAll().where(Users.mail.eq(email)).map {
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
            .firstOrNull()
    }
}

///////////////////////////////////////

data class UserData(
    val id: Int,
    val name: String,
    val surname: String,
    val mail: String,
    val password: String,
    val photo: ExposedBlob,
    val create: LocalDateTime
)

data class TaskData(
    val id: Int,
    val userId: Int,
    val task: String,
    val create: LocalDateTime,
    val finish: LocalDateTime?
)

data class CommentData(
    val id: Int,
    val userId: Int,
    val taskId: Int,
    val comment: String,
    val create: LocalDateTime
)

data class ConfigServer(
    val id: Int,
    val key: String,
    val value: String,
    val create: LocalDateTime
)

data class NewUserData(
    val name: String,
    val surname: String,
    val mail: String,
    val password: String
)

data class NewTaskData(
    val userId: Int,
    val task: String
)

data class NewCommentData(
    val userId: Int,
    val taskId: Int,
    val comment: String
)

data class Login(
    val mail: String,
    val password: String
)

data class Message(
    val message: String
)

data class TaskFile(
    val id: Int,
    val userId: Int,
    val taskId: Int,
    val fileName: String,
    val file: ExposedBlob,
    val type: String,
    val create: LocalDateTime
)

data class NewTaskFile(
    val userId: Int,
    val taskId: Int,
    val fileName: String,
    val file: ByteArray,
    val type: String
)
