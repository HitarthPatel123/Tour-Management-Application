package com.tours.backend.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tours.backend.Entities.Users;
import com.tours.backend.Repository.UserRepo;
import com.tours.backend.Service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2user =  (OAuth2User) authentication.getPrincipal();

        String email = oauth2user.getAttribute("email");
        String name = oauth2user.getAttribute("name");
        String contactNumber = oauth2user.getAttribute("contactNumber");

        Users user = userRepo.findByEmail(email)
                .orElseGet(() ->{
                    Users newUser = new Users();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setContactNumber(contactNumber);
                    newUser.setRole("ROLE_CUSTOMER");
                    newUser.setEnabled(true);

                    String username=name.replaceAll("\\s+", "").toLowerCase();
                    String rawPassword = "01in" + username;
                    newUser.setPassword(passwordEncoder.encode(rawPassword));

                    Users savedUser = userRepo.save(newUser);
                    return savedUser;
                });

        String token = jwtService.generateToken(user.getEmail());

        String redirectUrl = "http://localhost:5173/authSuccess";
        String finalRedirectUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("token", token)
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, finalRedirectUrl);
    }
}
