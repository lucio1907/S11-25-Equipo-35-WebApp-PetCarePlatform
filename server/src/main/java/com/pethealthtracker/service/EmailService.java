package com.pethealthtracker.service;

public interface EmailService {
    void sendWelcomeEmail(String to, String firstName, String token);
}
