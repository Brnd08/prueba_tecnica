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
public class ClienteDTO implements Serializable {

    private Integer idCliente;

    @NotNull
    @Size(max = 70, min = 2)
    private String nombreCliente;

    @NotNull
    @Size(max = 13, min = 11)
    private String rfc;

}
