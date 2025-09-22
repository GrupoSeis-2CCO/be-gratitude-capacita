package servicos.gratitude.be_gratitude_capacita.S3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/arquivos")
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        logger.info("Recebendo upload de arquivo: {} (size: {} bytes)", file.getOriginalFilename(), file.getSize());
        try {
            String url = s3Service.uploadFile(file, "bronze");
            logger.info("Upload realizado com sucesso. URL: {}", url);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            logger.error("Erro ao enviar arquivo para o S3", e);
            return ResponseEntity.status(500).body("Erro ao enviar arquivo: " + e.getMessage());
        }
    }
}