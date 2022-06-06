package com.ifcdpp.ifcdpp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Type(type = "text")
    private String description;
    private String developer;
    private String downloadLink;
    private BigDecimal price;
    private String imageLink;
    private String developerLink;

    @ManyToOne
    private CategoryEntity category;
}
