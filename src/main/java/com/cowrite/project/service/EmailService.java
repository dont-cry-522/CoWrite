package com.cowrite.project.service;

/**
 * Email Service
 *
 * @author heathcetide
 */
public interface EmailService {

    void sendWelcomeEmail(String username, String email) throws Exception;


    void sendEmailWithRetry(String email, String code, int maxRetries, long retryInterval) throws Exception;
}
