package com.ifcdpp.ifcdpp.models;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductRequest {

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
    private MultipartFile file;

}
