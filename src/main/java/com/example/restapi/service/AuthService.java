package com.example.restapi.service;

import com.example.restapi.dto.LoginRequest;
import com.example.restapi.dto.LoginResponse;
import com.example.restapi.entity.UserAccount;
import com.example.restapi.exception.InvalidCredentialsException;
import com.example.restapi.repository.UserAccountRepository;
import com.example.restapi.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

/**
 * Authentication business logic.
 *
 * <p>Validates credentials against the database and mints a JWT on success.
 * In a production system, you would compare the supplied password against
 * a {@code BCryptPasswordEncoder}-encoded hash; this demo compares plaintext
 * to keep the seed data readable.
 */
@Service
public class AuthService {

    private final UserAccountRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserAccountRepository userRepository, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Verify the credentials in {@code request} and return a JWT response.
     *
     * @throws InvalidCredentialsException if the user does not exist or the
     *         password does not match.
     */
    public LoginResponse login(LoginRequest request) {
        UserAccount user = userRepository.findByUserId(request.userId())
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.getPassword().equals(request.password())) {
            throw new InvalidCredentialsException();
        }

        String token = tokenProvider.generateToken(user.getUserId());
        return new LoginResponse(
                token,
                "Bearer",
                tokenProvider.getExpirationMs(),
                user.getUserId()
        );
    }
}
