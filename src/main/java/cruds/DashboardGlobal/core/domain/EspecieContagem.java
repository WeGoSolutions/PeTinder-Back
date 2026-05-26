package cruds.DashboardGlobal.core.domain;

public class EspecieContagem {

    private final String especie;
    private final long quantidade;

    public EspecieContagem(String especie, long quantidade) {
        this.especie = especie;
        this.quantidade = quantidade;
    }

    public String getEspecie() { return especie; }
    public long getQuantidade() { return quantidade; }
}
