package com.urlshortener.dal.entities

import org.hibernate.validator.constraints.URL
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

const val shortNameConstraint = "ShortNameUniqueConstraint"

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["shortName"], name = shortNameConstraint)])
class ShortUrl(
    @field:NotBlank
    @field:URL
    var url: String,
    @field:Size(min = 2, max = 10)
    var shortName: String,
    @ManyToOne var user: User,
    @Id @GeneratedValue var id: Long? = null
)
