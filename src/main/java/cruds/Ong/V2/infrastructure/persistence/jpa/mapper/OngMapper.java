package cruds.Ong.V2.infrastructure.persistence.jpa.mapper;

import cruds.Ong.V2.core.domain.Endereco;
import cruds.Ong.V2.core.domain.ImagemOng;
import cruds.Ong.V2.core.domain.Ong;
import cruds.Ong.V2.infrastructure.persistence.jpa.EnderecoEntity;
import cruds.Ong.V2.infrastructure.persistence.jpa.ImagemOngEntity;
import cruds.Ong.V2.infrastructure.persistence.jpa.OngEntity;

public class OngMapper {

    public static OngEntity toEntity(Ong ong) {
        if (ong == null) {
            return null;
        }

        OngEntity entity = OngEntity.builder()
                .id(ong.getId())
                .cnpj(ong.getCnpj())
                .cpf(ong.getCpf())
                .nome(ong.getNome())
                .razaoSocial(ong.getRazaoSocial())
                .senha(ong.getSenha())
                .email(ong.getEmail())
                .link(ong.getLink())
                .endereco(toEnderecoEntity(ong.getEndereco()))
                .imagemOng(toImagemOngEntity(ong.getImagemOng()))
                .build();

        return entity;
    }

    public static Ong toDomain(OngEntity entity) {
        if (entity == null) {
            return null;
        }

        Ong ong = new Ong(
                entity.getId(),
                entity.getCnpj(),
                entity.getCpf(),
                entity.getNome(),
                entity.getRazaoSocial(),
                entity.getSenha(),
                entity.getEmail(),
                entity.getLink()
        );

        if (entity.getEndereco() != null) {
            ong.setEndereco(toEnderecoDomain(entity.getEndereco()));
        }

        if (entity.getImagemOng() != null) {
            ong.setImagemOng(toImagemOngDomain(entity.getImagemOng()));
        }

        return ong;
    }

    private static EnderecoEntity toEnderecoEntity(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        return EnderecoEntity.builder()
                .id(endereco.getId())
                .cep(endereco.getCep())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .uf(endereco.getUf())
                .complemento(endereco.getComplemento())
                .build();
    }

    private static Endereco toEnderecoDomain(EnderecoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Endereco(
                entity.getId(),
                entity.getCep(),
                entity.getRua(),
                entity.getNumero(),
                entity.getCidade(),
                entity.getUf(),
                entity.getComplemento()
        );
    }

    private static ImagemOngEntity toImagemOngEntity(ImagemOng imagem) {
        if (imagem == null) {
            return null;
        }

        return ImagemOngEntity.builder()
                .id(imagem.getId())
                .dados(imagem.getDados())
                .arquivo(imagem.getNomeArquivo())
                .build();
    }

    private static ImagemOng toImagemOngDomain(ImagemOngEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ImagemOng(
                entity.getId(),
                entity.getDados(),
                entity.getArquivo()
        );
    }
}
