package cruds.Pets.controller.dto.response;

import cruds.Pets.entity.PetStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class PetStatusResponseDTO {

    private Integer petId;
    private String petNome;
    private Integer usuarioId;
    private String status;
    private String imageUrl;

    public PetStatusResponseDTO(PetStatus petStatus) {
        this.petId = petStatus.getPet().getId();
        this.petNome = petStatus.getPet().getNome();
        this.usuarioId = Math.toIntExact(petStatus.getUser().getId());
        this.status = petStatus.getStatus().name();
        var pet = petStatus.getPet();
        if (pet.getImagens() != null && !pet.getImagens().isEmpty()) {
            String base = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .build()
                    .toUriString();
            this.imageUrl = base + "/pets/" + pet.getId() + "/imagem/0";
        }
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public String getPetNome() {
        return petNome;
    }

    public void setPetNome(String petNome) {
        this.petNome = petNome;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}