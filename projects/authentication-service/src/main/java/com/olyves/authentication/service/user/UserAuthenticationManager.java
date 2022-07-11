package com.olyves.authentication.service.user;

import com.olyves.authentication.dao.UserRepository;
import com.olyves.authentication.dao.UserRoleRepository;
import com.olyves.authentication.exception.AuthenticationException;
import com.olyves.authentication.payload.request.SignUpRequest;
import com.olyves.authentication.util.JwtUtils;
import com.olyves.onboarding.common.model.User;
import com.olyves.onboarding.common.model.UserRole;
import com.olyves.onboarding.common.model.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserAuthenticationManager {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    private final UserRoleRepository roleRepository;

    private final PasswordEncoder encoder;

    public UserAuthenticationManager(AuthenticationManager authenticationManager,
                                     JwtUtils jwtUtils,
                                     UserRepository userRepository,
                                     UserRoleRepository roleRepository,
                                     PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public void authenticateUserSignUp(SignUpRequest signUpRequest) throws AuthenticationException {
        String password = signUpRequest.getPassword();
        String email = signUpRequest.getEmail();
        Set<String> requestRoles = signUpRequest.getRoles();

        if (userRepository.existsByEmail(email)) {
            throw new AuthenticationException(String.format("Email address %s already exits", email));
        }

        User.UserBuilder builder = User.builder();
        builder.email(email)
                .password(encoder.encode(password));

        Set<UserRole> userRoles = getRoles(requestRoles);

        builder.userRoles(userRoles);
        userRepository.save(builder.build());
        log.info("User with email {} has been registered successfully", email);
    }

    private Set<UserRole> getRoles(Set<String> requestRoles) {
        Set<UserRole> userRoles = new HashSet<>();

        if (CollectionUtils.isEmpty(requestRoles)) {
            Optional<UserRole> userRole = roleRepository.findByName(Role.CLIENT_EMPLOYEE);
            if (userRole.isEmpty()) {
                log.error("CLIENT EMPLOYEE userRole not found in db");
            } else {
                userRoles.add(userRole.get());
            }
        } else {
            for (String r : requestRoles) {
                if (EnumUtils.isValidEnum(Role.class, r)) {
                    log.debug("{} role not recognized", r);
                    continue;
                }
                Optional<UserRole> userRole = roleRepository.findByName(EnumUtils.getEnum(Role.class, r));
                if (userRole.isEmpty()) {
                    log.error("{} role not found in db", r);
                    continue;
                }
                userRoles.add(userRole.get());
            }
        }
        return userRoles;
    }

}
