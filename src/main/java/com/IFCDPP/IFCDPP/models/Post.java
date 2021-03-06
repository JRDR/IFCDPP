package com.ifcdpp.ifcdpp.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Post {

    private Long id;
    private String title;
    private String text;
    private int views;
}
