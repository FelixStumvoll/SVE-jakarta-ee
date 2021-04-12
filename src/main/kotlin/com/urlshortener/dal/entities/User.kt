package com.urlshortener.dal.entities

import javax.persistence.*
import javax.validation.constraints.NotBlank

const val userNameConstraint = "UserNameUniqueConstraint"

@Entity
@Table(name="ShortUrlUser",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name"], name = userNameConstraint)]
)
data class User(
    @field:NotBlank
    var name: String,
    var role: UserRole,
    var password: String,
    @Id @GeneratedValue var id: Long? = null
)

enum class UserRole(val type: String) {
    Premium("Premium"),
    Peasant("Free")
}