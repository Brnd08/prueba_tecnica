package prueba.tecnica.prueba_tecnica.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.tecnica.prueba_tecnica.domain.Pedido;


public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}
