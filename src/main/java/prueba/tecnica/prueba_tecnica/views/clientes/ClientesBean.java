package prueba.tecnica.prueba_tecnica.views.clientes;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import prueba.tecnica.prueba_tecnica.model.ClienteDTO;
import prueba.tecnica.prueba_tecnica.service.ClienteService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ManagedBean
@ViewScoped
public class ClientesBean {
    private static final Logger logger = LogManager.getLogger(ClientesBean.class);

    @Autowired
    private ClienteService clientesService;

    private List<ClienteDTO> clientes;
    private List<ClienteDTO> clientesSeleccionados;
    private ClienteDTO clienteSeleccionado;


    /**
     * Metódo ejecutado despues de instanciar el bean.
     * Recupera los clientes registrados
     */

    public void init() {
        String mensaje = "";
        try {
            clientesSeleccionados = new ArrayList<>();
            clientes = clientesService.findAll();
            mensaje = String.format(" %d clientes recuperados con exito", clientes.size());
            logger.log(Level.INFO, mensaje);
            FacesContext.getCurrentInstance().addMessage("Eliminar Pedido", new FacesMessage(mensaje));
            actualizarTablaYMensaje();
        } catch (Exception e) {
            mensaje = "Fallo al recuperar clientes";
            logger.log(Level.ERROR, "{}. Error: {}", mensaje, e.getMessage());
        }
    }

    public void nuevo() {
        this.clienteSeleccionado = new ClienteDTO();
    }

    public void eliminarSeleccionados() {
        String mensaje = "";
        try {
            int cantidad = clientesSeleccionados.size();
            for (ClienteDTO clienteDTO : clientesSeleccionados) {
                eliminarCliente(clienteDTO);
            }
            mensaje = cantidad + " Clientes fueron eliminados";
        } catch (Exception e) {
            mensaje = "No se pudo eliminar los clientes";
            logger.log(Level.ERROR, "{}. Error: {}", mensaje, e.getMessage());
        } finally {
            FacesContext.getCurrentInstance().addMessage("Eliminar Cliente", new FacesMessage(mensaje));
            actualizarTablaYMensaje();
        }
    }

    /**
     * Añede un nuevo cliente utilizando el valor de
     * atributo clienteSelecionado
     */
    public void agregarCliente() {
        String mensaje = "";
        try {
            int idNuevo = this.clientesService.create(clienteSeleccionado);
            clienteSeleccionado.setIdCliente(idNuevo);
            this.clientes.add(clienteSeleccionado);
            mensaje = "Cliente agregado con exito";
        } catch (Exception e) {
            mensaje = "No se pudo agregar el cliente";
            logger.log(Level.ERROR, "{}. Error: {}", mensaje, e.getMessage());
            e.printStackTrace();
        } finally {
            FacesContext.getCurrentInstance().addMessage("Agregar Cliente", new FacesMessage(mensaje));

            ocultarVentanaAgregar();
            actualizarTablaYMensaje();

        }
    }

    /**
     * Modifica el cliente correspondiente a el objeto guardado en el atributo
     * clienteSelecionado, actualiza los del cliente en la base de datos. Busca el
     * cliente utilizando el id de clienteSelecionado
     */
    public void modificarCliente() {
        String mensaje = "";
        try {
            this.clientesService.update(clienteSeleccionado.getIdCliente(), clienteSeleccionado);
            mensaje = "Cliente modificado con exito";
        } catch (Exception e) {
            mensaje = "No se pudo modificar el cliente";
            logger.log(Level.ERROR, "{}. Error: {}", mensaje, e.getMessage());
        } finally {
            FacesContext.getCurrentInstance().addMessage("Modificar Cliente", new FacesMessage(mensaje));
            ocultarVentanaModificar();
            actualizarTablaYMensaje();
        }
    }

    /**
     * Elimina el registro correspondiente al ultimo cliente seleccionado.
     * Utiliza el id del mismo para buscarlo en la base de datos
     */
    public void eliminarCliente() {
        String mensaje = "";
        try {
            this.clientesService.delete(clienteSeleccionado.getIdCliente());
            this.clientes.remove(clienteSeleccionado);
            this.clienteSeleccionado = null;
            mensaje = "Cliente eliminado";
        } catch (Exception e) {
            mensaje = "No se pudo eliminar el cliente";
            logger.log(Level.ERROR, "{}. Error: {}, clienteSelecionado {}", mensaje, e.getMessage(), clienteSeleccionado);
        } finally {
            FacesContext.getCurrentInstance().addMessage("Eliminar Cliente", new FacesMessage(mensaje));
            actualizarTablaYMensaje();
        }
    }

    /**
     * Elimina el registro correspondiente al dto especificado
     */
    private void eliminarCliente(ClienteDTO clienteEliminar) {
        this.clientesService.delete(clienteEliminar.getIdCliente());
        this.clientes.remove(clienteEliminar);
        this.clientesSeleccionados.remove(clienteEliminar);
    }

    /**
     * Actualiza el estado de la tabla de clientes y del mensaje en la vista.
     * La tabla agrega o elimina los clientes correspondientes.
     * El mensaje actuliza su contenido y se muestra al usuario
     */
    private void actualizarTablaYMensaje() {
        PrimeFaces.current().ajax().update("form:messages", "form:dt-clientes");
    }

    /**
     * Oculta la ventana emergente para agregar clientes
     */
    public void ocultarVentanaAgregar() {
        PrimeFaces.current().executeScript("PF('ventanaAgregar').hide()");
    }

    /**
     * Oculta la venta emergente para modificar clientes
     */
    public void ocultarVentanaModificar() {
        PrimeFaces.current().executeScript("PF('ventanaModificar').hide()");
    }
}