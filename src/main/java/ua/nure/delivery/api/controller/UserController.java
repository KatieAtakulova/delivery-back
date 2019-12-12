package ua.nure.delivery.api.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.delivery.api.dto.UserDto;
import ua.nure.delivery.entity.User;
import ua.nure.delivery.entity.enums.Role;
import ua.nure.delivery.service.UserService;
import ua.nure.delivery.service.convert.ExtendedConversionService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    private UserService userService;
    private ExtendedConversionService conversionService;

    public UserController(UserService userService, ExtendedConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @ApiOperation("Retrieve information about current user")
    @GetMapping("/me")
    public ResponseEntity<User> getMe() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @ApiOperation("Api for update info about user phone and full name")
    @PutMapping("/update")
    public void updateUserInfo(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String fullName
    ) {
        userService.updateUserInfo(id, phone, fullName);
    }

    @ApiOperation("Api for ban user")
    @PutMapping("/change-user-status")
    public void banUser(@RequestParam Long id, Role newRole) {
        userService.changeRole(id, newRole);
    }

    /**
     * Creates an user
     *
     * @param userDto user
     * @return created user
     */
    @PostMapping(value = "/registration")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void save(@RequestBody @Valid UserDto userDto) {
        User user = conversionService.convert(userDto, User.class);
        userService.save(user);
    }

    @ApiOperation("Api for get users by params. Params are optional")
    @GetMapping()
    public ResponseEntity<List<User>> findUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Role role) {

        return ResponseEntity.ok(userService.searchByParams(name, role));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
