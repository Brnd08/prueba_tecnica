package prueba.tecnica.prueba_tecnica.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prueba.tecnica.prueba_tecnica.domain.Articulo;
import prueba.tecnica.prueba_tecnica.domain.PartidasPedido;
import prueba.tecnica.prueba_tecnica.domain.Pedido;
import prueba.tecnica.prueba_tecnica.model.PartidaArticulo;
import prueba.tecnica.prueba_tecnica.model.PartidasPedidoDTO;
import prueba.tecnica.prueba_tecnica.model.PedidoDTO;
import prueba.tecnica.prueba_tecnica.repos.ClienteRepository;
import prueba.tecnica.prueba_tecnica.repos.PartidasPedidoRepository;
import prueba.tecnica.prueba_tecnica.repos.PedidoRepository;
import prueba.tecnica.prueba_tecnica.util.PedidoJasper;
import prueba.tecnica.prueba_tecnica.util.PedidoJasperExporter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidosJasperService extends PedidosService {
    private static final Logger logger = LogManager.getLogger(PedidosJasperService.class);
    @Autowired
    PartidasPedidoService partidasPedidoService;

    public PedidosJasperService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository, PartidasPedidoRepository partidasPedidoRepository) {
        super(pedidoRepository, clienteRepository, partidasPedidoRepository);
    }


    public void eliminarPedido(final PedidoJasper pedidoJasper) {
        // eliminar pedido
        super.delete(pedidoJasper.getIdPedido());
    }

    public List<PedidoJasper> getAllAsPedidoJasper() {
        List<PedidoJasper> pedidosJasper = new ArrayList<>();
        for (Pedido pedido : super.getAll()) {
            pedidosJasper.add(this.mapearAPedidoJasper(pedido));
        }
        return pedidosJasper;
    }

    public Integer registarPedido(final PedidoJasper pedidoJasper) {
        // crear pedido
        Integer idPedidoCreado = super.create(
                PedidoDTO.builder()
                        .idPedido(pedidoJasper.getIdPedido())
                        .descripcionPedido(pedidoJasper.getDescripcionPedido())
                        .idCliente(pedidoJasper.getIdCliente())
                        .build()
        );
        // crear partidas por cada articulo del pedido
        List<PartidasPedidoDTO> partidasPedidoDTOs = pedidoJasper.getArticulosPedido()
                .stream().map(partidaArticulo -> PartidasPedidoDTO.builder()
                        .idPedido(idPedidoCreado)
                        .idArticulo(partidaArticulo.getIdArticulo())
                        .cantidad(partidaArticulo.getCantidad())
                        .build()
                ).collect(Collectors.toList());
        partidasPedidoDTOs.forEach(partidasPedidoService::create);
        return idPedidoCreado;
    }


    public byte[] obtenerReportePdf(PedidoJasper pedidoJasper) {
        byte[] reportePDF;
        try {
            reportePDF = PedidoJasperExporter.exportToPDF(pedidoJasper);
        } catch (Exception e) {
            logger.log(Level.ERROR, "No se pudo crear Reporte Jasper. PedidoDTO: {}. Error: {}", pedidoJasper, e.getMessage());
            reportePDF = null;
        }
        return reportePDF;
    }


    public PedidoJasper mapearAPedidoJasper(Pedido pedido) {
        List<PartidaArticulo> articulosPedido = new ArrayList<>();
        partidasPedidoService.getPartidasPedido(pedido).forEach(partida ->
                articulosPedido.add(mapToPartidaArticulo(partida, partida.getArticulo()))
        );
        return PedidoJasper.builder()
                .idPedido(pedido.getIdPedido())
                .descripcionPedido(pedido.getDescripcionPedido())
                .clientePedido(pedido.getCliente().getNombreCliente())
                .articulosPedido(articulosPedido).build();
    }

    private PartidaArticulo mapToPartidaArticulo(PartidasPedido partidasPedido, Articulo articulo) {
        return PartidaArticulo.builder()
                .id(articulo.getIdArticulo())
                .nombre(articulo.getNombre())
                .descripcion(articulo.getDescripcion())
                .precio(articulo.getPrecio())
                .cantidad(partidasPedido.getCantidad()).build();
    }

    public PedidoJasper getPedidoJasper(PedidoJasper pedidoJasper) {
        Pedido pedido = super.getPedido(pedidoJasper.getIdPedido());
        return PedidoJasper.builder()
                .idPedido(pedido.getIdPedido())
                .descripcionPedido(pedido.getDescripcionPedido())
                .idCliente(pedido.getCliente().getIdCliente()).build();
    }

}