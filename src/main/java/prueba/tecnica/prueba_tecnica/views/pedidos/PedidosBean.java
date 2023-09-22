package prueba.tecnica.prueba_tecnica.views.pedidos;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import prueba.tecnica.prueba_tecnica.service.PedidosJasperService;
import prueba.tecnica.prueba_tecnica.util.PedidoJasper;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ManagedBean
@ViewScoped
public class PedidosBean {
    private static final Logger logger = LogManager.getLogger(PedidosBean.class);
    @Autowired
    private PedidosJasperService pedidosJasperService;
    private List<PedidoJasper> pedidos;
    private List<PedidoJasper> pedidosSeleccionados;
    private PedidoJasper pedidoSeleccionado;
    private StreamedContent pdfPedido;

    public void crearPdf() {
        PedidoJasper pedido = pedidosSeleccionados.get(0);
        ByteArrayInputStream pdfStream = new ByteArrayInputStream(
                pedidosJasperService.obtenerReportePdf(pedido)
        );
        int idPedido= pedido.getIdPedido();
        String nombreCliente = pedido.getClientePedido();
        pdfPedido = DefaultStreamedContent.builder()
                .name(String.format("pedido_%d_%s.pdf", idPedido, nombreCliente))
                .contentType("application/pdf")
                .stream(() -> pdfStream)
                .build();
    }


    /**
     * Metódo ejecutado despues de instanciar el bean.
     * Recupera los pedidos registrados
     */
    public void init() {
        String mensaje = "";
        try {
            pedidosSeleccionados = new ArrayList<>();
            pedidos = pedidosJasperService.getAllAsPedidoJasper();
            mensaje = String.format(" %d pedidos recuperados con exito", pedidos.size());
            logger.log(Level.INFO, mensaje);
            FacesContext.getCurrentInstance().addMessage("Eliminar Pedido" , new FacesMessage(mensaje));
            actualizarTablaYMensaje();
        } catch (Exception e) {
            mensaje = "Fallo al recuperar pedidos";
            logger.log(Level.ERROR, "{}. Error: {}", mensaje, e.getMessage());
            e.printStackTrace();
        }
    }

    public void nuevo() {
        this.pedidoSeleccionado = new PedidoJasper();
    }

    public void eliminarSeleccionados() {
        String mensaje = "";
        try {
            int cantidad = pedidosSeleccionados.size();
            for (PedidoJasper pedidoJasper : pedidosSeleccionados) {
                eliminarPedido(pedidoJasper);
            }
            mensaje = cantidad + " Pedidos fueron eliminados";
        } catch (Exception e) {
            mensaje = "No se pudo eliminar los pedidos";
            logger.log(Level.ERROR, "{}. Error: {}", mensaje, e.getMessage());
        } finally {
            FacesContext.getCurrentInstance().addMessage("Eliminar Pedido", new FacesMessage(mensaje));
            actualizarTablaYMensaje();
        }
    }

    /**
     * Elimina el registro correspondiente al ultimo pedido seleccionado.
     * Utiliza el id del mismo para buscarlo en la base de datos
     */
    public void eliminarPedido() {
        String mensaje = "";
        try {
            this.eliminarPedido(pedidoSeleccionado);
            this.pedidos.remove(pedidoSeleccionado);
            mensaje = "Pedido eliminado";
        } catch (Exception e) {
            mensaje = "No se pudo eliminar el pedido";
            logger.log(Level.ERROR, "{}. Error: {}, pedidoSelecionado {}", mensaje, e.getMessage(), pedidoSeleccionado);
        } finally {
            FacesContext.getCurrentInstance().addMessage("Eliminar Pedido", new FacesMessage(mensaje));
            actualizarTablaYMensaje();
        }
    }

    /**
     * Elimina el registro correspondiente al dto especificado
     */
    private void eliminarPedido(PedidoJasper pedidoJasper) {
        this.pedidosJasperService.delete(pedidoJasper.getIdPedido());
        this.pedidos.remove(pedidoJasper);
    }

    /**
     * Actualiza el estado de la tabla de pedidos y del mensaje en la vista.
     * La tabla agrega o elimina los pedidos correspondientes.
     * El mensaje actuliza su contenido y se muestra al usuario
     */
    private void actualizarTablaYMensaje() {
        PrimeFaces.current().ajax().update("form:messages", "form:dt-pedidos");
    }

    /**
     * Oculta la ventana emergente para agregar pedidos
     */
    public void ocultarVentanaAgregar() {
        PrimeFaces.current().executeScript("PF('ventanaAgregar').hide()");
    }
    public void actualizarTabla(){
        PrimeFaces.current().executeScript("PF('form:dt-pedidos').hide()");
    }

}