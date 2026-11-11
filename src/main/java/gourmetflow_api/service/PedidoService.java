package gourmetflow_api.service;

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

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        // Lógica de negocio transaccional para registrar un nuevo pedido en cocina
        return pedidoRepository.save(pedido);
    }
}