package prueba.tecnica.prueba_tecnica.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldError  implements Serializable {

    private String field;
    private String errorCode;

}
