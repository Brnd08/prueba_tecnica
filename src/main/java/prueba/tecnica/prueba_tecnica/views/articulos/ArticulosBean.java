package prueba.tecnica.prueba_tecnica.views.articulos;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import prueba.tecnica.prueba_tecnica.model.ArticuloDTO;
import prueba.tecnica.prueba_tecnica.service.ArticuloService;

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
public class ArticulosBean {
    private static final Logger logger = LogManager.getLogger(ArticulosBean.class);

    @Autowired
    private ArticuloService articulosService;

    private List<ArticuloDTO> articulos;
    private List<ArticuloDTO> articulosSeleccionados;
    private ArticuloDTO articuloSeleccionado;


    /**
     * Metódo ejecutado despues de instanciar el bean.
     * Recupera los articulos registrados
     */
    public void init() {
        String mensaje = "";
        try {
            articulosSeleccionados = new ArrayList<>();
            articulos = articulosService.findAll();
            mensaje = String.format(" %d articulos recuperados con exito", articulos.size());
            logger.log(Level.INFO, mensaje);
            FacesContext.getCurrentInstance().addMessage("Eliminar Pedido", new FacesMessage(mensaje));
            actualizarTablaYMensaje();
        } catch (Exception e) {
            mensaje = "Fallo al recuperar articulos";
            logger.log(Level.ERROR, "{}. Error: {}", mensaje, e.getMessage());
        }
    }

    public void nuevo() {
        this.articuloSeleccionado = new ArticuloDTO();
    }

    public void eliminarSeleccionados() {
        String mensaje = "";
        try {
            int cantidad = articulosSeleccionados.size();
            for (ArticuloDTO articuloDTO : articulosSeleccionados) {
                eliminarArticulo(articuloDTO);
            }
            mensaje = cantidad + " Articulos fueron eliminados";
        } catch (Exception e) {
            mensaje = "No se pudo eliminar los articulos";
            logger.log(Level.ERROR, "{}. Error: {}", mensaje, e.getMessage());
        } finally {
            FacesContext.getCurrentInstance().addMessage("Eliminar Articulo", new FacesMessage(mensaje));
            actualizarTablaYMensaje();
        }
    }

    /**
     * Añede un nuevo articulo utilizando el valor de
     * atributo articuloSelecionado
     */
    public void agregarArticulo() {
        String mensaje = "";
        try {
            int idNuevo = this.articulosService.create(articuloSeleccionado);
            articuloSeleccionado.setIdArticulo(idNuevo);
            this.articulos.add(articuloSeleccionado);
            mensaje = "Articulo agregado con exito";
        } catch (Exception e) {
            mensaje = "No se pudo agregar el articulo";
            logger.log(Level.ERROR, "{}. Error: {}", mensaje, e.getMessage());
            e.printStackTrace();
        } finally {
            FacesContext.getCurrentInstance().addMessage("Agregar Articulo", new FacesMessage(mensaje));

            ocultarVentanaAgregar();
            actualizarTablaYMensaje();

        }
    }

    /**
     * Modifica el articulo correspondiente a el objeto guardado en el atributo
     * articuloSelecionado, actualiza los del articulo en la base de datos. Busca el
     * articulo utilizando el id de articuloSelecionado
     */
    public void modificarArticulo() {
        String mensaje = "";
        try {
            this.articulosService.update(articuloSeleccionado);
            mensaje = "Articulo modificado con exito";
        } catch (Exception e) {
            mensaje = "No se pudo modificar el articulo";
            logger.log(Level.ERROR, "{}. Error: {}", mensaje, e.getMessage());
        } finally {
            FacesContext.getCurrentInstance().addMessage("Modificar Articulo", new FacesMessage(mensaje));
            ocultarVentanaModificar();
            actualizarTablaYMensaje();
        }
    }

    /**
     * Elimina el registro correspondiente al ultimo articulo seleccionado.
     * Utiliza el id del mismo para buscarlo en la base de datos
     */
    public void eliminarArticulo() {
        String mensaje = "";
        try {
            this.articulosService.delete(articuloSeleccionado);
            this.articulos.remove(articuloSeleccionado);
            this.articuloSeleccionado = null;
            mensaje = "Articulo eliminado";
        } catch (Exception e) {
            mensaje = "No se pudo eliminar el articulo";
            logger.log(Level.ERROR, "{}. Error: {}, articuloSelecionado {}", mensaje, e.getMessage(), articuloSeleccionado);
        } finally {
            FacesContext.getCurrentInstance().addMessage("Eliminar Articulo", new FacesMessage(mensaje));
            actualizarTablaYMensaje();
        }
    }

    /**
     * Elimina el registro correspondiente al dto especificado
     */
    private void eliminarArticulo(ArticuloDTO articuloEliminar) {
        this.articulosService.delete(articuloEliminar);
        this.articulos.remove(articuloEliminar);
        this.articulosSeleccionados.remove(articuloEliminar);
    }

    /**
     * Actualiza el estado de la tabla de articulos y del mensaje en la vista.
     * La tabla agrega o elimina los articulos correspondientes.
     * El mensaje actuliza su contenido y se muestra al usuario
     */
    private void actualizarTablaYMensaje() {
        PrimeFaces.current().ajax().update("form:messages", "form:dt-articulos");
    }

    /**
     * Oculta la ventana emergente para agregar articulos
     */
    public void ocultarVentanaAgregar() {
        PrimeFaces.current().executeScript("PF('ventanaAgregar').hide()");
    }

    /**
     * Oculta la venta emergente para modificar articulos
     */
    public void ocultarVentanaModificar() {
        PrimeFaces.current().executeScript("PF('ventanaModificar').hide()");
    }
}