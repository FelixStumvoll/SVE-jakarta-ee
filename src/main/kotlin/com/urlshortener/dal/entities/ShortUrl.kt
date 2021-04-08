package com.urlshortener.dal.entities

import org.hibernate.validator.constraints.URL
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["shortName"], name = "ShortNameUniqueConstraint")])
data class ShortUrl(
    @field:NotBlank
    @field:URL
    var url: String,
    @field:Size(min = 2, max = 10)
    var shortName: String,
    var userId: String,
    @Id @GeneratedValue var id: Long? = null
)
