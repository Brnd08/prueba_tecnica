package prueba.tecnica.prueba_tecnica.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Getter
@ToString
public class PartidaArticulo implements Serializable {
    private  Integer id;
    private  Integer idArticulo;
    private  String nombre;
    private  String descripcion;
    private  Double precio;
    private  Integer cantidad;
}