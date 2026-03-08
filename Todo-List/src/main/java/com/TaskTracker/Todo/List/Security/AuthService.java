package com.TaskTracker.Todo.List.Security;

import com.TaskTracker.Todo.List.Dto.LoginRequestDto;
import com.TaskTracker.Todo.List.Dto.LoginResponseDto;
import com.TaskTracker.Todo.List.Dto.RefreshResponseDto;
import com.TaskTracker.Todo.List.Entity.RefreshToken;
import com.TaskTracker.Todo.List.Entity.User;
import com.TaskTracker.Todo.List.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    public RefreshResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),loginRequestDto.getPassword())
        );
        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
//        return new LoginResponseDto(token);
        return new RefreshResponseDto(token,refreshToken.getToken());
    }

    public LoginResponseDto signup(LoginRequestDto signupRequestDto) {
        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);
        if(user != null){
            throw new IllegalArgumentException("user already exists");
        }

        user = userRepository.save(User.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build());

        String token = authUtil.generateAccessToken(user);

        return new LoginResponseDto(token);
    }
}
