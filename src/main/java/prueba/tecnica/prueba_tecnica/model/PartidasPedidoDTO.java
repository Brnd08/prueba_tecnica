package prueba.tecnica.prueba_tecnica.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartidasPedidoDTO  implements Serializable {

    private Integer idPartida;

    @NotNull
    private Integer cantidad;

    @NotNull
    private Integer idPedido;

    @NotNull
    private Integer idArticulo;

}
