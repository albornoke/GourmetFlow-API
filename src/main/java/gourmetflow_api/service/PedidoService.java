package gourmetflow_api.service;

import gourmetflow_api.dto.PedidoRequestDTO;
import gourmetflow_api.model.Pedido;
import gourmetflow_api.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public Pedido crearPedido(PedidoRequestDTO request) {
        Pedido pedido = new Pedido();
        pedido.setNumeroMesa(request.numeroMesa());
        pedido.setEstado(request.estado());

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