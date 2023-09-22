package prueba.tecnica.prueba_tecnica.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO  implements Serializable {

    private Integer idPedido;

    @Size(max = 255)
    private String descripcionPedido;

    @NotNull
    private Integer idCliente;

}
