package servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioCriacaoDto {

    @Size(min = 3, max = 10)
    @Schema(description = "Nome do Usuário", example = "John Doe")
    private String nome;

    @Email
    @Schema(description = "Email do usuario", example = "john@doe.com")
    private String email;

    @Size(min = 6, max = 20)
    @Schema(description = "Senha do usuario", example = "123456")
    private String senha;
}