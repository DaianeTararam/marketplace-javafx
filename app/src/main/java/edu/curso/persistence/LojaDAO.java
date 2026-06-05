package edu.curso.persistence;

import java.util.List;

import edu.curso.model.Loja;

public interface LojaDAO {
    public void cadastrar(Loja l);
    public List<Loja> consultar(String nome);
    public void atualizar(long id, Loja l);
    public void apagar(long id);
}