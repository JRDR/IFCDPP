package com.ifcdpp.ifcdpp.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IssueUser {

    private Long id;
    private String login;
    private String email;

}
