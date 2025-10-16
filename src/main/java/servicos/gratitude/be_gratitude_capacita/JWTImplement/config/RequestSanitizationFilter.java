package servicos.gratitude.be_gratitude_capacita.JWTImplement.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Filter that normalizes the request URI by decoding percent-encoding and removing
 * control characters (CR, LF, etc). This prevents mappings from failing when
 * clients accidentally send trailing %0A or similar sequences.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestSanitizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(httpRequest) {
            private String sanitize(String raw) {
                if (raw == null) return null;
                String decoded = raw;
                try {
                    decoded = URLDecoder.decode(raw, StandardCharsets.UTF_8.name());
                } catch (IllegalArgumentException | java.io.UnsupportedEncodingException e) {
                    // If decoding fails, fall back to raw value
                    decoded = raw;
                }
                // Remove control characters (newline, carriage return, tab, etc.)
                String cleaned = decoded.replaceAll("[\\p{Cntrl}]", "");
                // Also trim trailing/leading whitespace just in case
                return cleaned.trim();
            }

            @Override
            public String getRequestURI() {
                String raw = super.getRequestURI();
                return sanitize(raw);
            }

            @Override
            public StringBuffer getRequestURL() {
                // Reconstruct request URL using sanitized URI
                StringBuffer sb = new StringBuffer();
                sb.append(super.getScheme()).append("://").append(super.getServerName());
                int port = super.getServerPort();
                if ((super.getScheme().equals("http") && port != 80) || (super.getScheme().equals("https") && port != 443)) {
                    sb.append(':').append(port);
                }
                String sanitized = getRequestURI();
                sb.append(sanitized);
                return sb;
            }

            @Override
            public String getServletPath() {
                return sanitize(super.getServletPath());
            }
        };

        chain.doFilter(wrapper, response);
    }
}
