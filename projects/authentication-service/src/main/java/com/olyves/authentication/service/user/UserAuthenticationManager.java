package com.olyves.authentication.service.user;

import com.olyves.authentication.dao.UserRepository;
import com.olyves.authentication.dao.UserRoleRepository;
import com.olyves.authentication.payload.request.SignUpRequest;
import com.olyves.authentication.util.JwtUtils;
import com.olyves.onboarding.common.model.User;
import com.olyves.onboarding.common.model.UserRole;
import com.olyves.onboarding.common.model.enums.Role;
import com.olyves.onboarding.common.model.enums.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
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

    public MessageResponse authenticateUserSignUp(SignUpRequest signUpRequest) {
        String password = signUpRequest.getPassword();
        String email = signUpRequest.getEmail();
        Set<String> requestRoles = signUpRequest.getRoles();

        if (userRepository.existsByEmail(email)) {
            log.error("Email address {} already exits", email);
            return new MessageResponse("Email already exists", HttpStatus.BAD_REQUEST);
        }

        User.UserBuilder builder = User.builder();
        builder.email(email)
                .password(encoder.encode(password));

        Set<UserRole> userRoles = getRoles(requestRoles);

        builder.userRoles(userRoles);
        userRepository.save(builder.build());
        log.info("User with email {} has been registered successfully", email);
        return new MessageResponse("User Registered Successfully!", HttpStatus.OK);
    }

    public Set<UserRole> getRoles(Set<String> requestRoles) {
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
