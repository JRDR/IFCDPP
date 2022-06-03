package com.ifcdpp.ifcdpp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistrationModel {

    private String login;
    private String password;
    private String email;
    private String confirm;
}
