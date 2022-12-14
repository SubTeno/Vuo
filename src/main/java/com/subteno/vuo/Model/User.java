package com.subteno.vuo.Model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @NotEmpty(message = "name cannot be empty!")
    private String name;
    @NotEmpty(message = "username cannot be empty!")
    private String username;
    @NotEmpty(message = "password cannot be empty!")
    private String password;
    private Role role;
}
