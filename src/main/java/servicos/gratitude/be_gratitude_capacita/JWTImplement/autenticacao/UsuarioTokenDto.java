package servicos.gratitude.be_gratitude_capacita.JWTImplement.autenticacao;

import lombok.Data;

public class UsuarioTokenDto {
    private Integer idUsuario;
    private String nome;
    private String email;
    private String token;
    private Integer idCargo;

    public UsuarioTokenDto() {}

    public UsuarioTokenDto(String token) {
        this.token = token;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Integer getIdCargo() {
        return idCargo;
    }
    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }
}