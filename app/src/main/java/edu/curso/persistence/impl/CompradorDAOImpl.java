package edu.curso.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.curso.model.Comprador;
import edu.curso.model.Endereco;
import edu.curso.model.Telefone;
import edu.curso.persistence.CompradorDAO;
import edu.curso.persistence.GenericDao;

public class CompradorDAOImpl implements CompradorDAO {

    private GenericDao gDao;
    private Connection c;

    public CompradorDAOImpl() {
        try {
            gDao = new GenericDao();
            c = gDao.getConnection();
            System.out.println("Conexao CompradorDAO estabelecida com sucesso.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Falha de conexao no CompradorDAO.");
            e.printStackTrace();
        }
    }

    @Override
    public void cadastrarCompleto(Comprador c) {
        try {
            String sqlTel = "IF NOT EXISTS (SELECT 1 FROM telefone WHERE ddd = ? AND numero = ?) " +
                            "INSERT INTO telefone (ddd, numero) VALUES (?, ?)";
            PreparedStatement psTel = this.c.prepareStatement(sqlTel);
            psTel.setString(1, c.getTelefone().getDdd());
            psTel.setString(2, c.getTelefone().getNumero());
            psTel.setString(3, c.getTelefone().getDdd());
            psTel.setString(4, c.getTelefone().getNumero());
            psTel.executeUpdate();

            String sqlEnd = "IF NOT EXISTS (SELECT 1 FROM endereco WHERE cep = ? AND numero = ? AND cidade = ?) " +
                            "INSERT INTO endereco (cep, numero, cidade, logradouro, complemento, bairro, estado) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psEnd = this.c.prepareStatement(sqlEnd);
            psEnd.setString(1, c.getEndereco().getCep());
            psEnd.setString(2, c.getEndereco().getNumero());
            psEnd.setString(3, c.getEndereco().getCidade());
            psEnd.setString(4, c.getEndereco().getCep());
            psEnd.setString(5, c.getEndereco().getNumero());
            psEnd.setString(6, c.getEndereco().getCidade());
            psEnd.setString(7, c.getEndereco().getLogradouro());
            psEnd.setString(8, c.getEndereco().getComplemento());
            psEnd.setString(9, c.getEndereco().getBairro());
            psEnd.setString(10, c.getEndereco().getEstado());
            psEnd.executeUpdate();

            // Insere na tabela usuario
            String sqlUsu = "INSERT INTO usuario (email, username, senha, nome) VALUES (?, ?, ?, ?)";
            PreparedStatement psUsu = this.c.prepareStatement(sqlUsu);
            psUsu.setString(1, c.getEmail());
            psUsu.setString(2, c.getUsername());
            psUsu.setString(3, c.getSenha());
            psUsu.setString(4, c.getNome());
            psUsu.executeUpdate();

            // Insere na tabela comprador
            String sqlComp = "INSERT INTO comprador (username, cep, numero, cidade, ddd, num_telefone) " +
                             "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psComp = this.c.prepareStatement(sqlComp);
            psComp.setString(1, c.getUsername());
            psComp.setString(2, c.getEndereco().getCep());
            psComp.setString(3, c.getEndereco().getNumero());
            psComp.setString(4, c.getEndereco().getCidade());
            psComp.setString(5, c.getTelefone().getDdd());
            psComp.setString(6, c.getTelefone().getNumero());
            psComp.executeUpdate();

            System.out.println("Comprador cadastrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar comprador.");
            e.printStackTrace();
        }
    }

    @Override
    public Comprador buscarPorUsername(String username) {
        String sql = "SELECT u.nome, u.email, u.senha, u.username, " +
                     " e.logradouro, e.numero, e.complemento, e.bairro, e.cidade, e.estado, e.cep, " +
                     "t.ddd, t.numero AS telefone " +
                     "FROM comprador comp " +
                     "INNER JOIN usuario u ON comp.username = u.username " +
                     "INNER JOIN endereco e ON comp.cep = e.cep " +
                     "AND comp.numero = e.numero " +
                     "AND comp.cidade = e.cidade " +
                     "INNER JOIN telefone t ON comp.ddd = t.ddd " +
                     "AND comp.num_telefone = t.numero " +
                     "WHERE comp.username = ?";
        try {
            PreparedStatement ps = this.c.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Comprador comp = new Comprador();
                comp.setUsername(rs.getString("username"));
                comp.setNome(rs.getString("nome"));
                comp.setEmail(rs.getString("email"));
                comp.setSenha(rs.getString("senha"));

                Endereco end = new Endereco();
                end.setLogradouro(rs.getString("logradouro"));
                end.setNumero(rs.getString("numero"));
                end.setComplemento(rs.getString("complemento"));
                end.setBairro(rs.getString("bairro"));
                end.setCidade(rs.getString("cidade"));
                end.setEstado(rs.getString("estado"));
                end.setCep(rs.getString("cep"));
                comp.setEndereco(end);

                Telefone tel = new Telefone();
                tel.setDdd(rs.getString("ddd"));
                tel.setNumero(rs.getString("telefone"));
                comp.setTelefone(tel);

                return comp;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar comprador.");
            e.printStackTrace();
        }
        return null;
    }
}