package servicos.gratitude.be_gratitude_capacita.S3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/arquivos")
public class LocalFileController {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload-local")
    public ResponseEntity<String> uploadFileLocal(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("[LocalFileController] Recebendo upload de arquivo: " + file.getOriginalFilename());
            
            // Detectar tipo de arquivo e enviar para S3
            String contentType = file.getContentType();
            String url;
            
            if (contentType != null && contentType.equals("application/pdf")) {
                // Enviar PDF para S3
                System.out.println("[LocalFileController] Detectado arquivo PDF, enviando para S3...");
                url = s3Service.uploadPdfFile(file);
            } else if (contentType != null && contentType.startsWith("image/")) {
                // Enviar imagem para S3
                System.out.println("[LocalFileController] Detectado arquivo de imagem, enviando para S3...");
                url = s3Service.uploadCourseImage(file);
            } else {
                // Para outros tipos, também enviar como PDF
                System.out.println("[LocalFileController] Tipo de arquivo: " + contentType + ", enviando como PDF para S3...");
                url = s3Service.uploadPdfFile(file);
            }
            
            System.out.println("[LocalFileController] Upload para S3 realizado com sucesso. URL: " + url);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao salvar arquivo: " + e.getMessage());
        }
    }
}
