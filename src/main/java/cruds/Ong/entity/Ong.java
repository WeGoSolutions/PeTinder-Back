//package cruds.Ong.entity;
//
//import cruds.Imagem.entity.ImagemOng;
//import cruds.Pets.entity.Pet;
//import cruds.Users.entity.Endereco;
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
//@Table(name = "ong")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder(toBuilder = true)
//public class Ong {
//
//    @Id
//    @GeneratedValue(generator = "UUID")
//    private UUID id;
//
//    @Column(name = "cnpj")
//    private String cnpj;
//
//    @Column(name = "cpf")
//    private String cpf;
//
//    @Column(name = "nome")
//    private String nome;
//
//    @Column(name = "razao_social")
//    private String razaoSocial;
//
//    @Column(name = "senha")
//    private String senha;
//
//    @Column(name = "email")
//    private String email;
//
//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "fk_imagem_ong")
//    private ImagemOng imagemOng;
//
//    @OneToMany(mappedBy = "ong")
//    private List<Pet> pets;
//
//    @Column(name = "link")
//    private String link;
//
//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "fk_endereco")
//    private Endereco endereco;
//}
