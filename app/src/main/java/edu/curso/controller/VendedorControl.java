package edu.curso.controller;

import java.util.List;

import edu.curso.model.Vendedor;
import edu.curso.persistence.VendedorDAO;
import edu.curso.persistence.impl.VendedorDAOImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VendedorControl {

    private StringProperty username = new SimpleStringProperty("");
    private StringProperty cnpj = new SimpleStringProperty("");
    private StringProperty nome = new SimpleStringProperty("");
    private StringProperty email = new SimpleStringProperty("");
    private StringProperty senha = new SimpleStringProperty("");

    private ObservableList<Vendedor> listaVendedores = FXCollections.observableArrayList();

    private VendedorDAO vDao = new VendedorDAOImpl();

    public void salvar() {
        Vendedor v = new Vendedor();
        v.setUsername(username.get());
        v.setCnpj(cnpj.get());
        v.setNome(nome.get());
        v.setEmail(email.get());
        v.setSenha(senha.get());

        vDao.cadastrar(v);
        pesquisar();
        limparCampos();
    }

    public void pesquisar() {
        listaVendedores.clear();
        if (username.get() != null && !username.get().trim().isEmpty()) {
            Vendedor v = vDao.buscarPorUsername(username.get());
            if (v != null) {
                listaVendedores.add(v);
            }
        } else {
            List<Vendedor> todos = vDao.listarTodos();
            listaVendedores.addAll(todos);
        }
    }

    public void apagar(int index) {
        if (index >= 0 && index < listaVendedores.size()) {
            Vendedor v = listaVendedores.get(index);
            listaVendedores.remove(index);
            System.out.println("Vendedor removido da visualização: " + v.getUsername());
        }
    }

    public void toBoundary(Vendedor v) {
        if (v != null) {
            username.set(v.getUsername());
            cnpj.set(v.getCnpj());
            nome.set(v.getNome());
            email.set(v.getEmail());
            senha.set(v.getSenha());
        }
    }

    public void limparCampos() {
        username.set("");
        cnpj.set("");
        nome.set("");
        email.set("");
        senha.set("");
    }

    public StringProperty getUsername() { 
        return username; 
    }


    public StringProperty getCnpj(){ 
        return cnpj; 
    }

    public StringProperty getNome(){ 
        return nome; 
    }

    public StringProperty getEmail(){ 
        return email; 
    }
    
    public StringProperty getSenha(){ 
        return senha; 
    }

    public ObservableList<Vendedor> getLista(){
        return listaVendedores;
    }
}