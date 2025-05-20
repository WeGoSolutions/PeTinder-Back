package cruds.Dashboard.service;

import cruds.Dashboard.controller.dto.response.DashboardResponseQuantidadePetsDTO;
import cruds.Dashboard.entity.Dashboard;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.controller.dto.response.PetResponsePendenciasDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.repository.PetRepository;
import cruds.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    @Autowired
    private PetRepository petRepository;

    public List<PetResponseGeralDTO> obterRankingPets(Dashboard dashboard) {
        Integer ongId = dashboard.getOng().getId();
        List<Pet> pets = petRepository.findByOngIdOrderByCurtidasDesc(ongId);

        if (pets.isEmpty()) {
            naoExistePet(ongId);
        }

        return pets.stream()
                .map(PetResponseGeralDTO::toResponse)
                .collect(Collectors.toList());
    }

    public List<PetResponsePendenciasDTO> listarPendenciasPetsDaOng(Integer ongId) {
        List<Pet> pets = petRepository.findByOngId(ongId);
        if (pets.isEmpty()) {
            naoExistePet(ongId);
        }

        List<PetResponsePendenciasDTO> pendencias = new ArrayList<>();

        for (Pet pet : pets) {
            List<String> faltas = new ArrayList<>();
            if (Boolean.FALSE.equals(pet.getIsCastrado())) {
                faltas.add("Castração");
            }
            if (Boolean.FALSE.equals(pet.getIsVermifugo())) {
                faltas.add("Vermífugo");
            }
            if (Boolean.FALSE.equals(pet.getIsVacinado())) {
                faltas.add("Vacina");
            }
            if (!faltas.isEmpty()) {
                pendencias.add(new PetResponsePendenciasDTO(pet.getNome(), faltas));
            }
        }
        return pendencias;
    }

    public DashboardResponseQuantidadePetsDTO contarPetsAdotadosENaoAdotados(Integer ongId) {
        List<Pet> pets = petRepository.findByOngId(ongId);
        int adotados = 0;
        int naoAdotados = 0;

        if (pets.isEmpty()) {
            naoExistePet(ongId);
        }

        for (Pet pet : pets) {
            if (Boolean.TRUE.equals(pet.getIsAdotado())) {
                adotados++;
            } else {
                naoAdotados++;
            }
        }
        return new DashboardResponseQuantidadePetsDTO(adotados, naoAdotados);
    }

    private RuntimeException naoExistePet(Integer ongId) {
        return new NotFoundException("Nenhum pet encontrado para a ONG com ID: " + ongId);
    }
}
