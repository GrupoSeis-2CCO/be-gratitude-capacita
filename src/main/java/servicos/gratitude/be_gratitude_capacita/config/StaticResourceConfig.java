package servicos.gratitude.be_gratitude_capacita.config;

// NOTE: This class is intentionally NOT annotated with @Configuration to avoid
// duplicate bean conflicts. The active resource handler lives in
// servicos.gratitude.be_gratitude_capacita.infraestructure.config.StaticResourceConfig
// and should be used instead.

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

class StaticResourceConfigShadow implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Resolve absolute path to the local 'uploads' directory
        Path uploadsDir = Paths.get("uploads").toAbsolutePath().normalize();
        String uploadsLocation = uploadsDir.toUri().toString();

        // Map '/uploads/**' to serve files from the local uploads directory
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadsLocation)
                // cache a bit to reduce repeated disk access, tweak as needed
                .setCachePeriod(3600);
    }
}
