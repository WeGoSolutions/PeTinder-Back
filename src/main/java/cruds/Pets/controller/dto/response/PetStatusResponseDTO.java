package cruds.Pets.controller.dto.response;
import cruds.Pets.entity.PetStatus;

public class PetStatusResponseDTO {

    private Integer petId;
    private String petNome;
    private Integer usuarioId;
    private String status;

    public PetStatusResponseDTO(PetStatus petStatus) {
        this.petId    = petStatus.getPet().getId();
        this.petNome  = petStatus.getPet().getNome();
        this.usuarioId = Math.toIntExact(petStatus.getUser().getId());
        this.status   = petStatus.getStatus().name();
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
}