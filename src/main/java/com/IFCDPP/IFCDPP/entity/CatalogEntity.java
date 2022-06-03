package com.IFCDPP.IFCDPP.entity;

import lombok
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post {

    private Long id;
    private String title;
    private String text;
    private int views;
}
public class CatalogEntity {
}
