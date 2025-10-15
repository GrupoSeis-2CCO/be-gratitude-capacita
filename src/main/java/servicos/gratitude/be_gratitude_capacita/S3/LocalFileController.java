package servicos.gratitude.be_gratitude_capacita.S3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/arquivos")
public class LocalFileController {

    @PostMapping("/upload-local")
    public ResponseEntity<String> uploadFileLocal(@RequestParam("file") MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) originalFilename = "file";
            String filename = originalFilename.replaceAll("^.*[\\\\/]", "");
            filename = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
            String uniqueFilename = System.currentTimeMillis() + "_" + filename;

            Path uploadsDir = Paths.get("uploads").toAbsolutePath().normalize();
            System.out.println("Uploads directory resolved to: " + uploadsDir.toString());

            Files.createDirectories(uploadsDir);

            Path dest = uploadsDir.resolve(uniqueFilename).toAbsolutePath().normalize();
            System.out.println("Uploading file to: " + dest.toString());

            if (dest.getParent() != null) {
                Files.createDirectories(dest.getParent());
            }

            // usar transferTo para evitar carregar bytes na memória
            file.transferTo(dest.toFile());

            // Retornamos URL relativa que será servida pelo servidor (ex: /uploads/123_file.pdf)
            String url = "/uploads/" + uniqueFilename;
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao salvar arquivo: " + e.getMessage());
        }
    }
}
