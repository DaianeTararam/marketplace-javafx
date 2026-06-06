package edu.curso.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.curso.model.Vendedor;
import edu.curso.persistence.GenericDao;
import edu.curso.persistence.VendedorDAO;

public class VendedorDAOImpl implements VendedorDAO {
    private GenericDao gDao;
    private Connection c;

    public VendedorDAOImpl() {
        System.out.println("Vendedor DAO criado com database em SQL Server");
        try {
            gDao = new GenericDao();
            c = gDao.getConnection();
            System.out.println("Conexao do Vendedor estabelecida com sucesso!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Falha de conexao no Vendedor...");
            e.printStackTrace();
        }
    }

    @Override
    public void cadastrar(Vendedor v) {
        try {
            String sqlUsuario = "INSERT INTO usuario (email, username, senha, nome) VALUES (?, ?, ?, ?)";
            PreparedStatement p1 = c.prepareStatement(sqlUsuario);
            p1.setString(1, v.getEmail());
            p1.setString(2, v.getUsername());
            p1.setString(3, v.getSenha());
            p1.setString(4, v.getNome());
            p1.executeUpdate();

            String sqlVendedor = "INSERT INTO vendedor (username, cnpj) VALUES (?, ?)";
            PreparedStatement p2 = c.prepareStatement(sqlVendedor);
            p2.setString(1, v.getUsername());
            p2.setString(2, v.getCnpj());
            p2.executeUpdate();

            System.out.println("Vendedor cadastrado com sucesso (Usuario + Vendedor)!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar vendedor.");
            e.printStackTrace();
        }
    }

    @Override
    public Vendedor buscarPorUsername(String username) {
        Vendedor v = null;
        try {
            String sql = "SELECT u.nome, u.email, u.senha, v.username, v.cnpj " +
                         "FROM vendedor v INNER JOIN usuario u ON v.username = u.username " +
                         "WHERE v.username = ?";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, username);
            ResultSet rs = p.executeQuery();
            
            if (rs.next()) {
                v = new Vendedor();
                v.setUsername(rs.getString("username"));
                v.setCnpj(rs.getString("cnpj"));
                v.setEmail(rs.getString("email"));
                v.setSenha(rs.getString("senha"));
                v.setNome(rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar vendedor por username.");
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public List<Vendedor> listarTodos() {
        List<Vendedor> lista = new ArrayList<>();
        try {
            String sql = "SELECT u.nome, u.email, u.senha, v.username, v.cnpj " +
                         "FROM vendedor v INNER JOIN usuario u ON v.username = u.username";
            PreparedStatement p = c.prepareStatement(sql);
            ResultSet rs = p.executeQuery();
            
            while (rs.next()) {
                Vendedor v = new Vendedor();
                v.setUsername(rs.getString("username"));
                v.setCnpj(rs.getString("cnpj"));
                v.setEmail(rs.getString("email"));
                v.setSenha(rs.getString("senha"));
                v.setNome(rs.getString("nome"));
                lista.add(v);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar todos os vendedores.");
            e.printStackTrace();
        }
        return lista;
    }
}