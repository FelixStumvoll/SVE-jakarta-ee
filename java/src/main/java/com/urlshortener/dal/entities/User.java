package com.urlshortener.dal.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "ShortUrlUser", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = Constraints.userNameConstraint)})
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String name;
    private UserRole role;
    private String password;
    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<ShortUrl> shortUrls;
}

