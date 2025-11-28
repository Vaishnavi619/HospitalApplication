package com.example.demo.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    
    @Value("${mailtrap.api.url}")
    private String mailtrapApiUrl;

    @Value("${mailtrap.api.token}")
    private String mailtrapApiToken;

    @Value("${mailtrap.from.email}")
    private String mailtrapFromEmail;

    @Value("${mailtrap.from.name}")
    private String mailtrapFromName;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(mailtrapApiToken); 

  
        Map<String, Object> payload = new HashMap<>();
        payload.put("from", Map.of(
            "email", mailtrapFromEmail,
            "name", mailtrapFromName
        ));
        payload.put("to", List.of(Map.of("email", toEmail)));
        payload.put("subject", "Your OTP for Hospital Login");
        payload.put("text", "Your OTP is: " + otp);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(mailtrapApiUrl, request, String.class);
            System.out.println("✅ OTP email sent successfully to Mailtrap");
        } catch (Exception e) {
            System.out.println("❌ Failed to send OTP via Mailtrap: " + e.getMessage());
        }
    }
}
