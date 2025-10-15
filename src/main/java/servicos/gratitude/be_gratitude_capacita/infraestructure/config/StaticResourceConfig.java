package servicos.gratitude.be_gratitude_capacita.infraestructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Path.of("uploads");
        String uploadPath = uploadDir.toAbsolutePath().toUri().toString();
        // Serve files under /uploads/** mapped to the uploads directory
        registry.addResourceHandler("/uploads/**").addResourceLocations(uploadPath);
    }
}
