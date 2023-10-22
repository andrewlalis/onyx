package com.andrewlalis.onyx.auth.service;

import com.andrewlalis.onyx.auth.model.User;
import com.andrewlalis.onyx.auth.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
}
