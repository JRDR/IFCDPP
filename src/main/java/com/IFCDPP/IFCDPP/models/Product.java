package com.ifcdpp.ifcdpp.models;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {

    private Long id;
    private String title;
    private String developer;
    private String description;
    private String downloadLink;
    private BigDecimal price;
    private String category;
    private String imageLink;
    private String developerLink;
    private String preferences;
    private List<Review> reviews;

}
