package ua.nure.delivery.api.dto;

import lombok.Data;
import ua.nure.delivery.entity.enums.Role;

@Data
public class UserDto {

    private String phoneNumber;

    private String fullName;

    private String login;

    private String password;

    private Role role;
}
