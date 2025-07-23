package com.cowrite.project.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class CodeCreateUtils {

    @Autowired
    private RedisUtils redisUtils;

    private final Random random = new Random();
    private static final long EXPIRATION_TIME = 5 * 60 * 1000; // 5分钟，单位是毫秒

    public String generateCode() {
        return String.format("%06d", random.nextInt(999999));
    }

    public void sendVerificationCode(String email,String code) {
        String normalizedEmail = normalizeEmail(email);
        redisUtils.set(normalizedEmail, new CodeWithTimestamp(code).code, EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

    public Boolean verifyCode(String email, String inputCode) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail == null || inputCode == null) {
            return false;
        }

        CodeWithTimestamp storedCode = redisUtils.get(normalizedEmail, CodeWithTimestamp.class);
        if (storedCode == null || !storedCode.getCode().equals(inputCode)) {
            System.out.println("储存的code为"+storedCode.getCode());
            return false;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - storedCode.getTimestamp() >= EXPIRATION_TIME) {
            redisUtils.delete(normalizedEmail);
            return false;
        }

        redisUtils.delete(normalizedEmail); // 验证成功后删除
        return true;
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private static class CodeWithTimestamp implements Serializable {
        private  String code;
        private  long timestamp;

        // ✅ 添加无参构造函数（即使不使用，反序列化也需要）
        public CodeWithTimestamp() {
            this.code = null;
            this.timestamp = 0;
        }

        public CodeWithTimestamp(String code) {
            this.code = code;
            this.timestamp = System.currentTimeMillis();
        }

        public String getCode() {
            return code;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}