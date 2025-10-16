package servicos.gratitude.be_gratitude_capacita.infraestructure.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/proxy")
public class ImageProxyController {

    @GetMapping("/image")
    public ResponseEntity<byte[]> proxyImage(@RequestParam("url") String url) {
        if (url == null || url.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        HttpURLConnection conn = null;
        try {
            URL remote = new URL(url);
            conn = (HttpURLConnection) remote.openConnection();
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            int status = conn.getResponseCode();
            // follow up to 3 redirects
            int redirects = 0;
            while (status / 100 == 3 && redirects < 3) {
                String loc = conn.getHeaderField("Location");
                if (loc == null) break;
                conn.disconnect();
                remote = new URL(loc);
                conn = (HttpURLConnection) remote.openConnection();
                conn.setInstanceFollowRedirects(true);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(10000);
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                status = conn.getResponseCode();
                redirects++;
            }
            if (status != HttpURLConnection.HTTP_OK) {
                return ResponseEntity.status(status).build();
            }
            String contentType = conn.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                // Force image content type when unknown; browser only needs bytes
                contentType = MediaType.IMAGE_JPEG_VALUE;
            }
            try (InputStream is = conn.getInputStream()) {
                byte[] bytes = StreamUtils.copyToByteArray(is);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(bytes);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
