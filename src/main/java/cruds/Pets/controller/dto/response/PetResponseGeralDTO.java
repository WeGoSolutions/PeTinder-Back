//package cruds.Pets.controller.dto.response;
//
//import cruds.Pets.entity.Pet;
//import cruds.Pets.enums.PetStatusEnum;
//import cruds.Imagem.entity.Imagem;
//import cruds.Users.controller.dto.response.EnderecoResponseDTO;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class PetResponseGeralDTO {
//    private UUID id;
//    private String nome;
//    private Double idade;
//    private String porte;
//    private Integer curtidas;
//    private String descricao;
//    private List<String> tags;
//    private Boolean isCastrado;
//    private Boolean isVermifugo;
//    private Boolean isVacinado;
//    private List<String> imagens;
//    private String sexo;
//    private UUID ongId;
//    private String nomeOng;
//    private String linkOng;
//    private EnderecoResponseDTO endereco;
//    private PetStatusEnum status;
//
//    public PetResponseGeralDTO(Pet pet) {
//        this.id = pet.getId();
//        this.nome = pet.getNome();
//        this.idade = pet.getIdade();
//        this.porte = pet.getPorte();
//        this.curtidas = pet.getCurtidas();
//        this.descricao = pet.getDescricao();
//        this.tags = pet.getTags();
//        this.isCastrado = pet.getIsCastrado();
//        this.isVermifugo = pet.getIsVermifugo();
//        this.isVacinado = pet.getIsVacinado();
//        this.sexo = pet.getSexo();
//        this.ongId = pet.getOng() != null ? pet.getOng().getId() : null;
//
//        if (pet.getImagens() != null) {
//            this.imagens = pet.getImagens().stream()
//                    .map(Imagem::getCaminho)
//                    .collect(Collectors.toList());
//        }
//    }
//
//    public static PetResponseGeralDTO toResponse(Pet pet) {
//        String baseUri = ServletUriComponentsBuilder
//                .fromCurrentContextPath()
//                .build()
//                .toUriString();
//
//        List<String> imagemUrls = pet.getImagens() == null
//                ? null
//                : IntStream.range(0, pet.getImagens().size())
//                .mapToObj(i -> baseUri + "/api/pets/" + pet.getId() + "/imagens/" + i)
//                .collect(Collectors.toList());
//
//        return PetResponseGeralDTO.builder()
//                .id(pet.getId())
//                .nome(pet.getNome())
//                .idade(pet.getIdade())
//                .porte(pet.getPorte())
//                .curtidas(pet.getCurtidas())
//                .descricao(pet.getDescricao())
//                .tags(pet.getTags())
//                .isCastrado(pet.getIsCastrado())
//                .isVermifugo(pet.getIsVermifugo())
//                .isVacinado(pet.getIsVacinado())
//                .imagens(imagemUrls)
//                .sexo(pet.getSexo())
//                .ongId(pet.getOng() != null ? pet.getOng().getId() : null)
//                .nomeOng(pet.getOng() != null ? pet.getOng().getNome() : null)
//                .linkOng(pet.getOng() != null ? pet.getOng().getLink() : null)
//                .endereco(pet.getOng() != null && pet.getOng().getEndereco() != null
//                        ? EnderecoResponseDTO.toResponse(pet.getOng().getEndereco())
//                        : null)
//                .build();
//    }
//
//    public static PetResponseGeralDTO toResponse(Pet pet, PetStatusEnum status) {
//        PetResponseGeralDTO dto = toResponse(pet);
//        dto.setStatus(status);
//        return dto;
//    }
//}