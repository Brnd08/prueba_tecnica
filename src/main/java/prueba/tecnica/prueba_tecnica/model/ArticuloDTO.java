package prueba.tecnica.prueba_tecnica.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ArticuloDTO implements Serializable{
    private Integer idArticulo;

    @NotNull
    @Size(max = 50)
    private String nombre;

    @NotNull
    @Size(max = 255)
    private String descripcion;

    @NotNull
    private Double precio;


}
