package cruds.Forms.service;

import cruds.Forms.controller.dto.request.FormRequestCriarDTO;
import cruds.Forms.controller.dto.response.FormResponsePreenchimentoUserDTO;
import cruds.Forms.entity.Forms;
import cruds.Forms.exceptions.FormUpdateNotAllowedException;
import cruds.Forms.repository.FormsRepository;
import cruds.Pets.entity.Imagem;
import cruds.Pets.entity.Pet;
import cruds.Users.entity.User;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FormsService {

    private final UserService userService;
    private final FormsRepository formsRepository;

    @Autowired
    public FormsService(UserService userService, FormsRepository formsRepository) {
        this.userService = userService;
        this.formsRepository = formsRepository;
    }

    // Obtém os dados do formulário preenchidos com base no ID do usuário.
    public FormResponsePreenchimentoUserDTO getDadosFormulario(Integer id) {
        UserResponseCadastroDTO userData = userService.getUserById(id);
        FormResponsePreenchimentoUserDTO formDTO = new FormResponsePreenchimentoUserDTO();
        formDTO.setNome(userData.getNome());
        formDTO.setCpf(userData.getCpf());
        formDTO.setEmail(userData.getEmail());
        formDTO.setDataNasc(userData.getDataNasc());
        formDTO.setCep(userData.getCep());
        formDTO.setRua(userData.getRua());
        formDTO.setNumero(userData.getNumero());
        formDTO.setComplemento(userData.getComplemento());
        formDTO.setCidade(userData.getCidade());
        formDTO.setUf(userData.getUf());
        return formDTO;
    }

    // Cria um novo formulário a partir do DTO.
    public Forms createForm(FormRequestCriarDTO formDTO) {
        Forms form = mapToEntity(formDTO);
        form.setFinalizado(isFormComplete(form));
        return formsRepository.save(form);
    }

    // Atualiza um formulário existente utilizando o DTO.
    public Forms updateForm(Integer id, FormRequestCriarDTO formDTO) {
        Forms existingForm = formsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formulário com id " + id + " não encontrado"));
        if (existingForm.isFinalizado()) {
            throw new FormUpdateNotAllowedException("Formulário finalizado não pode ser atualizado.");
        }
        updateEntity(existingForm, formDTO);
        existingForm.setFinalizado(isFormComplete(existingForm));
        return formsRepository.save(existingForm);
    }

    // Mapeia o DTO para uma nova entidade Forms.
    private Forms mapToEntity(FormRequestCriarDTO dto) {
        Forms form = new Forms();
        // Mapeamento de associações.
        Pet pet = new Pet();
        pet.setId(dto.getPetId());
        form.setPet(pet);
        User user = new User();
        user.setId(dto.getUserId());
        form.setUser(user);
        // Mapeamento dos demais campos.
        form.setNome(dto.getNome());
        form.setCpf(dto.getCpf());
        form.setEmail(dto.getEmail());
        form.setDataNasc(dto.getDataNasc());
        form.setTelefone(dto.getTelefone());
        form.setCep(dto.getCep());
        form.setComplemento(dto.getComplemento());
        form.setRua(dto.getRua());
        form.setNumero(dto.getNumero());
        form.setCidade(dto.getCidade());
        form.setUf(dto.getUf());
        form.setTipoMoradia(dto.getTipoMoradia());
        form.setAluguePodeAnimal(dto.getAluguePodeAnimal());
        form.setInfosCasa(dto.getInfosCasa());
        form.setPossuiPet(dto.getPossuiPet());
        form.setCastradoOrVacinado(dto.getCastradoOrVacinado());
        form.setInfosPet(dto.getInfosPet());
        if (dto.getImagens() != null) {
            List<Imagem> imagens = new ArrayList<>();
            for (byte[] imgData : dto.getImagens()) {
                Imagem img = new Imagem();
                img.setDados(imgData);
                imagens.add(img);
            }
            form.setImagens(imagens);
        }
        return form;
    }

    // Atualiza a entidade Forms com os dados do DTO.
    private void updateEntity(Forms form, FormRequestCriarDTO dto) {
        form.setNome(dto.getNome());
        form.setCpf(dto.getCpf());
        form.setEmail(dto.getEmail());
        form.setDataNasc(dto.getDataNasc());
        form.setTelefone(dto.getTelefone());
        form.setCep(dto.getCep());
        form.setComplemento(dto.getComplemento());
        form.setRua(dto.getRua());
        form.setNumero(dto.getNumero());
        form.setCidade(dto.getCidade());
        form.setUf(dto.getUf());
        form.setTipoMoradia(dto.getTipoMoradia());
        form.setAluguePodeAnimal(dto.getAluguePodeAnimal());
        form.setInfosCasa(dto.getInfosCasa());
        form.setPossuiPet(dto.getPossuiPet());
        form.setCastradoOrVacinado(dto.getCastradoOrVacinado());
        form.setInfosPet(dto.getInfosPet());
        if (dto.getImagens() != null) {
            List<Imagem> imagens = new ArrayList<>();
            for (byte[] imgData : dto.getImagens()) {
                Imagem img = new Imagem();
                img.setDados(imgData);
                imagens.add(img);
            }
            form.setImagens(imagens);
        }
    }

    // Verifica se o formulário está completo.
    private boolean isFormComplete(Forms form) {
        return !isBlank(form.getNome()) &&
                !isBlank(form.getCpf()) &&
                !isBlank(form.getEmail()) &&
                form.getDataNasc() != null &&
                !isBlank(form.getTelefone()) &&
                !isBlank(form.getCep()) &&
                !isBlank(form.getRua()) &&
                form.getNumero() != null &&
                !isBlank(form.getCidade()) &&
                !isBlank(form.getUf()) &&
                !isBlank(form.getTipoMoradia()) &&
                !isBlank(form.getPossuiPet()) &&
                form.getImagens() != null &&
                form.getImagens().size() >= 5;
    }

    // Método auxiliar para verificar se uma string está vazia.
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}