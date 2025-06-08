package cruds.Pets.service;

import cruds.Imagem.repository.ImagemRepository;
import cruds.Ong.entity.Ong;
import cruds.Ong.repository.OngRepository;
import cruds.Pets.controller.dto.request.PetStatusRequestDTO;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.controller.dto.response.PetResponsePendingOngDTO;
import cruds.Pets.controller.dto.response.PetResponseUserPendenteDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import cruds.Pets.repository.PetRepository;
import cruds.Pets.repository.PetStatusRepository;
import cruds.Users.entity.User;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.NotFoundException;
import cruds.common.strategy.ImageStorageStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PetStatusServiceTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private ImagemRepository imagemRepository;
    @Mock
    private ImageStorageStrategy imageStorageStrategy;
    @InjectMocks
    private PetService petService;
    @Mock
    private PetStatusRepository petStatusRepository;
    @Mock
    private OngRepository ongRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private PetStatusService petStatusService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrUpdatePetStatus_newStatus_success() {
        PetStatusRequestDTO dto = new PetStatusRequestDTO();
        dto.setPetId(1);
        dto.setUserId(2);
        dto.setStatus(PetStatusEnum.LIKED);

        when(petStatusRepository.findByPet_IdAndUser_Id(1, 2)).thenReturn(Optional.empty());

        Pet pet = new Pet(); pet.setId(1);
        User user = new User(); user.setId(2L);

        when(petRepository.findById(1)).thenReturn(Optional.of(pet));
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(petStatusRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PetStatus result = petStatusService.createOrUpdatePetStatus(dto);

        assertEquals(PetStatusEnum.LIKED, result.getStatus());
        assertEquals(pet, result.getPet());
        assertEquals(user, result.getUser());
    }

    @Test
    void testCreateOrUpdatePetStatus_existingStatus_success() {
        PetStatusRequestDTO dto = new PetStatusRequestDTO();
        dto.setPetId(1);
        dto.setUserId(2);
        dto.setStatus(PetStatusEnum.PENDING);

        PetStatus existing = new PetStatus();
        existing.setId(10);
        existing.setStatus(PetStatusEnum.LIKED);

        when(petStatusRepository.findByPet_IdAndUser_Id(1, 2)).thenReturn(Optional.of(existing));
        when(petStatusRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PetStatus result = petStatusService.createOrUpdatePetStatus(dto);

        assertEquals(PetStatusEnum.PENDING, result.getStatus());
        assertNotNull(result.getAlteradoParaPending());
    }

    @Test
    void testCreateOrUpdatePetStatus_petNotFound() {
        PetStatusRequestDTO dto = new PetStatusRequestDTO();
        dto.setPetId(1);
        dto.setUserId(2);
        dto.setStatus(PetStatusEnum.LIKED);

        when(petStatusRepository.findByPet_IdAndUser_Id(1, 2)).thenReturn(Optional.empty());
        when(petRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> petStatusService.createOrUpdatePetStatus(dto));
    }

    @Test
    void testGetPetStatusList_returnsStatuses() {
        PetStatus s1 = new PetStatus(); s1.setStatus(PetStatusEnum.LIKED);
        PetStatus s2 = new PetStatus(); s2.setStatus(PetStatusEnum.PENDING);

        when(petStatusRepository.findByPet_Id(1)).thenReturn(List.of(s1, s2));

        List<PetStatusEnum> result = petStatusService.getPetStatusList(1);

        assertEquals(List.of(PetStatusEnum.LIKED, PetStatusEnum.PENDING), result);
    }

    @Test
    void testGetPetStatusList_empty_returnsAdopted() {
        when(petStatusRepository.findByPet_Id(1)).thenReturn(List.of());

        List<PetStatusEnum> result = petStatusService.getPetStatusList(1);

        assertEquals(List.of(PetStatusEnum.ADOPTED), result);
    }

    @Test
    void testListPendingPetsWithOngForUser_success() {
        int userId = 5;
        when(userRepository.existsById(userId)).thenReturn(true);

        Pet pet = new Pet();
        pet.setId(10);
        pet.setNome("Rex");
        pet.setIdade(2.0);
        pet.setPorte("Médio");
        pet.setDescricao("Amigável");
        pet.setIsCastrado(true);
        pet.setIsVermifugo(true);
        pet.setIsVacinado(true);
        pet.setImagens(List.of());
        pet.setSexo("M");
        Ong ong = new Ong(); ong.setId(100);
        pet.setOng(ong);

        PetStatus status = new PetStatus();
        status.setPet(pet);

        when(petStatusRepository.findByUserIdAndStatus(userId, PetStatusEnum.PENDING)).thenReturn(List.of(status));

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI("/status/pending/ong/5");

        List<PetResponsePendingOngDTO> result = petStatusService.listPendingPetsWithOngForUser(userId, req);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
        assertEquals(pet.getId(), result.get(0).getPetId());
    }

    @Test
    void testListPendingPetsWithOngForUser_userNotFound() {
        int userId = 5;
        when(userRepository.existsById(userId)).thenReturn(false);

        MockHttpServletRequest req = new MockHttpServletRequest();

        assertThrows(NotFoundException.class, () -> petStatusService.listPendingPetsWithOngForUser(userId, req));
    }

    @Test
    void testListAvailablePetsForUser_success() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        int userId = 1;
        Pet pet = new Pet();
        pet.setId(10);
        pet.setIsAdopted(false);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(petStatusRepository.findPetsNotInteractedByUser(userId)).thenReturn(List.of(pet));

        List<PetResponseGeralDTO> result = petStatusService.listAvailablePetsForUser(userId);

        assertEquals(1, result.size());
        assertEquals(pet.getId(), result.get(0).getId());

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void testListAvailablePetsForUser_userNotFound() {
        int userId = 1;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> petStatusService.listAvailablePetsForUser(userId));
    }

    @Test
    void testGetPetsByUserAndStatus_success() {
        int userId = 2;
        PetStatusEnum status = PetStatusEnum.LIKED;
        PetStatus petStatus = new PetStatus();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(petStatusRepository.findByUserIdAndStatus(userId, status)).thenReturn(List.of(petStatus));

        List<PetStatus> result = petStatusService.getPetsByUserAndStatus(userId, status);

        assertEquals(1, result.size());
        assertEquals(petStatus, result.get(0));
    }

    @Test
    void testGetPetsByUserAndStatus_userNotFound() {
        int userId = 2;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> petStatusService.getPetsByUserAndStatus(userId, PetStatusEnum.LIKED));
    }

    @Test
    void testDeletePetStatus_success() {
        int petId = 10;
        int userId = 20;
        PetStatus status = new PetStatus();

        when(petStatusRepository.findByPetIdAndUserId(petId, userId)).thenReturn(Optional.of(status));

        assertDoesNotThrow(() -> petStatusService.deletePetStatus(petId, userId));
        verify(petStatusRepository).delete(status);
    }

    @Test
    void testDeletePetStatus_notFound() {
        int petId = 10;
        int userId = 20;

        when(petStatusRepository.findByPetIdAndUserId(petId, userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> petStatusService.deletePetStatus(petId, userId));
    }

    @Test
    void testListDefaultPets_success() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        int userId = 1;
        Pet pet = new Pet();
        pet.setId(10);
        pet.setIsAdopted(false);

        PetResponseGeralDTO dto = new PetResponseGeralDTO();
        dto.setId(10);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(petStatusRepository.findPetsNotInteractedByUser(userId)).thenReturn(List.of(pet));

        List<PetResponseGeralDTO> result = petStatusService.listDefaultPets(userId);

        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getId());

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void testListDefaultPets_userNotFound() {
        int userId = 1;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> petStatusService.listDefaultPets(userId));
    }

    @Test
    void testListDefaultPets_empty() {
        int userId = 1;
        when(userRepository.existsById(userId)).thenReturn(true);
        when(petStatusRepository.findPetsNotInteractedByUser(userId)).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> petStatusService.listDefaultPets(userId));
    }

    @Test
    void testIncrementarCurtidasPet_success() {
        int petId = 5;
        Pet pet = new Pet();
        pet.setId(petId);
        pet.setCurtidas(2);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(petRepository.save(any())).thenReturn(pet);

        petStatusService.incrementarCurtidasPet(petId);

        assertEquals(3, pet.getCurtidas());
        verify(petRepository).save(pet);
    }

    @Test
    void testIncrementarCurtidasPet_petNotFound() {
        int petId = 5;
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> petStatusService.incrementarCurtidasPet(petId));
    }

    @Test
    void testDecrementarCurtidasPet_success() {
        int petId = 6;
        Pet pet = new Pet();
        pet.setId(petId);
        pet.setCurtidas(4);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(petRepository.save(any())).thenReturn(pet);

        petStatusService.decrementarCurtidasPet(petId);

        assertEquals(3, pet.getCurtidas());
        verify(petRepository).save(pet);
    }

    @Test
    void testDecrementarCurtidasPet_petNotFound() {
        int petId = 6;
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> petStatusService.decrementarCurtidasPet(petId));
    }

    @Test
    void testPublicarAdocao_success() {
        int petId = 7;
        Pet pet = new Pet();
        pet.setId(petId);
        pet.setIsAdopted(false);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(petRepository.save(any())).thenReturn(pet);

        petStatusService.publicarAdocao(petId);

        assertTrue(pet.getIsAdopted());
        verify(petRepository).save(pet);
    }

    @Test
    void testPublicarAdocao_petNotFound() {
        int petId = 7;
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> petStatusService.publicarAdocao(petId));
    }

    @Test
    void testAdoptPet_statusExists_success() {
        int petId = 1, userId = 2;
        Pet pet = new Pet(); pet.setId(petId); pet.setIsAdopted(false);
        User user = new User(); user.setId((long) userId);
        PetStatus status = new PetStatus(); status.setPet(pet); status.setUser(user);

        when(petStatusRepository.findByPet_IdAndUser_Id(petId, userId)).thenReturn(Optional.of(status));
        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(petRepository.save(any())).thenReturn(pet);
        when(petStatusRepository.save(any())).thenReturn(status);

        PetStatus result = petStatusService.adoptPet(petId, userId);

        assertEquals(PetStatusEnum.ADOPTED, result.getStatus());
        assertTrue(pet.getIsAdopted());
        verify(petRepository).save(pet);
        verify(petStatusRepository).deleteByPetIdAndUserIdNot(petId, userId);
    }

    @Test
    void testAdoptPet_statusNotExists_success() {
        int petId = 1, userId = 2;
        Pet pet = new Pet(); pet.setId(petId); pet.setIsAdopted(false);
        User user = new User(); user.setId((long) userId);

        when(petStatusRepository.findByPet_IdAndUser_Id(petId, userId)).thenReturn(Optional.empty());
        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(petRepository.save(any())).thenReturn(pet);
        when(petStatusRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PetStatus result = petStatusService.adoptPet(petId, userId);

        assertEquals(PetStatusEnum.ADOPTED, result.getStatus());
        assertEquals(pet, result.getPet());
        assertEquals(user, result.getUser());
        assertTrue(pet.getIsAdopted());
        verify(petRepository).save(pet);
        verify(petStatusRepository).deleteByPetIdAndUserIdNot(petId, userId);
    }

    @Test
    void testGetAllPetsWithUserStatus_success() {
        Pet pet = new Pet(); pet.setId(1); pet.setNome("Rex");
        User user = new User(); user.setId(2L);
        PetStatus status = new PetStatus(); status.setStatus(PetStatusEnum.LIKED);

        when(petRepository.findAll()).thenReturn(List.of(pet));
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(petStatusRepository.findByPet_IdAndUser_Id(1, 2)).thenReturn(Optional.of(status));

        List<Object> result = petStatusService.getAllPetsWithUserStatus();

        assertEquals(1, result.size());
        var map = (java.util.Map<?, ?>) result.get(0);
        assertEquals(1, map.get("id_pet"));
        assertEquals(2L, map.get("id_user"));
        assertEquals("Rex", map.get("nome_pet"));
        assertEquals("LIKED", map.get("status"));
    }

    @Test
    void testRemoverOutrosStatus_callsRepository() {
        int petId = 1, userId = 2;
        petStatusService.removerOutrosStatus(petId, userId);
        verify(petStatusRepository).deleteByPetIdAndUserIdNot(petId, userId);
    }

    @Test
    void testListPendingUsersByPetId_success() {
        int petId = 1;
        Pet pet = new Pet(); pet.setId(petId);
        User user = new User(); user.setId(2L);
        PetStatus status = new PetStatus(); status.setUser(user);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(petStatusRepository.findByPet_IdAndStatus(petId, PetStatusEnum.PENDING)).thenReturn(List.of(status));

        List<PetResponseUserPendenteDTO> result = petStatusService.listPendingUsersByPetId(petId);

        assertEquals(1, result.size());
        assertEquals(user.getId().intValue(), result.get(0).getIdUser());
    }

    @Test
    void testListPendingUsersByPetId_petNotFound() {
        int petId = 1;
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> petStatusService.listPendingUsersByPetId(petId));
    }

    @Test
    void testListPendingUsersByPetId_noPendingUsers() {
        int petId = 1;
        Pet pet = new Pet(); pet.setId(petId);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(petStatusRepository.findByPet_IdAndStatus(petId, PetStatusEnum.PENDING)).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> petStatusService.listPendingUsersByPetId(petId));
    }

}