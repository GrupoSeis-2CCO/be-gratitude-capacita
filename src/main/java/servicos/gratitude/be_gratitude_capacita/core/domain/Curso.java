package servicos.gratitude.be_gratitude_capacita.core.domain;

public class Curso {
    private Integer idCurso;
    private String tituloCurso;
    private String descricao;
    private String imagem;
    private Boolean ocultado;
    private Integer duracaoEstimada;

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public String getTituloCurso() {
        return tituloCurso;
    }

    public void setTituloCurso(String tituloCurso) {
        this.tituloCurso = tituloCurso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Boolean getOcultado() {
        return ocultado;
    }

    public void setOcultado(Boolean ocultado) {
        this.ocultado = ocultado;
    }

    public Integer getDuracaoEstimada() {
        return duracaoEstimada;
    }

    public void setDuracaoEstimada(Integer duracaoEstimada) {
        this.duracaoEstimada = duracaoEstimada;
    }
}