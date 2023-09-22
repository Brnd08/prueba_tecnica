package prueba.tecnica.prueba_tecnica.views.pedidos;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import prueba.tecnica.prueba_tecnica.model.PartidaArticulo;
import prueba.tecnica.prueba_tecnica.service.ArticuloService;
import prueba.tecnica.prueba_tecnica.service.PedidosJasperService;
import prueba.tecnica.prueba_tecnica.util.FilaPedido;
import prueba.tecnica.prueba_tecnica.util.PedidoJasper;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Component
@ManagedBean
@ViewScoped
public class NuevosPedidosBean {
    private static final Logger logger = LogManager.getLogger(NuevosPedidosBean.class);

    @Autowired
    private ArticuloService articulosService;
    @Autowired
    private PedidosJasperService pedidosJasperService;

    private String descripcionPedido;
    private Integer idCliente;


    private List<FilaPedido> articulosDisponibles;

    /**
     * Metódo ejecutado despues de instanciar el bean.
     * Recupera los articulos registrados
     */
    public void init() {
        String mensaje = "";
        try {
            articulosDisponibles = new ArrayList<>();
            articulosService.findAll().forEach(articuloDTO ->
                    articulosDisponibles.add(FilaPedido.fromArticulo(articuloDTO)));
            mensaje = String.format(" %d articulos recuperados con exito", articulosDisponibles.size());
            logger.log(Level.INFO, mensaje);

        } catch (Exception e) {
            mensaje = "Fallo al recuperar articulos";
            logger.log(Level.ERROR, "{}. Error: {}", mensaje, e.getMessage());
        }
    }
    public void guardar() {

        String mensaje = "";
        try {
            Stream<FilaPedido> articulosAgregados = articulosDisponibles.stream()
                .filter(filaPedido -> filaPedido.getCantidad() > 0);

            final List<PartidaArticulo> partidaArticulos = articulosAgregados.map(filaPedido ->
                    PartidaArticulo.builder().idArticulo(filaPedido.getIdArticulo())
                            .cantidad(filaPedido.getCantidad()).build()
            ).collect(Collectors.toList());


            final PedidoJasper pedido = PedidoJasper.builder()
                    .descripcionPedido(descripcionPedido)
                    .idCliente(idCliente)
                    .articulosPedido(partidaArticulos)
                    .build();
            pedidosJasperService.registarPedido(pedido);
            mensaje = "Pedido Registrado correctamente";
        } catch (Exception e) {
            mensaje = "No se pudo registrar el pedido";
            logger.log(Level.ERROR, "{}. Error: {}", mensaje, e.getMessage());
        } finally {
            FacesContext.getCurrentInstance().addMessage("Ragistrar Pedido", new FacesMessage(mensaje));
            mostrarMensaje();
        }

        mostrarMensaje();
    }

    private void mostrarMensaje() {
        PrimeFaces.current().ajax().update("form:messages");
    }
}