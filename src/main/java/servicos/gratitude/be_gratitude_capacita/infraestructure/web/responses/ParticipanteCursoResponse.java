package servicos.gratitude.be_gratitude_capacita.infraestructure.web.responses;

import java.time.LocalDateTime;

public class ParticipanteCursoResponse {

    private Long id;
    private String nome;
    private String materiais;
    private String avaliacao;
    private LocalDateTime ultimoAcesso;

    public ParticipanteCursoResponse(Long id, String nome, String materiais, String avaliacao, LocalDateTime ultimoAcesso) {
        this.id = id;
        this.nome = nome;
        this.materiais = materiais;
        this.avaliacao = avaliacao;
        this.ultimoAcesso = ultimoAcesso;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMateriais() {
        return materiais;
    }

    public void setMateriais(String materiais) {
        this.materiais = materiais;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }
}
