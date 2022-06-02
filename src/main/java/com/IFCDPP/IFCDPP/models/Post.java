package com.IFCDPP.IFCDPP.models;

import lombok.*;

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
