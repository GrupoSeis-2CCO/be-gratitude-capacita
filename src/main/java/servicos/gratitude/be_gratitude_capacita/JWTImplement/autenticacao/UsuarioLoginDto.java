package servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioLoginDto {

    @Schema(description = "Email do usuario", example = "john@doe.com")
    private String email;

    @Schema(description = "Senha do usuario", example = "123456")
    private String senha;
}