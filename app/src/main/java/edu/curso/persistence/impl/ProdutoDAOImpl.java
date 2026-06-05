package edu.curso.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.curso.model.Produto;
import edu.curso.model.Loja;
import edu.curso.persistence.GenericDao;
import edu.curso.persistence.ProdutoDAO;

public class ProdutoDAOImpl implements ProdutoDAO {
    private GenericDao gDao;
    private Connection c;

    public ProdutoDAOImpl() {
        System.out.println("Produto DAO criado com database em SQL Server");
        try {
            gDao = new GenericDao();
            c = gDao.getConnection();
            System.out.println("Conexão do Produto estabelecida com sucesso!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Falha de conexão no Produto...");
            e.printStackTrace();
        }
    }

    @Override
    public void cadastrar(Produto p) {
        try {
            String sql = "INSERT INTO produto (nome, preco, descricao, categoria, id_loja) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement p1 = c.prepareStatement(sql);
            p1.setString(1, p.getNome());
            p1.setDouble(2, p.getPreco());
            p1.setString(3, p.getDescricao());
            p1.setString(4, p.getCategoria());
            p1.setLong(5, p.getLoja().getId());
            p1.executeUpdate();

            System.out.println("Produto cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar produto.");
            e.printStackTrace();
        }
    }

    @Override
    public List<Produto> consultar(String nome) {
        List<Produto> lista = new ArrayList<>();
        try {
            // INNER JOIN para cumprir a exigência do professor de junção de tabelas!
            String sql = "SELECT p.*, l.nome AS nome_loja FROM produto p " +
                         "INNER JOIN loja l ON p.id_loja = l.id " +
                         "WHERE p.nome LIKE ?";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, "%" + nome + "%"); // Permite buscar por partes do nome
            ResultSet rs = p.executeQuery();
            
            while (rs.next()) {
                Produto prod = new Produto();
                prod.setId(rs.getLong("id"));
                prod.setNome(rs.getString("nome"));
                prod.setPreco(rs.getDouble("preco"));
                prod.setDescricao(rs.getString("descricao"));
                prod.setCategoria(rs.getString("categoria"));

                Loja loja = new Loja();
                loja.setId(rs.getLong("id_loja"));
                loja.setNome(rs.getString("nome_loja"));
                prod.setLoja(loja);

                lista.add(prod);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar produtos.");
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void atualizar(long id, Produto p) {
        try {
            String sql = "UPDATE produto SET nome = ?, preco = ?, descricao = ?, categoria = ?, id_loja = ? WHERE id = ?";
            PreparedStatement p1 = c.prepareStatement(sql);
            p1.setString(1, p.getNome());
            p1.setDouble(2, p.getPreco());
            p1.setString(3, p.getDescricao());
            p1.setString(4, p.getCategoria());
            p1.setLong(5, p.getLoja().getId());
            p1.setLong(6, id);
            p1.executeUpdate();

            System.out.println("Produto atualizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar produto.");
            e.printStackTrace();
        }
    }

    @Override
    public void apagar(long id) {
        try {
            String sql = "DELETE FROM produto WHERE id = ?";
            PreparedStatement p1 = c.prepareStatement(sql);
            p1.setLong(1, id);
            p1.executeUpdate();

            System.out.println("Produto apagado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao apagar produto.");
            e.printStackTrace();
        }
    }
}