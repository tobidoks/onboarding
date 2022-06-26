package com.olyves.authentication.dao;

import com.olyves.onboarding.common.model.UserRole;
import com.olyves.onboarding.common.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByName(Role role);
}
