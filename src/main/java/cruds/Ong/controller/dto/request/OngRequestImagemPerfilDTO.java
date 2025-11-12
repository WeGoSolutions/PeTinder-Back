//package cruds.Ong.controller.dto.request;
//import jakarta.validation.constraints.NotBlank;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import java.util.Base64;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class OngRequestImagemPerfilDTO {
//    @NotBlank
//    private String imagemOng;
//
//    public byte[] getImagemDecodificada() {
//        String base64Data = imagemOng;
//        if (base64Data.startsWith("data:")) {
//            int commaIndex = base64Data.indexOf(",");
//            if (commaIndex != -1) {
//                base64Data = base64Data.substring(commaIndex + 1);
//            }
//        }
//        return Base64.getDecoder().decode(base64Data);
//    }
//}
