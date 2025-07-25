package com.tours.backend.Controller;

import com.tours.backend.Entities.Users;
import com.tours.backend.Service.JwtService;
import com.tours.backend.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody Users user) throws Exception{
        if(!user.isEnabled()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not enabled");
        }
        userService.register(user);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Users loginUser){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));

        if(authentication.isAuthenticated()){
            String token = jwtService.generateToken(loginUser.getEmail());
            return ResponseEntity.ok(token);
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminDashboard(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = getUsername(authentication);
        return ResponseEntity.ok("Admin Dashboard");
    }

    @GetMapping("/customer/dashboard")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> customerDashboard(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = getUsername(authentication);
        return ResponseEntity.ok("Customer Dashboard");
    }

    private String getUsername(Authentication authentication) {
        if(authentication == null || !authentication.isAuthenticated()){
            return "Unknown User";
        }
        Object principal = authentication.getPrincipal();
        if(principal instanceof Users){
            return ((Users)principal).getUsername();
        }
        return principal.toString();
    }
}
