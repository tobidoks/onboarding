package com.olyves.authentication.service.user;

import com.olyves.authentication.dao.UserRepository;
import com.olyves.authentication.payload.request.SignUpRequest;
import com.olyves.authentication.util.JwtUtils;
import com.olyves.onboarding.common.model.User;
import com.olyves.onboarding.common.model.UserRole;
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


    public UserAuthenticationManager(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }


    public MessageResponse authenticateUserSignUp(SignUpRequest signUpRequest) {
        String password = signUpRequest.getPassword();
        String email = signUpRequest.getEmail();
        Role requestRole = signUpRequest.getRole();

        if (userRepository.existsByEmail(email)) {
            log.error("Email address {} already exits", email);
            return new MessageResponse("Email already exists", HttpStatus.BAD_REQUEST);
        }

        User.UserBuilder builder = User.builder();
        builder.email(email).password(encoder.encode(password));

        UserRole userRole = getRole(requestRole);

        builder.userRole(userRole);
        userRepository.save(builder.build());
        log.info("User with username {} has been registered successfully", username);
        return new MessageResponse("User Registered Successfully!", HttpStatus.OK);
    }


    public UserRole getRole(String requestRole) {
        Set<Role> roles = new HashSet<>();

        //todo customize the role on the user table to have all the roles
        if (ObjectUtils.isEmpty(requestRole)) {
            Optional<Role> role = roleRepository.findByName(URole.USER);
            if (role.isEmpty()) {
                log.error("USER role not found in db");
            } else {
                roles.add(role.get());
            }
        } else {
            for (String r : requestRole) {
                if (!roleNameToURole.containsKey(r)) {
                    log.debug("{} role not recognized", r);
                    continue;
                }
                Optional<Role> role = roleRepository.findByName(roleNameToURole.get(r));
                if (role.isEmpty()) {
                    log.error("{} role not found in db", r);
                    continue;
                }
                roles.add(role.get());
            }
        }
        return roles;
    }

}
