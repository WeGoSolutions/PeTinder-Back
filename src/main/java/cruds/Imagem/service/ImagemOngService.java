package cruds.Imagem.service;

import cruds.Imagem.entity.ImagemOng;
import cruds.Imagem.repository.ImagemOngRepository;
import cruds.Ong.entity.Ong;
import cruds.Ong.repository.OngRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagemOngService {

    @Autowired
    private ImagemOngRepository imagemOngRepository;

    @Autowired
    private OngRepository ongRepository;

    public void salvarOngComImagem(Ong ong, ImagemOng imagemOng) {
        ImagemOng imagemSalva = imagemOngRepository.save(imagemOng);
        ong.setImagemOng(imagemSalva);
        ongRepository.save(ong);
    }
}