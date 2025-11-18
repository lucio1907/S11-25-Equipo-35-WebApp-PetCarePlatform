package com.pethealthtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class PetHealthTrackerApplication {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(PetHealthTrackerApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "/");
        
        // Asegurarse de que el contextPath termine con /
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        
        // Construir las URLs
        String apiUrl = "http://localhost:%s%s".formatted(port, contextPath);
        String docsUrl = "http://localhost:%s%sswagger-ui.html".formatted(port, contextPath);
        
        // Mensaje formateado
        String message = (
                """
                
                ╔════════════════════════════════════════════════════════════════════╗
                ║                                                                  ║
                ║   🐾  ¡Pet Health Tracker está en funcionamiento! 🐾              ║
                ║                                                                  ║
                ║   🌐  URL de la API: %-43s║
                ║                                                                  ║
                ║   📚  Documentación: %-40s║
                ║                                                                  ║
                ╚════════════════════════════════════════════════════════════════════╝
                """).formatted(
                apiUrl,
                docsUrl
        );
        
        System.out.println(message);
        System.out.println("🔍 Para detener la aplicación, presiona Ctrl + C");
    }
}
