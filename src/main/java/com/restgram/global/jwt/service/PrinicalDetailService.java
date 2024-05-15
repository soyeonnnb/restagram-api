package com.restgram.global.jwt.service;

import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrinicalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("load user by username");
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        return createUser(user);
    }

    private org.springframework.security.core.userdetails.User createUser(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getType());
        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                "",
                Collections.singletonList(grantedAuthority)
        );
    }
}
