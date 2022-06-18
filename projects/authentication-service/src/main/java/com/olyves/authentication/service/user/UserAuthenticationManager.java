package com.olyves.authentication.service.user;

import com.olyves.authentication.dao.UserRepository;
import com.olyves.authentication.payload.request.SignUpRequest;
import com.olyves.authentication.util.JwtUtils;
import com.olyves.onboarding.common.model.User;
import com.olyves.onboarding.common.model.enums.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserAuthenticationManager {
    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;


    public UserAuthenticationManager(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }


    public MessageResponse authenticateUserSignUp(SignUpRequest signUpRequest) {
        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();
        String email = signUpRequest.getEmail();
        Role requestRoles = signUpRequest.getRole();

        if (userRepository.existsByUsername(username)) {
            log.error("Username '{}' already exists", username);
            return new MessageResponse("Username already exists", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(email)) {
            logger.error("User with username {} has an email that already exits", username);
            return new MessageResponse("Email already exists", HttpStatus.BAD_REQUEST);
        }

        User.UserBuilder builder = User.builder();
        builder.email(email)
                .password(encoder.encode(password));

        Set<Role> roles = getRoles(requestRoles);

        builder.roles(roles);
        userRepository.save(builder.build());
        logger.info("User with username {} has been registered successfully", username);
        return new MessageResponse("User Registered Successfully!", HttpStatus.OK);
    }


    public Set<Role> getRoles(Set<String> requestRoles) {
        Set<Role> roles = new HashSet<>();

        if (ObjectUtils.isEmpty(requestRoles)) {
            Optional<Role> role = roleRepository.findByName(URole.USER);
            if (role.isEmpty()) {
                logger.error("USER role not found in db");
            } else {
                roles.add(role.get());
            }
        } else {
            for (String r : requestRoles) {
                if (!roleNameToURole.containsKey(r)) {
                    logger.debug("{} role not recognized", r);
                    continue;
                }
                Optional<Role> role = roleRepository.findByName(roleNameToURole.get(r));
                if (role.isEmpty()) {
                    logger.error("{} role not found in db", r);
                    continue;
                }
                roles.add(role.get());
            }
        }
        return roles;
    }

}
