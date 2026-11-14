package gourmetflow_api.repository;

import gourmetflow_api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    //Buscamos pedidos por estado específico
    List<Pedido> findByEstado(String estado);

    //Buscamos pedidos por número de mesa
    List<Pedido> findByNumeroMesa(Integer numeroMesa);

    //Buscamos pedidos por número de mesa y estado específico
    List<Pedido> findByNumeroMesaAndEstado(Integer numeroMesa, String estado);

}