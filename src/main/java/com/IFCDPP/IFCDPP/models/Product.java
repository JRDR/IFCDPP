package com.ifcdpp.ifcdpp.models;


import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {

    private String title;
    private String developer;
    private String description;
    private String downloadLink;
    private BigDecimal price;
    private String category;

}
