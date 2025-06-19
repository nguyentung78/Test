package com.ra.demo.repository;

import com.ra.demo.model.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUserByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Users> findByEmail(String email);

    @Query("SELECT u FROM Users u WHERE u.isDeleted = false")
    Page<Users> findAllActiveUsers(Pageable pageable);
}