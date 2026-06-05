package edu.curso.persistence;

import java.util.List;

import edu.curso.model.Produto;

public interface ProdutoDAO {
    public void cadastrar(Produto p);
    public List<Produto> consultar(String nome);
    public void atualizar(long id, Produto p);
    public void apagar(long id);               
}