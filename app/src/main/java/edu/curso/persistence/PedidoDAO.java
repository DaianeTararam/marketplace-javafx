package edu.curso.persistence;

import java.util.List;

import edu.curso.model.ItemPedido;
import edu.curso.model.Pedido;

public interface PedidoDAO {
    public void finalizarVenda(Pedido pedido, List<ItemPedido> carrinho);
    public List<Pedido> buscarHistoricoPorComprador(String username);
}