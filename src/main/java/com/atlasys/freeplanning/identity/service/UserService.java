package com.atlasys.freeplanning.identity.service;

import com.atlasys.freeplanning.identity.dto.UserResponse;
import com.atlasys.freeplanning.identity.dto.UserUpdateEmailRequest;
import com.atlasys.freeplanning.identity.dto.UserUpdatePasswordRequest;
import com.atlasys.freeplanning.identity.dto.UserUpdateRequest;
import com.atlasys.freeplanning.identity.exception.EntityChangeFailureException;
import com.atlasys.freeplanning.identity.mapper.UserMapper;
import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.identity.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserResponse find(User loggedUser) {
        return new UserResponse(loggedUser);
    }

    @Transactional
    public UserResponse update(User loggedUser, UserUpdateRequest request) {
        mapper.updateEntityFromDto(request, loggedUser);
        return new UserResponse(repository.save(loggedUser));
    }

    @Transactional(rollbackFor = {EntityChangeFailureException.class, EntityExistsException.class})
    public UserResponse changeEmail(User loggedUser, @NonNull UserUpdateEmailRequest request) {
        if (repository.findByEmail(request.email()).isPresent())
            throw new EntityExistsException("E-mail already registered");
        if (!passwordEncoder.matches(request.password(), loggedUser.getPassword()))
            throw new EntityChangeFailureException("Invalid password");

        loggedUser.setEmail(request.email());
        return new UserResponse(repository.save(loggedUser));
    }

    @Transactional(rollbackFor = EntityChangeFailureException.class)
    public void changePassword(User loggedUser, @NonNull UserUpdatePasswordRequest request) {
        if (!passwordEncoder.matches(request.oldPassword(), loggedUser.getPassword()))
            throw new EntityChangeFailureException("Invalid password");

        if (!request.newPassword().equals(request.confirmNewPassword()))
            throw new EntityChangeFailureException("New passwords must match");

        if (request.newPassword().equals(request.oldPassword()))
            throw new EntityChangeFailureException("New password must be different from old password");

        String hashedNewPassword = passwordEncoder.encode(request.newPassword());
        loggedUser.setPassword(hashedNewPassword);
        repository.save(loggedUser);
    }
}
