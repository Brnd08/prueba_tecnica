package prueba.tecnica.prueba_tecnica.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import prueba.tecnica.prueba_tecnica.domain.Cliente;
import prueba.tecnica.prueba_tecnica.model.ClienteDTO;
import prueba.tecnica.prueba_tecnica.repos.ClienteRepository;
import prueba.tecnica.prueba_tecnica.util.NotFoundException;

import java.util.ArrayList;
import java.util.List;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(final ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteDTO> findAll() {
        final List<Cliente> clientes = clienteRepository.findAll(Sort.by("idCliente"));
        List<ClienteDTO> list = new ArrayList<>();
        for (Cliente cliente : clientes) {
            ClienteDTO clienteDTO = mapToDTO(cliente);
            list.add(clienteDTO);
        }
        return list;
    }

    public ClienteDTO get(final Integer idCliente) {
        return clienteRepository.findById(idCliente)
                .map(this::mapToDTO)
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ClienteDTO clienteDTO) {
        final Cliente cliente = new Cliente();
        mapToEntity(clienteDTO, cliente);
        return clienteRepository.save(cliente).getIdCliente();
    }

    public void update(final Integer idCliente, final ClienteDTO clienteDTO) {
        final Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(NotFoundException::new);
        mapToEntity(clienteDTO, cliente);
        clienteRepository.save(cliente);
    }

    public void delete(final Integer idCliente) {
        clienteRepository.deleteById(idCliente);
    }

    private ClienteDTO mapToDTO(final Cliente cliente) {
        return ClienteDTO.builder()
                .idCliente(cliente.getIdCliente())
                .nombreCliente(cliente.getNombreCliente())
                .rfc(cliente.getRfc())
                .build();
    }

    private void mapToEntity(final ClienteDTO clienteDTO, final Cliente cliente) {
        cliente.setNombreCliente(clienteDTO.getNombreCliente());
        cliente.setRfc(clienteDTO.getRfc());
    }

}
