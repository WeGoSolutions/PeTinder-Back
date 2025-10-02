package cruds.Pets.V2.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "imagem_pet")
public class ImagemPetEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "link")
    private String caminho;
    
    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "fk_pet")
    private UUID petId;

    public ImagemPetEntity() {}

    public ImagemPetEntity(UUID id, String caminho, String nomeArquivo, UUID petId) {
        this.id = id;
        this.caminho = caminho;
        this.nomeArquivo = nomeArquivo;
        this.petId = petId;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCaminho() { return caminho; }
    public void setCaminho(String caminho) { this.caminho = caminho; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public UUID getPetId() { return petId; }
    public void setPetId(UUID petId) { this.petId = petId; }
}