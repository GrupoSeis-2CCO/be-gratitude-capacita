package servicos.gratitude.be_gratitude_capacita.infraestructure.web.response;

public class CursoResponse {
    private Integer idCurso;
    private String tituloCurso;
    private String descricao;
    private String imagem;
    private Boolean ocultado;
    private Integer duracaoEstimada;

    // computed fields
    private Integer totalMateriais;
    private Integer totalAlunos;

    public CursoResponse() {}

    public Integer getIdCurso() { return idCurso; }
    public String getTituloCurso() { return tituloCurso; }
    public String getDescricao() { return descricao; }
    public String getImagem() { return imagem; }
    public Boolean getOcultado() { return ocultado; }
    public Integer getDuracaoEstimada() { return duracaoEstimada; }
    public Integer getTotalMateriais() { return totalMateriais; }
    public Integer getTotalAlunos() { return totalAlunos; }

    public void setIdCurso(Integer idCurso) { this.idCurso = idCurso; }
    public void setTituloCurso(String tituloCurso) { this.tituloCurso = tituloCurso; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setImagem(String imagem) { this.imagem = imagem; }
    public void setOcultado(Boolean ocultado) { this.ocultado = ocultado; }
    public void setDuracaoEstimada(Integer duracaoEstimada) { this.duracaoEstimada = duracaoEstimada; }
    public void setTotalMateriais(Integer totalMateriais) { this.totalMateriais = totalMateriais; }
    public void setTotalAlunos(Integer totalAlunos) { this.totalAlunos = totalAlunos; }
}
