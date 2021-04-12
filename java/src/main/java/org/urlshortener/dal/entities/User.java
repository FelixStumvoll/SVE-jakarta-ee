package org.urlshortener.dal.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Data
@Entity
@Table(name="ShortUrlUser", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = Constraints.shortUrlConstraint)})
@NoArgsConstructor
public class User{
    @Id @GeneratedValue
    private Long id;
    @NotBlank
    private String name;
    private UserRole role;
    private String password;
}

