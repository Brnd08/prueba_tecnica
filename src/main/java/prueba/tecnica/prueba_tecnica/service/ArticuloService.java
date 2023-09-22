package prueba.tecnica.prueba_tecnica.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import prueba.tecnica.prueba_tecnica.domain.Articulo;
import prueba.tecnica.prueba_tecnica.model.ArticuloDTO;
import prueba.tecnica.prueba_tecnica.repos.ArticuloRepository;
import prueba.tecnica.prueba_tecnica.util.NotFoundException;

import java.util.ArrayList;
import java.util.List;


@Service
public class ArticuloService {
    private final Logger logger = LogManager.getLogger(ArticuloService.class);

    @Autowired
    private ArticuloRepository articuloRepository;

    public List<ArticuloDTO> findAll() {
        final List<Articulo> articulos = articuloRepository.findAll(Sort.by("idArticulo"));

        logger.log(Level.INFO, "{} articulos recuperados", articulos.size());
        List<ArticuloDTO> list = new ArrayList<>();
        for (Articulo articulo : articulos) {
            ArticuloDTO articuloDTO = mapToDTO(articulo);
            list.add(articuloDTO);
        }
        return list;
    }

    public ArticuloDTO get(final Integer idArticulo) {
        ArticuloDTO articuloDTO= articuloRepository.findById(idArticulo)
                .map(this::mapToDTO)
                .orElseThrow(NotFoundException::new);
        logger.log(Level.INFO, "DTO obtenido. DTO: {}. id articulo: {}", articuloDTO, idArticulo);
        return articuloDTO;
    }

    public Integer create(final ArticuloDTO articuloDTO) {
        final Articulo articulo = new Articulo();
        mapToEntity(articuloDTO, articulo);
        logger.log(Level.INFO, "Articulo creado. DTO: {}. Articulo: {}", articuloDTO, articulo);
        return articuloRepository.save(articulo).getIdArticulo();
    }

    public void update(final Integer idArticulo, final ArticuloDTO articuloDTO) {
        final Articulo articulo = articuloRepository.findById(idArticulo)
                .orElseThrow(NotFoundException::new);
        mapToEntity(articuloDTO, articulo);
        articuloRepository.save(articulo);
        logger.log(Level.INFO, "Articulo actualizado. DTO: {}. Articulo: {}", articuloDTO, articulo);
    }
    public void update(final ArticuloDTO articuloDTO) {
        final Articulo articulo = articuloRepository.findById(articuloDTO.getIdArticulo())
                .orElseThrow(NotFoundException::new);
        mapToEntity(articuloDTO, articulo);
        articuloRepository.save(articulo);
        logger.log(Level.INFO, "Articulo actualizado. DTO: {}. Articulo: {}", articuloDTO, articulo);
    }

    public void delete(final Integer idArticulo) {
        articuloRepository.deleteById(idArticulo);
        logger.log(Level.INFO, "Articulo eliminado. Id articulo {}", idArticulo);
    }

    public void delete(final ArticuloDTO articuloDTO) {
        articuloRepository.deleteById(articuloDTO.getIdArticulo());
        logger.log(Level.INFO, "Articulo eliminado. DTO {}", articuloDTO);
    }

    private ArticuloDTO mapToDTO(final Articulo articulo) {
        return ArticuloDTO.builder()
                .idArticulo(articulo.getIdArticulo())
                .nombre(articulo.getNombre())
                .descripcion(articulo.getDescripcion())
                .precio(articulo.getPrecio())
                .build();
    }

    private void mapToEntity(final ArticuloDTO articuloDTO, final Articulo articulo) {
        articulo.setNombre(articuloDTO.getNombre());
        articulo.setDescripcion(articuloDTO.getDescripcion());
        articulo.setPrecio(articuloDTO.getPrecio());
    }

}
