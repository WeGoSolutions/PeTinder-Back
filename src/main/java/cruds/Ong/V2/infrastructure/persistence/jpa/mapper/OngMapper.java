package cruds.Ong.V2.infrastructure.persistence.jpa.mapper;

import cruds.Ong.V2.core.domain.EnderecoOng;
import cruds.Ong.V2.core.domain.ImagemOng;
import cruds.Ong.V2.core.domain.Ong;
import cruds.Users.entity.Endereco;

public class OngMapper {

    public static cruds.Ong.entity.Ong toEntity(Ong domain) {
        if (domain == null) return null;

        cruds.Ong.entity.Ong.OngBuilder builder = cruds.Ong.entity.Ong.builder()
                .id(domain.getId())
                .cnpj(domain.getCnpj())
                .cpf(domain.getCpf())
                .nome(domain.getNome())
                .razaoSocial(domain.getRazaoSocial())
                .senha(domain.getSenha())
                .email(domain.getEmail())
                .link(domain.getLink());

        if (domain.getEndereco() != null) {
            builder.endereco(toEnderecoEntity(domain.getEndereco()));
        }

        if (domain.getImagemOng() != null) {
            builder.imagemOng(toImagemOngEntity(domain.getImagemOng()));
        }

        return builder.build();
    }

    public static Ong toDomain(cruds.Ong.entity.Ong entity) {
        if (entity == null) return null;

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
            ong.definirEndereco(toEnderecoDomain(entity.getEndereco()));
        }

        if (entity.getImagemOng() != null) {
            ong.definirImagem(toImagemOngDomain(entity.getImagemOng()));
        }

        return ong;
    }

    private static Endereco toEnderecoEntity(EnderecoOng domain) {
        if (domain == null) return null;

        Integer numero = null;
        if (domain.getNumero() != null && !domain.getNumero().isEmpty()) {
            try {
                numero = Integer.parseInt(domain.getNumero());
            } catch (NumberFormatException e) {
                // Se não for um número válido, mantém como null
                numero = null;
            }
        }

        return Endereco.builder()
                .id(domain.getId())
                .cep(domain.getCep())
                .rua(domain.getRua())
                .numero(numero)
                .cidade(domain.getCidade())
                .uf(domain.getUf())
                .complemento(domain.getComplemento())
                .build();
    }

    private static EnderecoOng toEnderecoDomain(Endereco entity) {
        if (entity == null) return null;

        String numero = entity.getNumero() != null ? entity.getNumero().toString() : null;

        return new EnderecoOng(
            entity.getId(),
            entity.getCep(),
            entity.getRua(),
            numero,
            entity.getCidade(),
            entity.getUf(),
            entity.getComplemento()
        );
    }

    private static cruds.Imagem.entity.ImagemOng toImagemOngEntity(ImagemOng domain) {
        if (domain == null) return null;

        cruds.Imagem.entity.ImagemOng entity = new cruds.Imagem.entity.ImagemOng(domain.getDados());
        entity.setId(domain.getId());
        entity.setArquivo(domain.getArquivo());
        return entity;
    }

    private static ImagemOng toImagemOngDomain(cruds.Imagem.entity.ImagemOng entity) {
        if (entity == null) return null;

        return new ImagemOng(
            entity.getId(),
            entity.getDados(),
            entity.getArquivo()
        );
    }
}
