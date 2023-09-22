package prueba.tecnica.prueba_tecnica.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import prueba.tecnica.prueba_tecnica.domain.Cliente;
import prueba.tecnica.prueba_tecnica.domain.Pedido;
import prueba.tecnica.prueba_tecnica.model.PedidoDTO;
import prueba.tecnica.prueba_tecnica.repos.ClienteRepository;
import prueba.tecnica.prueba_tecnica.repos.PartidasPedidoRepository;
import prueba.tecnica.prueba_tecnica.repos.PedidoRepository;
import prueba.tecnica.prueba_tecnica.util.NotFoundException;

import java.util.ArrayList;
import java.util.List;


@Service
public class PedidosService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    public PedidosService(final PedidoRepository pedidoRepository,
                          final ClienteRepository clienteRepository,
                          final PartidasPedidoRepository partidasPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<PedidoDTO> findAll() {
        final List<Pedido> pedidos = pedidoRepository.findAll(Sort.by("idPedido"));
        List<PedidoDTO> list = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            PedidoDTO pedidoDTO = mapToDTO(pedido);
            list.add(pedidoDTO);
        }
        return list;
    }

    public List<Pedido> getAll() {
        final List<Pedido> pedidos = pedidoRepository.findAll(Sort.by("idPedido"));
        return pedidos;
    }


    public PedidoDTO get(final Integer idPedido) {
        return pedidoRepository.findById(idPedido)
                .map(this::mapToDTO)
                .orElseThrow(NotFoundException::new);
    }
    public Pedido getPedido(final Integer idPedido) {
        return pedidoRepository.findById(idPedido)
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PedidoDTO pedidoDTO) {
        final Pedido pedido = new Pedido();
        mapToEntity(pedidoDTO, pedido);
        return pedidoRepository.save(pedido).getIdPedido();
    }

    public void update(final Integer idPedido, final PedidoDTO pedidoDTO) {
        final Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(NotFoundException::new);
        mapToEntity(pedidoDTO, pedido);
        pedidoRepository.save(pedido);
    }

    public void delete(final Integer idPedido) {
        pedidoRepository.deleteById(idPedido);
    }



    public PedidoDTO mapToDTO(final Pedido pedido) {
        return PedidoDTO.builder()
                .idPedido(pedido.getIdPedido())
                .descripcionPedido(pedido.getDescripcionPedido())
                .idCliente(pedido.getCliente().getIdCliente())
                .build();
    }

    public void mapToEntity(final PedidoDTO pedidoDTO, final Pedido pedido) {
        pedido.setDescripcionPedido(pedidoDTO.getDescripcionPedido());
        final Cliente cliente = pedidoDTO.getIdCliente() == null ? null : clienteRepository.findById(pedidoDTO.getIdCliente())
                .orElseThrow(() -> new NotFoundException("cliente not found"));
        pedido.setCliente(cliente);
    }



}
