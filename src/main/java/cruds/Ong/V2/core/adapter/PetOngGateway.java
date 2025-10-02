package cruds.Ong.V2.core.adapter;

import java.util.List;
import java.util.UUID;

public interface PetOngGateway {

    List<PetOngInfo> listarPetsPorOng(UUID ongId);

    void removerPetsPorOng(UUID ongId);

    public static class PetOngInfo {
        private UUID id;
        private String nome;
        private String raca;
        private String porte;
        private Integer idade;
        private String sexo;
        private String descricao;
        private Boolean adotado;
        private List<String> status;

        public PetOngInfo(UUID id, String nome, String raca, String porte,
                         Integer idade, String sexo, String descricao,
                         Boolean adotado, List<String> status) {
            this.id = id;
            this.nome = nome;
            this.raca = raca;
            this.porte = porte;
            this.idade = idade;
            this.sexo = sexo;
            this.descricao = descricao;
            this.adotado = adotado;
            this.status = status;
        }

        // Getters
        public UUID getId() { return id; }
        public String getNome() { return nome; }
        public String getRaca() { return raca; }
        public String getPorte() { return porte; }
        public Integer getIdade() { return idade; }
        public String getSexo() { return sexo; }
        public String getDescricao() { return descricao; }
        public Boolean getAdotado() { return adotado; }
        public List<String> getStatus() { return status; }
    }
}

