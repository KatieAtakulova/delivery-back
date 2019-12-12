package ua.nure.delivery.api.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.nure.delivery.api.dto.UserDto;
import ua.nure.delivery.entity.User;

@Component
public class UserConverter implements Converter<UserDto, User> {

    private PasswordEncoder passwordEncoder;

    public UserConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User convert(UserDto userDto) {
        return User.builder()
                .fullName(userDto.getFullName())
                .phone(userDto.getPhoneNumber())
                .login(userDto.getLogin())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(userDto.getRole())
                .build();
    }
}
