//package cruds.Pets.entity;
//
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import cruds.Imagem.entity.Imagem;
//import cruds.Ong.entity.Ong;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//import java.util.UUID;
//
//@Entity
//@Table(name = "pet")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder(toBuilder = true)
//public class Pet {
//
//    @Id
//    @GeneratedValue(generator = "UUID")
//    private UUID id;
//
//    private String nome;
//
//    private Double idade;
//
//    private String porte;
//
//    private Integer curtidas;
//
//    @ElementCollection
//    @CollectionTable(name = "pet_tags", joinColumns = @JoinColumn(name = "pet_id"))
//    @Column(name = "tag")
//    private List<String> tags;
//
//    private String descricao;
//
//    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private List<Imagem> imagens;
//
//    @Builder.Default    private Boolean isCastrado = false;
//
//    @Builder.Default    private Boolean isVermifugo = false;
//
//    @Builder.Default    private Boolean isVacinado = false;
//
//    @Builder.Default    private Boolean isAdopted = false;
//
//    private String sexo;
//
//    @ManyToOne
//    @JoinColumn(name = "fk_ong", nullable = false)
//    private Ong ong;
//}
