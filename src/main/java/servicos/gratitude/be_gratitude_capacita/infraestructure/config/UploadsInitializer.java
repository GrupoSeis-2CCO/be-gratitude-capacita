package servicos.gratitude.be_gratitude_capacita.infraestructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class UploadsInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(UploadsInitializer.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Path uploadsDir = Path.of("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadsDir);
            logger.info("Uploads directory ensured at startup: {}", uploadsDir.toString());
        } catch (Exception e) {
            logger.error("Failed to create uploads directory at startup: {}", uploadsDir.toString(), e);
            throw e;
        }
    }
}
