package com.TaskTracker.Todo.List.Security;

import com.TaskTracker.Todo.List.Entity.RefreshToken;
import com.TaskTracker.Todo.List.Repository.RefreshTokenRepository;
import com.TaskTracker.Todo.List.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    public RefreshToken createRefreshToken(Long userId){
        RefreshToken token = RefreshToken.builder()
                .user(userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found ")))
                .expiryDate((Instant.now().plusMillis(refreshTokenDurationMs)))
                .token(UUID.randomUUID().toString())
                .build();
        return refreshTokenRepository.save(token);
    }
    public boolean isRefreshTokenExpired(RefreshToken token){
        return token.getExpiryDate().isBefore(Instant.now());
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public void delete(RefreshToken tokenFromDB) {
        refreshTokenRepository.delete(tokenFromDB);
    }
}
