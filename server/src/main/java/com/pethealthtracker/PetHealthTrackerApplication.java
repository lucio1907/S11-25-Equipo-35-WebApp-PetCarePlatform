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
        // 1. Obtener el protocolo (http o https)
        // Verificamos si SSL está habilitado, si no, por defecto es "http"
        boolean isSsl = env.getProperty("server.ssl.enabled", Boolean.class, false);
        String protocol = isSsl ? "https" : "http";

        // 2. Obtener el Host (Dominio o IP)
        String host = env.getProperty("server.host", "localhost");

        // 3. Tu código existente para Puerto y ContextPath
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "/");

        // Asegurarse de que el contextPath termine con /
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        // 4. Construir las URLs dinámicamente
        String apiUrl = "%s://%s:%s%s".formatted(protocol, host, port, contextPath);
        String docsUrl = "%s://%s:%s%sswagger-ui.html".formatted(protocol, host, port, contextPath);

        // Mensaje formateado
        String message = ("""

                ╔════════════════════════════════════════════════════════════════════╗
                ║                                                                    ║
                ║   🐾  ¡Pet Health Tracker está en funcionamiento! 🐾               ║
                ║                                                                    ║
                ║   🌐  URL de la API: %-43s   ║
                ║                                                                    ║
                ║   📚  Documentación: %-40s     ║
                ║                                                                    ║
                ╚════════════════════════════════════════════════════════════════════╝
                """).formatted(
                apiUrl,
                docsUrl);

        System.out.println(message);
        System.out.println("🔍 Para detener la aplicación, presiona Ctrl + C");
    }
}
