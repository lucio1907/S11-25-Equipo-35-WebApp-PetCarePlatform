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
        String apiUrl = String.format("http://localhost:%s%s", port, contextPath);
        String docsUrl = String.format("http://localhost:%s%sswagger-ui.html", port, contextPath);
        
        // Mensaje formateado
        String message = String.format(
            "\n" +
            "╔════════════════════════════════════════════════════════════════════╗\n" +
            "║                                                                  ║\n" +
            "║   🐾  ¡Pet Health Tracker está en funcionamiento! 🐾              ║\n" +
            "║                                                                  ║\n" +
            "║   🌐  URL de la API: %-43s║\n" +
            "║                                                                  ║\n" +
            "║   📚  Documentación: %-40s║\n" +
            "║                                                                  ║\n" +
            "╚════════════════════════════════════════════════════════════════════╝\n",
            apiUrl,
            docsUrl
        );
        
        System.out.println(message);
        System.out.println("🔍 Para detener la aplicación, presiona Ctrl + C");
    }
}
