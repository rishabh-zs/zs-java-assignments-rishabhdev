package com.zs.assignment11.service;

import com.zs.assignment11.dao.UserJpaRepository;
import com.zs.assignment11.model.User;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * The type User service.
 */
@Service
@CacheConfig(cacheNames = "users")
public class UserService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Instantiates a new User service.
     *
     * @param userJpaRepository the user jpa repository
     */
    public UserService(UserJpaRepository userJpaRepository, PasswordEncoder passwordEncoder) {
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Gets user by username.
     *
     * @param username the username
     * @return the user by username
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(key = "#username", unless = "#result == null")
    @Observed(name = "user.service", contextualName = "Fetch User By Username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Request received to fetch user with username: {}", username);

        User user = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Cacheable(key = "'all'", unless = "#result == null")
    @Observed(name="user.service",contextualName = "Fetch All Users")
    public List<User> getAllUsers(){
        log.info("Request received to get all users");
        List<User> users;
        try{
            users = userJpaRepository.findAll();
            log.info("All users retrieved successfully");
        }catch(Exception ex){
            log.error("Error retrieving all users: {}", ex.getMessage());
            throw new RuntimeException("Failed to fetch all users.", ex);
        }
        return users;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    @Observed(name = "user.service", contextualName = "Add User")
    public User addUser(User user) {
        log.info("Request received to save user with username: {}", user.getUsername());

        if (userJpaRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userJpaRepository.save(user);
            log.info("User saved successfully");
        } catch (DataIntegrityViolationException ex) {
            log.error("Data integrity violation while saving user: {}", ex.getMessage());
            throw new IllegalStateException("Unable to save user due to data integrity constraints", ex);
        }
        return user;
    }

}
