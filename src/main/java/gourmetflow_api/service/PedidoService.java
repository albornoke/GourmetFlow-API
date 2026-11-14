package gourmetflow_api.service;

import gourmetflow_api.dto.PedidoRequestDTO;
import gourmetflow_api.model.Cliente;
import gourmetflow_api.model.Pedido;
import gourmetflow_api.model.Usuario;
import gourmetflow_api.repository.ClienteRepository;
import gourmetflow_api.repository.PedidoRepository;
import gourmetflow_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Pedido crearPedido(PedidoRequestDTO request) {
        Pedido pedido = new Pedido();
        pedido.setNumeroMesa(request.numeroMesa());
        pedido.setEstado(request.estado());

        if (request.clienteId() != null) {
            Cliente cliente = clienteRepository.findById(request.clienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + request.clienteId()));
            pedido.setCliente(cliente);
        }

        if (request.usuarioId() != null) {
            Usuario mesero = usuarioRepository.findById(request.usuarioId())
                    .orElseThrow(() -> new RuntimeException("Mesero/Usuario no encontrado con ID: " + request.usuarioId()));
            pedido.setMesero(mesero);
        }

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public Pedido actualizarEstadoPedido(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }
}