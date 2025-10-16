package servicos.gratitude.be_gratitude_capacita.core.application.usecase.respostaDoUsuario;

public class NotaResult {
    private final int corretas;
    private final int total;
    private final double percent;

    public NotaResult(int corretas, int total, double percent) {
        this.corretas = corretas;
        this.total = total;
        this.percent = percent;
    }

    public int getCorretas() { return corretas; }
    public int getTotal() { return total; }
    public double getPercent() { return percent; }
}
