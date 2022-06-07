package com.ifcdpp.ifcdpp.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Profile {

    private Long id;
    private String login;
    private String email;
    private boolean active;
    private boolean isAdmin;

}
