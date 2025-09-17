package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.gateways.TokenJwtGateway;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import java.util.Date;

@Service
public class TokenJwtAdapter implements TokenJwtGateway {
    @Value("${jwt.secret}")
    private String secret;
    private static final SignatureAlgorithm ALG = SignatureAlgorithm.HS256;
    private static final long EXPIRATION = 86400000; // 1 dia em ms

    @Override
    public String gerarToken(Usuario usuario) {
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALG.getJcaName());
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("id", usuario.getIdUsuario())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, ALG)
                .compact();
    }
}
