package com.olyves.authentication.util;


import com.olyves.authentication.dao.UserRepository;
import com.olyves.authentication.service.user.UserDetailsImpl;
import com.olyves.onboarding.common.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserUtils {
    private final UserRepository userRepository;

    public UserUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String email = userDetails.getEmail();
            User user = userRepository.findByEmail(email).orElse(null);
            if (ObjectUtils.isEmpty(user) && StringUtils.isNotBlank(email)) {
                log.warn("Could not retrieve {} from database", email);
            }
            return user;
        } catch (Exception e) {
            log.error("Error while retrieving user from authentication");
        }
        return null;
    }
}
