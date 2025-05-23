package cruds.Forms.service;

import cruds.Forms.controller.dto.request.FormRequestCriarDTO;
import cruds.Forms.controller.dto.response.FormResponsePreenchimentoUserDTO;
import cruds.Forms.entity.Forms;
import cruds.Forms.repository.FormsRepository;
import cruds.Imagem.entity.Imagem;
import cruds.Imagem.entity.ImagemForms;
import cruds.Pets.entity.Pet;
import cruds.Users.entity.User;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.service.UserService;
import cruds.common.exception.BadRequestException;
import cruds.common.exception.NotAllowedException;
import cruds.common.exception.NotFoundException;
import cruds.common.util.ImageValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FormsService {

    private static final String UPLOAD_DIR = "C:/Users/Cauan/Desktop/S3 local/imagens/";

    private final UserService userService;
    private final FormsRepository formsRepository;

    @Autowired
    public FormsService(UserService userService, FormsRepository formsRepository) {
        this.userService = userService;
        this.formsRepository = formsRepository;
    }

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

    public Forms createForm(FormRequestCriarDTO formDTO) {
        List<byte[]> imagensBytes = new ArrayList<>();
        List<String> nomesArquivos = new ArrayList<>();

        for (MultipartFile file : formDTO.getImagens()) {
            try {
                imagensBytes.add(file.getBytes());
                nomesArquivos.add(file.getOriginalFilename());
            } catch (IOException e) {
                throw new BadRequestException("Erro ao processar a imagem: " + e.getMessage());
            }
        }

        try {
            ImageValidationUtil.validateFormImages(imagensBytes, nomesArquivos);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao validar as imagens do formulário: " + e.getMessage());
        }

        Forms form = mapToEntity(formDTO, imagensBytes);
        form.setFinalizado(isFormComplete(form));
        return formsRepository.save(form);
    }

    public Forms updateForm(Integer id, FormRequestCriarDTO formDTO) {
        Forms existingForm = formsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Formulário com id " + id + " não encontrado"));

        if (existingForm.isFinalizado()) {
            throw new NotAllowedException("Formulário finalizado não pode ser atualizado.");
        }

        List<byte[]> imagensBytes = new ArrayList<>();
        List<String> nomesArquivos = new ArrayList<>();

        for (MultipartFile file : formDTO.getImagens()) {
            try {
                imagensBytes.add(file.getBytes());
                nomesArquivos.add(file.getOriginalFilename());
            } catch (IOException e) {
                throw new BadRequestException("Erro ao processar a imagem: " + e.getMessage());
            }
        }

        try {
            ImageValidationUtil.validateFormImages(imagensBytes, nomesArquivos);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao validar as imagens do formulário: " + e.getMessage());
        }

        updateEntity(existingForm, formDTO, imagensBytes);
        existingForm.setFinalizado(isFormComplete(existingForm));
        return formsRepository.save(existingForm);
    }

    private Forms mapToEntity(FormRequestCriarDTO dto, List<byte[]> imagensBytes) {
        Forms form = new Forms();
        Pet pet = new Pet();
        pet.setId(dto.getPetId());
        form.setPet(pet);
        User user = new User();
        user.setId(Long.valueOf(dto.getUserId()));
        form.setUser(user);
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

        if (imagensBytes != null) {
            List<ImagemForms> imagens = new ArrayList<>();
            for (byte[] imgData : imagensBytes) {
                String nomeArquivo = "form_" + UUID.randomUUID() + ".jpg";
                String caminhoCompleto = UPLOAD_DIR + nomeArquivo;
                try {
                    Files.write(Paths.get(caminhoCompleto), imgData);
                    imagens.add(new ImagemForms(caminhoCompleto, form));
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao salvar imagem no disco: " + e.getMessage());
                }
            }
            form.setImagens(imagens);
        }

        return form;
    }

    private void updateEntity(Forms form, FormRequestCriarDTO dto, List<byte[]> imagensBytes) {
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

        if (imagensBytes != null) {
            List<ImagemForms> imagens = new ArrayList<>();
            for (byte[] imgData : imagensBytes) {
                String nomeArquivo = "form_" + UUID.randomUUID() + ".jpg";
                String caminhoCompleto = UPLOAD_DIR + nomeArquivo;
                try {
                    Files.write(Paths.get(caminhoCompleto), imgData);
                    imagens.add(new ImagemForms(caminhoCompleto, form));
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao salvar imagem no disco: " + e.getMessage());
                }
            }
            form.setImagens(imagens);
        }
    }

    public Forms obterFormPorId(Integer id) {
        return formsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Formulário com id " + id + " não encontrado"));
    }

    public byte[] getImagemPorIndice(Integer formId, int indice) {
        Forms form = obterFormPorId(formId);
        List<ImagemForms> imagens = form.getImagens();
        if (imagens == null || indice < 0 || indice >= imagens.size()) {
            throw new NotFoundException("Índice inválido para formulário com id " + formId);
        }
        try {
            return Files.readAllBytes(Paths.get(imagens.get(indice).getCaminho()));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler imagem do disco: " + e.getMessage());
        }
    }

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

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
