package servicos.gratitude.be_gratitude_capacita.core.domain;

import java.time.LocalDateTime;

public class Usuario {
    private Integer idUsuario;
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private LocalDateTime dataEntrada;
    private LocalDateTime ultimoAcesso;
    private Cargo fkCargo;

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public Cargo getFkCargo() {
        return fkCargo;
    }

    public void setFkCargo(Cargo fkCargo) {
        this.fkCargo = fkCargo;
    }
}
