package cruds.DashboardGlobal.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import java.time.LocalDateTime;

@Entity
@Subselect("SELECT id, nome_animal, especie, raca, genero, cor_pelagem, codigo_abrigo, abandonado_devolvido, tipo_movimentacao, status_final, foi_adotado, faixa_etaria, dias_no_abrigo, data_entrada, data_movimentacao FROM dados_gold")
@Synchronize("dados_gold")
@Immutable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DadosGoldEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome_animal")
    private String nomeAnimal;

    @Column(name = "especie")
    private String especie;

    @Column(name = "raca")
    private String raca;

    @Column(name = "genero")
    private String genero;

    @Column(name = "cor_pelagem")
    private String corPelagem;

    @Column(name = "codigo_abrigo")
    private String codigoAbrigo;

    @Column(name = "abandonado_devolvido")
    private String abandonadoDevolvido;

    @Column(name = "tipo_movimentacao")
    private String tipoMovimentacao;

    @Column(name = "status_final")
    private String statusFinal;

    @Column(name = "foi_adotado")
    private String foiAdotado;

    @Column(name = "faixa_etaria")
    private String faixaEtaria;

    @Column(name = "dias_no_abrigo")
    private Integer diasNoAbrigo;

    @Column(name = "data_entrada")
    private LocalDateTime dataEntrada;

    @Column(name = "data_movimentacao")
    private LocalDateTime dataMovimentacao;
}
