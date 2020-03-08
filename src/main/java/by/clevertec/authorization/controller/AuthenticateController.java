package by.clevertec.authorization.controller;

import by.clevertec.authorization.service.PasswordVerificationAndTokenGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class AuthenticateController {

    @Autowired
    private PasswordVerificationAndTokenGeneration passwordVerificationAndTokenGeneration;

    @GetMapping
    public ResponseEntity authenticate(@RequestHeader(name = "Authorization") String authorization) {
        return passwordVerificationAndTokenGeneration.checkLogAndPass(authorization);
    }
}