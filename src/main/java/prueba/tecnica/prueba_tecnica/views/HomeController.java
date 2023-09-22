package prueba.tecnica.prueba_tecnica.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping()
    public String index() {
        return "index.xhtml";
    }
    @GetMapping("/articulos")
    public String crudArticulos() {
        return "articulos.xhtml";
    }
    @GetMapping("/nuevosPedidos")
    public String nuevoPedido() {
        return "nuevosPedidos.xhtml";
    }
    @GetMapping("/clientes")
    public String crudClientes() {
        return "clientes.xhtml";
    }
    @GetMapping("/pedidos")
    public String crudPedidos() {
        return "pedidos.xhtml";
    }

}
