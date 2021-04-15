package org.urlshortener.dal.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"shortName"}, name = Constraints.shortUrlConstraint)})
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrl {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    @URL
    private String url;
    @Size(min = 2, max = 10)
    private String shortName;
    @ManyToOne
    private User user;
}
