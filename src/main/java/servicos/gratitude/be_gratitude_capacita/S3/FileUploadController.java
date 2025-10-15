package servicos.gratitude.be_gratitude_capacita.S3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@RestController
@RequestMapping("/arquivos")
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "tipoBucket", defaultValue = "bronze") String tipoBucket) {
        logger.info("Recebendo upload de arquivo: {} (size: {} bytes)", file.getOriginalFilename(), file.getSize());
        try {
            // Em ambiente de desenvolvimento preferimos salvar localmente e retornar uma URL
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) originalFilename = "file";
            String filename = originalFilename.replaceAll("^.*[\\\\/]", "");
            filename = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
            String uniqueFilename = System.currentTimeMillis() + "_" + filename;

            // resolve uploads dir as an absolute, normalized path so we know exactly where files will be written
            java.nio.file.Path uploadsDir = java.nio.file.Paths.get("uploads").toAbsolutePath().normalize();
            logger.info("Uploads directory resolved to: {}", uploadsDir.toString());

            // ensure the uploads directory exists
            java.nio.file.Files.createDirectories(uploadsDir);

            java.nio.file.Path dest = uploadsDir.resolve(uniqueFilename).toAbsolutePath().normalize();
            logger.info("Uploading file to resolved path: {}", dest.toString());

            // ensure parent directories for the destination file exist (defensive)
            if (dest.getParent() != null) {
                java.nio.file.Files.createDirectories(dest.getParent());
            }

            // usar transferTo para evitar carregar bytes na memória
            file.transferTo(dest.toFile());

            String url = "/uploads/" + uniqueFilename;
            logger.info("Upload local realizado com sucesso. URL: {}", url);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            logger.error("Erro ao salvar arquivo localmente", e);
            return ResponseEntity.status(500).body("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    @PatchMapping("/apostila/{idApostila}/url")
    public ResponseEntity<String> patchUrlApostila(
            @PathVariable Integer idApostila,
            @RequestBody Map<String, String> body) {
        logger.info("=== PATCH REQUEST RECEBIDO ===");
        logger.info("ID da Apostila: {}", idApostila);
        logger.info("Body completo: {}", body);

        try {
            String url = body.get("url");
            logger.info("URL extraída: {}", url);

            if (url == null || url.trim().isEmpty()) {
                logger.error("ERRO: URL está vazia ou nula");
                return ResponseEntity.badRequest().body("URL não pode estar vazia");
            }

            logger.info("Chamando s3Service.patchAtualizarUrlArquivo...");
            s3Service.patchAtualizarUrlArquivo(idApostila, url);
            logger.info("Método do service executado com sucesso");

            String responseMessage = "URL da apostila atualizada com sucesso";
            logger.info("Resposta: {}", responseMessage);
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            logger.error("ERRO no controller: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Erro ao atualizar URL: " + e.getMessage());
        }
    }
}