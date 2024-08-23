package com.projectrestapi.projectrestapi.service;
import com.projectrestapi.projectrestapi.entity.login.LoginRequest;
import com.projectrestapi.projectrestapi.entity.login.Users;
import com.projectrestapi.projectrestapi.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Users> user = userRepo.findByEmail(email);

        if(user.isPresent()) {
            Users users = user.get();

            return new org.springframework.security.core.userdetails.User(
                    email,
                    users.getPassword(),
                    users.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getRole()))
                            .collect(Collectors
                                    .toList()));
        }
        else throw new UsernameNotFoundException("Could not findUser with email = " + email);

    }


    public ResponseEntity<Optional> signIn(Users user){
        try {

            user.setPassword(passwordEncoder().encode(user.getPassword()));
            return ResponseEntity.ok(Optional.of(userRepo.save(user)));
        }

        catch (DataIntegrityViolationException e)
        {
            return ResponseEntity.badRequest()
                    .body(Optional.of("Duplicate entry detected: " + e.getMessage()));
        }

    }


    public boolean checkUser(String email)
    {
       return userRepo.findByEmail(email).isPresent();
    }

    public ResponseEntity<String> resetPassword(LoginRequest request)
    {
        Optional<Users> user=userRepo.findByEmail(request.getEmail());
        try {

            user.orElseThrow(RuntimeException::new)
                    .setPassword(passwordEncoder().encode(request.getPassword()));

            Users users=user.get();

           userRepo.save(users);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Password Modified Successfully");
        }
        catch(RuntimeException runtimeException)
        {
            runtimeException.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .body("Try Again...!");
        }
    }

    public Optional<Users> getUser(long id)
    {
        return userRepo.findById(id);
    }

    public ResponseEntity<String> deleteUser(String email)
    {
        try {
            userRepo.deleteById(userRepo.findByEmail(email)
                    .get().getUserId());
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);

        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
