package prueba.tecnica.prueba_tecnica.util;

import lombok.*;
import prueba.tecnica.prueba_tecnica.model.PartidaArticulo;

import java.util.List;
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PedidoJasper {
    private int idPedido;
    private String descripcionPedido;
    private String clientePedido;
    private List<PartidaArticulo> articulosPedido;
    private int idCliente;
}
