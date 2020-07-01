package app.controllers;

import app.services.VerifyTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class VerifyTokenController {

    private VerifyTokenService verifyTokenService;

    @Autowired
    public VerifyTokenController(VerifyTokenService verifyTokenService) {
        this.verifyTokenService = verifyTokenService;
    }

    @GetMapping(value = "/token/{token}", produces = "application/json")
    public void enableUser(@PathVariable String token, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", "http://localhost:4200/");
        httpServletResponse.setStatus(verifyTokenService.enableUser(token).getStatusCode().value());
    }
}
