package prueba.tecnica.prueba_tecnica.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;


@Entity
@Getter
@Setter
@ToString
public class Articulo {

    @Id()
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idArticulo;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @OneToMany(mappedBy = "articulo", cascade = CascadeType.ALL)
    private Set<PartidasPedido> articuloPartidasPedidos;

}
