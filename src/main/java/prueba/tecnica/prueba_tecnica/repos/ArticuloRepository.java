package prueba.tecnica.prueba_tecnica.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.tecnica.prueba_tecnica.domain.Articulo;


public interface ArticuloRepository extends JpaRepository<Articulo, Integer> {
}
