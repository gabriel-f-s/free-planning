package com.atlasys.freeplanning.identity.service;

import com.atlasys.freeplanning.identity.dto.AuthenticationRequest;
import com.atlasys.freeplanning.identity.dto.AuthenticationResponse;
import com.atlasys.freeplanning.identity.dto.RegisterRequest;
import com.atlasys.freeplanning.identity.dto.UserResponse;
import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.identity.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthenticationResponse login(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        User user = (User) auth.getPrincipal();
        if (user == null) throw new UsernameNotFoundException("User not found");

        String token = tokenService.generateToken(user);

        return new AuthenticationResponse(token, new UserResponse(user));
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (this.userRepository.findByEmail(request.email()).isPresent())
            throw new EntityExistsException("E-mail already registered");

        String encryptedPassword = passwordEncoder.encode(request.password());

        User newUser = new User();
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        newUser.setPassword(encryptedPassword);
        newUser.setOccupation(request.occupation());
        newUser.setHourlyRate(request.hourlyRate());
        this.userRepository.save(newUser);

        String token = tokenService.generateToken(newUser);

        return new AuthenticationResponse(token, new UserResponse(newUser));
    }
}
