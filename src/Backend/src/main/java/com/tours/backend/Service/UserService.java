package com.tours.backend.Service;

import com.tours.backend.Entities.Users;
import com.tours.backend.Repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    private UserRepo userRepository;

    private PasswordEncoder passwordEncoder;

    public void register(Users user) {
        logger.info("Attempting to register user with email: " + user.getEmail());

        if(user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_CUSTOMER");
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            logger.warning("Email already in use: " + user.getEmail());
            throw new RuntimeException("Email is already in use. Please use a different email.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("User successfully registered with email: " + user.getEmail());
    }

    public Users login(String email, String password) throws UserNotFoundException, InvalidCredentialsException {
        logger.info("Attempting to login user with email: " + email);

        Users user = userRepository.getUserByEmail(email);
        if(user == null) {
            logger.warning("User not found with email: " + email);
            throw new UserNotFoundException("Invalid email or password.");
        }

        if(!passwordEncoder.matches(password, user.getPassword())) {
            logger.warning("Incorrect password for email: " + email);
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        logger.info("User successfully logged in with email: " + email);
        return user;
    }
}
