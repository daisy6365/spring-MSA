package com.study.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestUser {

    @NotNull(message = "Email connot be null")
    @Size(min = 2, message = "Email not be less than 2 characters")
    @Email
    private String email;

    @NotNull(message = "Name connot be null")
    @Size(min = 2, message = "Name not be less than 2 characters")
    private String name;

    @NotNull(message = "Password connot be null")
    @Size(min = 8, message = "Password Must be equal or grater than 8 characters")
    private String pwd;

}
