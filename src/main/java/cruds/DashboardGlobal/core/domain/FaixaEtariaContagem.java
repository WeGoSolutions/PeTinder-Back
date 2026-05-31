package cruds.DashboardGlobal.core.domain;

public class FaixaEtariaContagem {

    private final String faixaEtaria;
    private final long quantidade;

    public FaixaEtariaContagem(String faixaEtaria, long quantidade) {
        this.faixaEtaria = faixaEtaria;
        this.quantidade = quantidade;
    }

    public String getFaixaEtaria() { return faixaEtaria; }
    public long getQuantidade() { return quantidade; }
}
