package cruds.DashboardGlobal.core.domain;

public class StatusContagem {

    private final String status;
    private final long quantidade;

    public StatusContagem(String status, long quantidade) {
        this.status = status;
        this.quantidade = quantidade;
    }

    public String getStatus() { return status; }
    public long getQuantidade() { return quantidade; }
}
