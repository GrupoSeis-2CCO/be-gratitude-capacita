package servicos.gratitude.be_gratitude_capacita.core.domain;

public class Curso {
    private Long id;
    private String titulo;
    private String descricao;
    private String imagem;
    private Boolean ocultado;
    private Integer duracaoEstimada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public Long getIdCurso() {
        return this.id;
    }

    public void setIdCurso(Long idCurso) {
        this.id = idCurso;
    }

    public String getTituloCurso() {
        return this.titulo;
    }

    public void setTituloCurso(String tituloCurso) {
        this.titulo = tituloCurso;
    }
}