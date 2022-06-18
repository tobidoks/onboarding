package com.olyves.authentication.dao;

import com.olyves.onboarding.common.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById();

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
