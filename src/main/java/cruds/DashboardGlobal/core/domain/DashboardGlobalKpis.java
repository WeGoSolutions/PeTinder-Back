package cruds.DashboardGlobal.core.domain;

public class DashboardGlobalKpis {

    private final long totalEntradas;
    private final double taxaAdocao;
    private final double tempoMedioAdocao;
    private final double taxaRetorno;

    public DashboardGlobalKpis(long totalEntradas, double taxaAdocao, double tempoMedioAdocao, double taxaRetorno) {
        this.totalEntradas = totalEntradas;
        this.taxaAdocao = taxaAdocao;
        this.tempoMedioAdocao = tempoMedioAdocao;
        this.taxaRetorno = taxaRetorno;
    }

    public long getTotalEntradas() { return totalEntradas; }
    public double getTaxaAdocao() { return taxaAdocao; }
    public double getTempoMedioAdocao() { return tempoMedioAdocao; }
    public double getTaxaRetorno() { return taxaRetorno; }
}
