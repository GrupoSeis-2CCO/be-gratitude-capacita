package servicos.gratitude.be_gratitude_capacita.infraestructure.config;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class PdfSeedInitializer {

    private static final Logger log = LoggerFactory.getLogger(PdfSeedInitializer.class);

    @Value("${app.uploads.dir:uploads}")
    private String uploadsDir;

    @EventListener(ApplicationReadyEvent.class)
    public void ensureSamplePdfsHaveContent() {
        try {
            Path base = Paths.get(uploadsDir).toAbsolutePath().normalize();
            if (!Files.exists(base)) {
                Files.createDirectories(base);
            }

            List<String> files = Arrays.asList(
                    "reurb_intro.pdf",
                    "reurb_instrumentos.pdf",
                    "reurb_modelos.pdf",
                    "reurb_participacao.pdf",
                    "regulacao_apostila_intro.pdf"
            );

            for (String name : files) {
                Path p = base.resolve(name);
                if (!Files.exists(p) || isLikelyEmptyPdf(p)) {
                    createPdfWithText(p, name);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to initialize sample PDFs: {}", e.getMessage());
        }
    }

    private boolean isLikelyEmptyPdf(Path p) {
        try {
            return Files.size(p) < 512; // very small => likely placeholder
        } catch (IOException e) {
            return true;
        }
    }

    private void createPdfWithText(Path path, String title) {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                cs.newLineAtOffset(72, page.getMediaBox().getHeight() - 72);
                cs.showText("Apostila - " + cleanTitle(title));
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(72, page.getMediaBox().getHeight() - 110);
                cs.showText("Este é um PDF gerado automaticamente para fins de demonstração.");
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
                cs.newLineAtOffset(72, page.getMediaBox().getHeight() - 130);
                cs.showText("Gerado em: " + LocalDateTime.now());
                cs.endText();
            }

            File f = path.toFile();
            doc.save(f);
            log.info("Seeded PDF with content at: {}", f.getAbsolutePath());
        } catch (IOException e) {
            log.warn("Failed to write PDF {}: {}", path, e.getMessage());
        }
    }

    private String cleanTitle(String t) {
        String base = t;
        if (base.toLowerCase().endsWith(".pdf")) {
            base = base.substring(0, base.length() - 4);
        }
        return base.replace('_', ' ');
    }
}
