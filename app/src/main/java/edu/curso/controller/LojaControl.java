package edu.curso.controller;

import java.util.List;

import edu.curso.model.Loja;
import edu.curso.model.Vendedor;
import edu.curso.persistence.LojaDAO;
import edu.curso.persistence.impl.LojaDAOImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LojaControl {

    private StringProperty id = new SimpleStringProperty("");
    private StringProperty nome = new SimpleStringProperty("");
    private StringProperty vendedor = new SimpleStringProperty(""); // Guarda o username do vendedor

    private ObservableList<Loja> listaLojas = FXCollections.observableArrayList();

    private LojaDAO lDao = new LojaDAOImpl();

    public void salvar() {
        Loja l = new Loja();
        l.setNome(nome.get());
        
        Vendedor v = new Vendedor();
        v.setUsername(vendedor.get());
        l.setVendedor(v);

        if (id.get() != null && !id.get().trim().isEmpty()) {
            try {
                long idLong = Long.parseLong(id.get());
                lDao.atualizar(idLong, l);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido para atualização.");
            }
        } else {
            lDao.cadastrar(l);
        }

        pesquisar();
        limparCampos();
    }

    public void pesquisar() {
        listaLojas.clear();
        List<Loja> encontradas = lDao.consultar(nome.get());
        listaLojas.addAll(encontradas);
    }

    public void apagar(int index) {
        if (index >= 0 && index < listaLojas.size()) {
            Loja l = listaLojas.get(index);
            lDao.apagar(l.getId());
            listaLojas.remove(index);
            System.out.println("Loja excluída do banco e da tela: " + l.getNome());
        }
    }

    public void toBoundary(Loja l) {
        if (l != null) {
            id.set(String.valueOf(l.getId()));
            nome.set(l.getNome());
            if (l.getVendedor() != null) {
                vendedor.set(l.getVendedor().getUsername());
            } else {
                vendedor.set("");
            }
        }
    }

    public void limparCampos() {
        id.set("");
        nome.set("");
        vendedor.set("");
    }

    public StringProperty getId() { 
        return id; 
    }
    public StringProperty getNome() { 
        return nome; 
    }
    public StringProperty getVendedor() { 
        return vendedor; 
    }

    public ObservableList<Loja> getLista() {
        return listaLojas;
    }
}