package app.controllers;

import app.entity.payloads.JwtRequest;
import app.entity.payloads.RegisterRequest;
import app.services.ReCaptchaService;
import app.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserServiceImpl userService;
    private ReCaptchaService reCaptchaService;

    @Autowired
    public UserController(UserServiceImpl userService, ReCaptchaService reCaptchaService) {
        this.userService = userService;
        this.reCaptchaService = reCaptchaService;
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return userService.createAuthenticationToken(jwtRequest);
    }

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest user) {

        System.out.println(user.getUsername() + " " + user.getPassword());
        return userService.registerUser(user);
    }

    @GetMapping(value = "/users/getAll", produces = "application/json")
    public ResponseEntity<?> getAllUsers() {
        return userService.getUsersBySpecificRole();
    }

    @PostMapping(value = "/reCaptcha", produces = "application/json")
    public ResponseEntity<?> validateCaptcha(@RequestBody String captcha) {
        if (reCaptchaService.verify(captcha))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/users/changeRole/{id}", produces = "application/json")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id) {
        return userService.changeRole(id);
    }

    @DeleteMapping(value = "/users/admin/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
