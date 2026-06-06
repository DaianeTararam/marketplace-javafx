package edu.curso.persistence.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.curso.model.Comprador;
import edu.curso.model.ItemPedido;
import edu.curso.model.Pedido;
import edu.curso.model.Produto;
import edu.curso.persistence.GenericDao;
import edu.curso.persistence.PedidoDAO;

public class PedidoDAOImpl implements PedidoDAO {

    private GenericDao gDao;
    private Connection c;

    public PedidoDAOImpl() {
        try {
            gDao = new GenericDao();
            c = gDao.getConnection();
            System.out.println("Conexao PedidoDAO estabelecida com sucesso.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Falha de conexao no PedidoDAO.");
            e.printStackTrace();
        }
    }

    @Override
    public void finalizarVenda(Pedido pedido, List<ItemPedido> carrinho) {
        try {
            String sqlPedido =
                "INSERT INTO pedido (data_pedido, status, valor_total, user_comprador) " +
                "VALUES (?, ?, ?, ?)";
            PreparedStatement psPedido = c.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setDate(1, Date.valueOf(pedido.getDataPedido().toLocalDate()));
            psPedido.setString(2, pedido.getStatus());
            psPedido.setDouble(3, pedido.getValorTotal());
            psPedido.setString(4, pedido.getComprador().getUsername());
            psPedido.executeUpdate();

            ResultSet keys = psPedido.getGeneratedKeys();
            long idPedido = -1;
            if (keys.next()) {
                idPedido = keys.getLong(1);
            }

            String sqlItem =
                "INSERT INTO item_pedido (quantidade, valor_unitario, id_pedido, id_produto) " +
                "VALUES (?, ?, ?, ?)";
            PreparedStatement psItem = c.prepareStatement(sqlItem);
            for (ItemPedido item : carrinho) {
                psItem.setInt(1, item.getQuantidade());
                psItem.setDouble(2, item.getValorUnitario());
                psItem.setLong(3, idPedido);
                psItem.setLong(4, item.getProduto().getId());
                psItem.addBatch();
            }
            psItem.executeBatch();

            c.commit();
            c.setAutoCommit(true);
            System.out.println("Pedido finalizado com sucesso. ID: " + idPedido);

        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("Erro ao finalizar pedido.");
            e.printStackTrace();
        }
    }

    @Override
    public List<Pedido> buscarHistoricoComprador(String username, String senha) {
        List<Pedido> lista = new ArrayList<>();
        String sql =
            "SELECT ped.id, ped.data_pedido, ped.status, ped.valor_total, u.nome " +
            "FROM pedido ped " +
            "INNER JOIN usuario u ON ped.user_comprador = u.username " +
            "WHERE ped.user_comprador = ? AND u.senha = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, senha);
        
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getLong("id"));
                p.setDataPedido(rs.getDate("data_pedido").toLocalDate().atStartOfDay());
                p.setStatus(rs.getString("status"));
                p.setValorTotal(rs.getDouble("valor_total"));
                Comprador comp = new Comprador();
                comp.setUsername(username);
                comp.setNome(rs.getString("nome"));
                comp.setSenha(senha);
                p.setComprador(comp);
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar histórico.");
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Pedido> buscarHistoricoVendedor(String username, String senha) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT ped.id, ped.data_pedido, ped.status, ped.valor_total, prod.nome AS nome_produto " +
                     "FROM pedido ped " +
                     "INNER JOIN item_pedido item ON ped.id = item.id_pedido " +
                     "INNER JOIN produto prod ON item.id_produto = prod.id " +
                     "INNER JOIN loja l ON prod.id_loja = l.id " +
                     "INNER JOIN usuario u ON l.user_vendedor = u.username " +
                     "WHERE l.user_vendedor = ? AND u.senha = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, senha);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getLong("id"));
                p.setDataPedido(rs.getDate("data_pedido").toLocalDate().atStartOfDay());
                p.setStatus(rs.getString("status"));
                p.setValorTotal(rs.getDouble("valor_total"));
                Produto prod = new Produto();
                prod.setNome(rs.getString("nome_produto"));
                ItemPedido item = new ItemPedido();
                item.setProduto(prod);
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar historico.");
            e.printStackTrace();
        }
        return lista;
    }
}