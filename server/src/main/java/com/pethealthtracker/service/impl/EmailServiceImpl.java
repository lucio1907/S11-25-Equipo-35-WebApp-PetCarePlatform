package com.pethealthtracker.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.pethealthtracker.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${EMAIL_USERNAME}")
    private String fromEmail;

    @Value("${API_URL}")
    private String apiUrl;

    @Async
    @Override
    // @Transactional // ELIMINADO: Bloquea recursos innecesariamente
    public void sendWelcomeEmail(String to, String firstName, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // El 'true' aquí es para multipart (adjuntos/imágenes embebidas)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("¡Verifica tu cuenta en My Pet Cloud!");

            String verificationLink = apiUrl + "/api/auth/verify-email?token=" + token;

            String htmlContent = """
                <html>
                <body>
                    <h2>Hola %s,</h2>
                    <p>Gracias por registrarte en PetHealthTracker.</p>
                    <p>Por favor, haz clic en el siguiente enlace para verificar tu cuenta:</p>
                    <a href="%s" style="background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">VERIFICAR EMAIL</a>
                    <p>O copia este enlace en tu navegador: <br> %s</p>
                    <br>
                    <p>Saludos,<br>El equipo.</p>
                </body>
                </html>
                """.formatted(firstName, verificationLink, verificationLink);

            // CORRECCIÓN PRINCIPAL: Agregar ', true'
            helper.setText(htmlContent, true); 

            mailSender.send(message);
            log.info("Email enviado a {}", to);
            
        } catch (Exception e) {
            // CORRECCIÓN: Usar log en vez de throw, porque en @Async nadie atrapa la excepción
            log.error("Error enviando el email a {}: {}", to, e.getMessage());
        }
    }
}