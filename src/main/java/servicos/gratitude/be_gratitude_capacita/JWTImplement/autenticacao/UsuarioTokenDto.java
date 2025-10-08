package servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao;

import lombok.Data;

@Data
public class   UsuarioTokenDto {
    private Integer idUsuario;
    private String nome;
    private String email;
    private String token;
    private Integer idCargo; // Adicione este campo

    public UsuarioTokenDto() {
    }

    public UsuarioTokenDto(String token) {
        this.token = token;
    }
}