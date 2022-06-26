package com.olyves.authentication.controller;


import com.olyves.authentication.payload.request.SignUpRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiOperation(value = "/api/v1/auth", tags = "Authentication Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @PostMapping("sign-up")
    public ResponseEntity<?> signUp(@Validated @RequestBody SignUpRequest signUpRequest) {
        //todo add client http interceptor on every request




    }

}
