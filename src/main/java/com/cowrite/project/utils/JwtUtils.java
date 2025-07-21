package com.cowrite.project.utils;

import com.cowrite.project.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${hibiscus.jwt.secret}")
    private String secret;
    
    @Value("${hibiscus.jwt.expiration}")
    private Long expiration;

    @Value("${hibiscus.jwt.header}")
    private String tokenHeader;

    @Value("${hibiscus.jwt.token-start}")
    private String tokenPrefix;

    public JwtUtils(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(tokenHeader);
        if (header != null && header.startsWith(tokenPrefix + " ")) {
            return header.substring(tokenPrefix.length() + 1);
        }
        return null;
    }

    public String generateToken(User user) {
        if (user == null) {
            return null;
        }
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("id", user.getId());
        objectObjectHashMap.put("username", user.getUsername());
        objectObjectHashMap.put("email", user.getEmail());
        objectObjectHashMap.put("avatarUrl", user.getAvatarUrl());
        objectObjectHashMap.put("status", user.getStatus());
        objectObjectHashMap.put("themeDark", user.getThemeDark());
        objectObjectHashMap.put("emailNotifications", user.getEmailNotifications());
        objectObjectHashMap.put("language", user.getLanguage());
        objectObjectHashMap.put("bio", user.getBio());
        return Jwts.builder()
            .setSubject(String.valueOf(user.getId()))
            .setClaims(objectObjectHashMap)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !isTokenBlacklisted(token);
        } catch (Exception e) {
            log.error("Invalid JWT token: ", e);
            return false;
        }
    }

    private boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember("jwt:blacklist:" + getUserFromToken(token).getEmail(), token));
    }

    public String refreshToken(String oldToken) {
        if (validateToken(oldToken)) {
            Date now = new Date();
            Date expiryDate = getExpirationDateFromToken(oldToken);
            if (expiryDate.getTime() - now.getTime() < 300000) { // 5分钟内过期
                return generateToken(getUserFromToken(oldToken));
            }
        }
        return null;
    }
    
    public void invalidateToken(String token) {
        redisTemplate.opsForSet().add("jwt:blacklist:"+getUserFromToken(token).getId(), token);
    }

    @Scheduled(cron = "0 0 3 * * ?") // 每天凌晨3点执行
    public void cleanExpiredBlacklist() {
        Set<String> keys = redisTemplate.keys("jwt:blacklist:*");
        if (keys != null) {
            keys.forEach(key -> {
                Long count = redisTemplate.opsForZSet().removeRangeByScore(key, 0,
                    System.currentTimeMillis() - expiration * 1000);
                log.info("清理过期黑名单令牌: {} 个", count);
            });
        }
    }

    private Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody()
            .getExpiration();
    }

    public User getUserFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            User user = new User();

            // 获取用户 ID，确保不为 null
            Long userId = claims.get("id", Long.class);
            user.setId(userId);

            // 获取用户名，并检查是否为空
            String username = claims.get("username", String.class);
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("Username is missing or empty in the token");
            }
            user.setUsername(username);

            // 获取邮箱
            String email = claims.get("email", String.class);
            user.setEmail(email);

            // 获取头像 URL
            String avatarUrl = claims.get("avatarUrl", String.class);
            user.setAvatarUrl(avatarUrl);

            // 获取状态
            String status = claims.get("status", String.class);
            user.setStatus(status);

            return user;
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", token, e);
            throw new JwtException("Invalid token provided.");
        }
    }

}