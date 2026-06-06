package edu.curso.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.curso.model.Loja;
import edu.curso.model.Vendedor;
import edu.curso.persistence.GenericDao;
import edu.curso.persistence.LojaDAO;

public class LojaDAOImpl implements LojaDAO {
    private GenericDao gDao;
    private Connection c;

    public LojaDAOImpl() {
        try {
            gDao = new GenericDao();
            c = gDao.getConnection();
            System.out.println("Conexao LojaDAO estabelecida com sucesso.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Falha de conexao na LojaDAO.");
            e.printStackTrace();
        }
    }

    @Override
    public void cadastrar(Loja l) {
        try {
            String sql = "INSERT INTO loja (nome, user_vendedor) VALUES (?, ?)";
            PreparedStatement p1 = c.prepareStatement(sql);
            p1.setString(1, l.getNome());
            p1.setString(2, l.getVendedor() != null ? l.getVendedor().getUsername() : "");
            p1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Loja> consultar(String nome) {
        List<Loja> lista = new ArrayList<>();
        try {
            String sql = "SELECT id, nome, user_vendedor FROM loja WHERE nome LIKE ?";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, "%" + nome + "%");
            ResultSet rs = p.executeQuery();
            while (rs.next()) { 
                Loja l = new Loja();
                l.setId(rs.getLong("id"));
                l.setNome(rs.getString("nome"));
                Vendedor v = new Vendedor();
                v.setUsername(rs.getString("user_vendedor"));
                l.setVendedor(v);
                lista.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void atualizar(long id, Loja l) {
        try {
            String sql = "UPDATE loja SET nome = ? WHERE id = ?";
            PreparedStatement p1 = c.prepareStatement(sql);
            p1.setString(1, l.getNome());
            p1.setLong(2, id);
            p1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apagar(long id) {
        try {
            String sql = "DELETE FROM loja WHERE id = ?";
            PreparedStatement p1 = c.prepareStatement(sql);
            p1.setLong(1, id);
            p1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}