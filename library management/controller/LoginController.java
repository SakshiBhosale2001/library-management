package com.projectrestapi.projectrestapi.controller;

import com.projectrestapi.projectrestapi.entity.login.JwtTokenProvider;
import com.projectrestapi.projectrestapi.entity.login.LoginRequest;
import com.projectrestapi.projectrestapi.entity.login.Users;
import com.projectrestapi.projectrestapi.jwt.JwtUtils;
import com.projectrestapi.projectrestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager manager;              //Authenticate provided authentication object to manager


    @Autowired
    private JwtUtils jwtUtils;



    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        //verifying Authentication
        Authentication authentication=manager.authenticate(new UsernamePasswordAuthenticationToken
                (loginRequest.getEmail(), loginRequest.getPassword()));

        //getting user details
        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());

        //generating tokens
        String token = jwtUtils.generateToken(userDetails);

        //retrieving token & email
        JwtTokenProvider jwtTokenProvider = JwtTokenProvider.builder()
                .jwtToken(token)
                .email(userDetails.getUsername())
                .build();

        return new ResponseEntity<>(jwtTokenProvider.getJwtToken(), HttpStatus.OK);
    }

    @PutMapping("/reset/password")
    public ResponseEntity<String> checkUser(@RequestBody LoginRequest request)
    {
        return userService.checkUser(request.getEmail()) ? userService.resetPassword(request)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Can Not Proceed a Request...Please Enter a Registered Email...! \n " +
                        "OR \n Kindly Register First ! ...Thank You!! ");

    }



    @GetMapping("/auth/user/username")
    public String getLoggedInUser(Principal principal){
         return principal.getName();
    }


    @PostMapping(value = "/signup")
    public Object signUp(@RequestBody Users user)
    {
        return this.userService.signIn(user);
    }



    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete")
    public ResponseEntity<String>  deleteUser(@RequestBody LoginRequest request){
            this.login(request);
            return userService.deleteUser(request.getEmail());
    }


    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler()
    {
        return "Credentials Invalid !!";
    }

}
