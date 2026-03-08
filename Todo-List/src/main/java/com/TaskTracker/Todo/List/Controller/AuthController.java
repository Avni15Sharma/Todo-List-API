package com.TaskTracker.Todo.List.Controller;

import com.TaskTracker.Todo.List.Dto.LoginRequestDto;
import com.TaskTracker.Todo.List.Dto.LoginResponseDto;
import com.TaskTracker.Todo.List.Dto.RefreshRequestDto;
import com.TaskTracker.Todo.List.Dto.RefreshResponseDto;
import com.TaskTracker.Todo.List.Entity.RefreshToken;
import com.TaskTracker.Todo.List.Error.ResourceNotFoundException;
import com.TaskTracker.Todo.List.Security.AuthService;
import com.TaskTracker.Todo.List.Security.AuthUtil;
import com.TaskTracker.Todo.List.Security.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final AuthUtil authUtil;
    @PostMapping("/login")
    public ResponseEntity<RefreshResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> signup(@Valid @RequestBody LoginRequestDto signupRequestDto){
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refresh(@RequestBody RefreshRequestDto refreshToken){
        Optional<RefreshToken> token = refreshTokenService.findByToken(refreshToken.getRefreshToken());
        if(token.isEmpty()){
            throw new BadCredentialsException("Invalid Refresh Token");
        }
        RefreshToken tokenFromDB = token.get();
        if(refreshTokenService.isRefreshTokenExpired(tokenFromDB)){
            refreshTokenService.delete(tokenFromDB);
            throw new BadCredentialsException("Refresh token expired. Please login again");
        }else{
            refreshTokenService.delete(tokenFromDB);
            RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(tokenFromDB.getUser().getId());
            RefreshResponseDto responseDto = RefreshResponseDto.builder()
                    .accessToken(authUtil.generateAccessToken(tokenFromDB.getUser()))
                    .refreshToken(newRefreshToken.getToken())
                    .build();

            return ResponseEntity.ok(responseDto);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequestDto requestDto){
        String token = requestDto.getRefreshToken();
        if(token == null || token.isEmpty()){
            throw new BadCredentialsException("Refresh Token is required");
        }
        Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(token);
        if(refreshToken.isEmpty()){
            throw new BadCredentialsException("Invalid Refresh Token");
        }else{
            refreshTokenService.delete(refreshToken.get());
            return ResponseEntity.ok("Logged out successfully");
        }
    }
}
