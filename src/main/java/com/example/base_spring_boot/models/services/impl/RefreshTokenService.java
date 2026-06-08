package com.example.base_spring_boot.models.services.impl;

import com.example.base_spring_boot.exceptions.HttpNotFoundException;
import com.example.base_spring_boot.models.entities.RefreshToken;
import com.example.base_spring_boot.models.entities.User;
import com.example.base_spring_boot.models.repositories.IUserRepository;
import com.example.base_spring_boot.models.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final IUserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshToken createRefreshToken(Long userId) {
        User user=userRepository.findById(userId).orElseThrow(()-> new HttpNotFoundException("User not found"));
        RefreshToken refreshToken=new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(new Date(new Date().getTime() + 7*24*60*60*1000));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
    public boolean verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().before(new Date(new Date().getTime()))) {
            refreshTokenRepository.delete(refreshToken);
            return false;
        }
        return true;
    }
}
