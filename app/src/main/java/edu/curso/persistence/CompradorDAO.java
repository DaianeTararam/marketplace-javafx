package edu.curso.persistence;

import edu.curso.model.Comprador;

public interface CompradorDAO {
    public void cadastrarCompleto(Comprador c);
    public Comprador buscarPorUsername(String username);
}