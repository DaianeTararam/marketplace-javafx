package edu.curso.persistence;

import java.util.List;

import edu.curso.model.Vendedor;

public interface VendedorDAO {
    public void cadastrar(Vendedor v);
    public Vendedor buscarPorUsername(String username);
    public List<Vendedor> listarTodos();
}