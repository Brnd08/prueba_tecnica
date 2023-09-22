package prueba.tecnica.prueba_tecnica.util;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import prueba.tecnica.prueba_tecnica.model.ArticuloDTO;

@Builder
@Getter
@ToString
@Setter
public class FilaPedido {
    private int idArticulo;
    private String nombreArticulo;
    private double precioArticulo;
    private String descripcionArticulo;
    private int cantidad;

    public void incrementarCantidad() {
        cantidad += 1;
    }

    public void disminuirCantidad() {
        if (cantidad > 0) {
            cantidad -= 1;
        }
    }

    public static FilaPedido fromArticulo(ArticuloDTO articulo) {
        return builder().idArticulo(articulo.getIdArticulo())
                .nombreArticulo(articulo.getNombre())
                .cantidad(0)
                .descripcionArticulo(articulo.getDescripcion())
                .precioArticulo(articulo.getPrecio()).build();
    }
}
