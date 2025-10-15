package servicos.gratitude.be_gratitude_capacita.infraestructure.web.response;

public class MaterialResponse {
    private Integer id;
    private String tipo; // video | apostila | avaliacao
    private String titulo;
    private String descricao;
    private String url; // quando aplicável

    public MaterialResponse() {}

    public MaterialResponse(Integer id, String tipo, String titulo, String descricao, String url) {
        this.id = id;
        this.tipo = tipo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.url = url;
    }

    public Integer getId() { return id; }
    public String getTipo() { return tipo; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getUrl() { return url; }

    public void setId(Integer id) { this.id = id; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setUrl(String url) { this.url = url; }
}
