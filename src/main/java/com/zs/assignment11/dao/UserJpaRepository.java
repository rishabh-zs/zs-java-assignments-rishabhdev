package com.zs.assignment11.dao;

import com.zs.assignment11.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface User jpa repository.
 */
public interface UserJpaRepository extends JpaRepository<User, Long> {
    /**
     * Find by username optional.
     *
     * @param userName the username
     * @return the optional
     */
    Optional<User> findByUsername(String userName);

    boolean existsByUsername(String userName);
}
