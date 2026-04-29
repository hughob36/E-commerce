package com.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAppResponseSimpleDTO {

    private Long id;
    private String name;
    private String lastName;
    private String username;
    private String email;
}
