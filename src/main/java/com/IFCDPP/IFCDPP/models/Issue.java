package com.ifcdpp.ifcdpp.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Issue {

    private Long id;
    private String topic;
    private String description;
    private IssueUser user;
}
