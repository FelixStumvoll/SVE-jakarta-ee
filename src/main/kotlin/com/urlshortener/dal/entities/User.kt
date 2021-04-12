package com.urlshortener.dal.entities

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name="ShortUrlUser" , uniqueConstraints = [UniqueConstraint(columnNames = ["name"], name = "ShortNameUniqueConstraint")])
data class User(
    @field:NotBlank
    var name: String,
    var role: UserRole,
    var password: String,
    @Id @GeneratedValue var id: Long? = null
)
