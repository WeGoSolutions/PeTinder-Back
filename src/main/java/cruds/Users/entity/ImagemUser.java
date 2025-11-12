//package cruds.Users.entity;
//
//import jakarta.persistence.*;
//
//import java.util.UUID;
//
//@Entity
//@Table(name = "imagem_usuario")
//public class ImagemUser {
//
//    @Id
//    @GeneratedValue(generator = "UUID")
//    private UUID id;
//
//    @Lob
//    private byte[] dados;
//
//    private String arquivo;
//
//    public ImagemUser() {}
//
//    public ImagemUser(byte[] dados) {
//        this.dados = dados;
//    }
//
//    public UUID getId() {
//        return id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//    public byte[] getDados() {
//        return dados;
//    }
//
//    public void setDados(byte[] dados) {
//        this.dados = dados;
//    }
//
//    public void setArquivo(String arquivo) {
//        this.arquivo = arquivo;
//    }
//
//    public String getArquivo() {
//        return arquivo;
//    }
//}