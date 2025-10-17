package servicos.gratitude.be_gratitude_capacita.infraestructure.web.response;

import java.time.LocalDateTime;

public class ParticipanteCursoResponse {
    private Integer idUsuario;
    private String nome;
    private Integer materiaisConcluidos;
    private Integer materiaisTotais;
    private Double avaliacao;
    private LocalDateTime ultimoAcesso;
    // nova informação: nota da última tentativa (acertos e total)
    private Integer ultimaNotaAcertos;
    private Integer ultimaNotaTotal;

    public ParticipanteCursoResponse(Integer idUsuario, String nome, Integer materiaisConcluidos, Integer materiaisTotais, Double avaliacao, LocalDateTime ultimoAcesso, Integer ultimaNotaAcertos, Integer ultimaNotaTotal) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.materiaisConcluidos = materiaisConcluidos;
        this.materiaisTotais = materiaisTotais;
        this.avaliacao = avaliacao;
        this.ultimoAcesso = ultimoAcesso;
        this.ultimaNotaAcertos = ultimaNotaAcertos;
        this.ultimaNotaTotal = ultimaNotaTotal;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public String getNome() { return nome; }
    public Integer getMateriaisConcluidos() { return materiaisConcluidos; }
    public Integer getMateriaisTotais() { return materiaisTotais; }
    public Double getAvaliacao() { return avaliacao; }
    public LocalDateTime getUltimoAcesso() { return ultimoAcesso; }
    public Integer getUltimaNotaAcertos() { return ultimaNotaAcertos; }
    public Integer getUltimaNotaTotal() { return ultimaNotaTotal; }

    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public void setNome(String nome) { this.nome = nome; }
    public void setMateriaisConcluidos(Integer materiaisConcluidos) { this.materiaisConcluidos = materiaisConcluidos; }
    public void setMateriaisTotais(Integer materiaisTotais) { this.materiaisTotais = materiaisTotais; }
    public void setAvaliacao(Double avaliacao) { this.avaliacao = avaliacao; }
    public void setUltimoAcesso(LocalDateTime ultimoAcesso) { this.ultimoAcesso = ultimoAcesso; }
    public void setUltimaNotaAcertos(Integer ultimaNotaAcertos) { this.ultimaNotaAcertos = ultimaNotaAcertos; }
    public void setUltimaNotaTotal(Integer ultimaNotaTotal) { this.ultimaNotaTotal = ultimaNotaTotal; }
}
