package com.ifcdpp.ifcdpp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CatalogEntity {

    private Long id;
    private String title;
    private String text;
    private int views;
}
