package org.urlshortener.dal.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
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
    private String userId;
}
