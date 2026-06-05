package edu.curso.persistence.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.curso.model.ItemPedido;
import edu.curso.model.Pedido;
import edu.curso.persistence.GenericDao;
import edu.curso.persistence.PedidoDAO;

public class PedidoDAOImpl implements PedidoDAO {
    private GenericDao gDao;
    private Connection c;

    public PedidoDAOImpl() {
        try {
            gDao = new GenericDao();
            c = gDao.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finalizarVenda(Pedido pedido, List<ItemPedido> carrinho) {
        System.out.println("Venda finalizada no banco.");
    }

    @Override
    public List<Pedido> buscarHistoricoPorComprador(String username) {
        return new ArrayList<>(); 
    }
}