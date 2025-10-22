package servicos.gratitude.be_gratitude_capacita.infraestructure.messaging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class NotificacaoEmailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("idCurso")
    private Integer idCurso;

    @JsonProperty("tituloCurso")
    private String tituloCurso;

    @JsonProperty("descricaoCurso")
    private String descricaoCurso;

    @JsonProperty("emailAluno")
    private String emailAluno;

    @JsonProperty("nomeAluno")
    private String nomeAluno;

    @JsonProperty("dataEnvio")
    private Long dataEnvio;

    // Constructors
    public NotificacaoEmailDTO() {}

    public NotificacaoEmailDTO(Integer idCurso, String tituloCurso, String descricaoCurso,
                               String emailAluno, String nomeAluno, Long dataEnvio) {
        this.idCurso = idCurso;
        this.tituloCurso = tituloCurso;
        this.descricaoCurso = descricaoCurso;
        this.emailAluno = emailAluno;
        this.nomeAluno = nomeAluno;
        this.dataEnvio = dataEnvio;
    }

    // Getters & Setters
    public Integer getIdCurso() { return idCurso; }
    public void setIdCurso(Integer idCurso) { this.idCurso = idCurso; }

    public String getTituloCurso() { return tituloCurso; }
    public void setTituloCurso(String tituloCurso) { this.tituloCurso = tituloCurso; }

    public String getDescricaoCurso() { return descricaoCurso; }
    public void setDescricaoCurso(String descricaoCurso) { this.descricaoCurso = descricaoCurso; }

    public String getEmailAluno() { return emailAluno; }
    public void setEmailAluno(String emailAluno) { this.emailAluno = emailAluno; }

    public String getNomeAluno() { return nomeAluno; }
    public void setNomeAluno(String nomeAluno) { this.nomeAluno = nomeAluno; }

    public Long getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(Long dataEnvio) { this.dataEnvio = dataEnvio; }
}
