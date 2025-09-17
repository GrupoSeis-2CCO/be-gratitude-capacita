package servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioListarDto {

    @Schema(description = "Id do usuario", example = "1")
    private Integer idUsuario;

    @Schema(description = "Nome do usuario", example = "John Doe")
    private String nome;

    @Schema(description = "Email do usuario", example = "john@doe.com")
    private String email;
}
