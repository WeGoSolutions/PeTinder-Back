package cruds.DashboardGlobal.core.domain;

public class MotivoRetornoContagem {

    private final String motivo;
    private final long quantidade;

    public MotivoRetornoContagem(String motivo, long quantidade) {
        this.motivo = motivo;
        this.quantidade = quantidade;
    }

    public String getMotivo() { return motivo; }
    public long getQuantidade() { return quantidade; }
}
