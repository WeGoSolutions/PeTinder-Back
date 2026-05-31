package cruds.Pets.V2.infrastructure.persistence;

import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetStatusJpaRepository;
import cruds.Pets.V2.core.adapter.ImagemPetGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetGatewayImplTest {

    @Mock
    private PetJpaRepository petJpaRepository;

    @Mock
    private PetStatusJpaRepository petStatusRepository;

    @Mock
    private ImagemPetGateway imagemPetGateway;

    @Mock
    private ImagemPetJpaRepository imagemPetJpaRepository;

    private PetGatewayImpl petGateway;

    @BeforeEach
    void setUp() {
        petGateway = new PetGatewayImpl(
                petJpaRepository,
                petStatusRepository,
                imagemPetGateway,
                imagemPetJpaRepository
        );
    }

    @Test
    @DisplayName("Deve calcular curtidas reais ao buscar pet por id")
    void deveCalcularCurtidasReaisAoBuscarPetPorId() {
        UUID petId = UUID.randomUUID();
        PetEntity petEntity = PetEntity.builder()
                .id(petId)
                .nome("Rex")
                .idade(2.0)
                .porte("Médio")
                .curtidas(1)
                .tags(List.of("Amigável"))
                .descricao("Descrição")
                .isCastrado(true)
                .isVermifugo(true)
                .isVacinado(true)
                .isAdotado(false)
                .sexo("MACHO")
                .ongId(UUID.randomUUID())
                .build();

        when(petJpaRepository.findById(petId)).thenReturn(Optional.of(petEntity));
        when(imagemPetJpaRepository.findKeysByPetId(petId)).thenReturn(Collections.emptyList());
        when(petStatusRepository.countByPetIdAndStatus(petId, PetStatusEnum.LIKED)).thenReturn(5L);

        Pet resultado = petGateway.buscarPorId(petId).orElseThrow();

        assertNotNull(resultado);
        assertEquals(5, resultado.getCurtidas());
    }

    @Test
    @DisplayName("Deve ordenar pets por likes reais ao listar por ONG")
    void deveOrdenarPetsPorLikesReaisAoListarPorOng() {
        UUID ongId = UUID.randomUUID();
        UUID petMaisCurtidoId = UUID.randomUUID();
        UUID petMenosCurtidoId = UUID.randomUUID();

        PetEntity petMaisCurtido = PetEntity.builder()
                .id(petMaisCurtidoId)
                .nome("Mais curtido")
                .idade(3.0)
                .porte("Pequeno")
                .curtidas(1)
                .tags(List.of("Tag"))
                .descricao("Descrição")
                .isCastrado(true)
                .isVermifugo(true)
                .isVacinado(true)
                .isAdotado(false)
                .sexo("FEMEA")
                .ongId(ongId)
                .build();

        PetEntity petMenosCurtido = PetEntity.builder()
                .id(petMenosCurtidoId)
                .nome("Menos curtido")
                .idade(4.0)
                .porte("Grande")
                .curtidas(10)
                .tags(List.of("Tag"))
                .descricao("Descrição")
                .isCastrado(true)
                .isVermifugo(true)
                .isVacinado(true)
                .isAdotado(false)
                .sexo("MACHO")
                .ongId(ongId)
                .build();

        when(petJpaRepository.findByOngIdOrderByCurtidasDesc(ongId)).thenReturn(List.of(petMenosCurtido, petMaisCurtido));
        when(imagemPetJpaRepository.findKeysByPetId(any())).thenReturn(Collections.emptyList());
        when(petStatusRepository.countByPetIdAndStatus(petMaisCurtidoId, PetStatusEnum.LIKED)).thenReturn(7L);
        when(petStatusRepository.countByPetIdAndStatus(petMenosCurtidoId, PetStatusEnum.LIKED)).thenReturn(2L);

        List<Pet> resultado = petGateway.listarPorOng(ongId);

        assertEquals(2, resultado.size());
        assertEquals(petMaisCurtidoId, resultado.get(0).getId());
        assertEquals(7, resultado.get(0).getCurtidas());
        assertEquals(petMenosCurtidoId, resultado.get(1).getId());
        assertEquals(2, resultado.get(1).getCurtidas());
    }
}
