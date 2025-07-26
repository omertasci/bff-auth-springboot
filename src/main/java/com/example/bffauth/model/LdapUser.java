package com.example.bffauth.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LdapUser {
    @NotBlank
    private String fullName;

    @NotBlank
    private String username;

    @NotBlank
    private String email;
}
