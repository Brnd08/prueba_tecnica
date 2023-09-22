package prueba.tecnica.prueba_tecnica.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import prueba.tecnica.prueba_tecnica.domain.Articulo;
import prueba.tecnica.prueba_tecnica.domain.PartidasPedido;
import prueba.tecnica.prueba_tecnica.domain.Pedido;
import prueba.tecnica.prueba_tecnica.model.PartidasPedidoDTO;
import prueba.tecnica.prueba_tecnica.repos.ArticuloRepository;
import prueba.tecnica.prueba_tecnica.repos.PartidasPedidoRepository;
import prueba.tecnica.prueba_tecnica.repos.PedidoRepository;
import prueba.tecnica.prueba_tecnica.util.NotFoundException;

import java.util.ArrayList;
import java.util.List;


@Service
public class PartidasPedidoService {

    private final PartidasPedidoRepository partidasPedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ArticuloRepository articuloRepository;

    public PartidasPedidoService(final PartidasPedidoRepository partidasPedidoRepository,
                                 final PedidoRepository pedidoRepository, final ArticuloRepository articuloRepository) {
        this.partidasPedidoRepository = partidasPedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.articuloRepository = articuloRepository;
    }

    public List<PartidasPedido> getPartidasPedido(final Pedido pedido){
        return this.partidasPedidoRepository.findByPedido(pedido);
    }

    public List<PartidasPedidoDTO> findAll() {
        final List<PartidasPedido> partidasPedidos = partidasPedidoRepository.findAll(Sort.by("idPartida"));
        List<PartidasPedidoDTO> list = new ArrayList<>();
        for (PartidasPedido partidasPedido : partidasPedidos) {
            PartidasPedidoDTO partidasPedidoDTO = mapToDTO(partidasPedido);
            list.add(partidasPedidoDTO);
        }
        return list;
    }

    public PartidasPedidoDTO get(final Integer idPartida) {
        return partidasPedidoRepository.findById(idPartida)
                .map(this::mapToDTO)
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PartidasPedidoDTO partidasPedidoDTO) {
        final PartidasPedido partidasPedido = new PartidasPedido();
        mapToEntity(partidasPedidoDTO, partidasPedido);
        return partidasPedidoRepository.save(partidasPedido).getIdPartida();
    }

    public void update(final Integer idPartida, final PartidasPedidoDTO partidasPedidoDTO) {
        final PartidasPedido partidasPedido = partidasPedidoRepository.findById(idPartida)
                .orElseThrow(NotFoundException::new);
        mapToEntity(partidasPedidoDTO, partidasPedido);
        partidasPedidoRepository.save(partidasPedido);
    }

    public void delete(final Integer idPartida) {
        partidasPedidoRepository.deleteById(idPartida);
    }

    private PartidasPedidoDTO mapToDTO(final PartidasPedido partidasPedido) {
        return PartidasPedidoDTO.builder()
                .idPartida(partidasPedido.getIdPartida())
                .idPedido(partidasPedido.getPedido().getIdPedido())
                .idArticulo(partidasPedido.getArticulo().getIdArticulo())
                .cantidad(partidasPedido.getCantidad()).build();
    }

    private void mapToEntity(final PartidasPedidoDTO partidasPedidoDTO, final PartidasPedido partidasPedido) {
        partidasPedido.setCantidad(partidasPedidoDTO.getCantidad());
        final Pedido pedido = partidasPedidoDTO.getIdPedido() == null ? null : pedidoRepository.findById(partidasPedidoDTO.getIdPedido())
                .orElseThrow(() -> new NotFoundException("pedido no encontrado"));
        partidasPedido.setPedido(pedido);
        final Articulo articulo = partidasPedidoDTO.getIdArticulo() == null ? null : articuloRepository.findById(partidasPedidoDTO.getIdArticulo())
                .orElseThrow(() -> new NotFoundException("articulo no encontrado"));
        partidasPedido.setArticulo(articulo);
    }

}
