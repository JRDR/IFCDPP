package com.ifcdpp.ifcdpp.models;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Blog {

    List<Post> posts;
    int pagesNum;
}
