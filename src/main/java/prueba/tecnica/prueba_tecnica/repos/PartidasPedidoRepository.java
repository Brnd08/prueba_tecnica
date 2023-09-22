package prueba.tecnica.prueba_tecnica.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.tecnica.prueba_tecnica.domain.Articulo;
import prueba.tecnica.prueba_tecnica.domain.PartidasPedido;
import prueba.tecnica.prueba_tecnica.domain.Pedido;

import java.util.List;


public interface PartidasPedidoRepository extends JpaRepository<PartidasPedido, Integer> {
    List<PartidasPedido> findByPedido(Pedido pedido);
    List<PartidasPedido> findByPedidoAndArticulo(Pedido pedido, Articulo articulo);

}
