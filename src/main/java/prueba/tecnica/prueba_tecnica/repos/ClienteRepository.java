package prueba.tecnica.prueba_tecnica.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.tecnica.prueba_tecnica.domain.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
