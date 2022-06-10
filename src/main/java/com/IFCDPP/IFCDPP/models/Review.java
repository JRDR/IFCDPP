package com.ifcdpp.ifcdpp.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Review {

    private Long id;
    private String topic;
    private String description;
    private IssueUser user;
    private Product product;
}
