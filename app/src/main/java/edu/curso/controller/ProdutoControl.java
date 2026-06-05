package edu.curso.controller;

import java.util.List;

import edu.curso.model.Loja;
import edu.curso.model.Produto;
import edu.curso.persistence.ProdutoDAO;
import edu.curso.persistence.impl.ProdutoDAOImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProdutoControl {

    private StringProperty nome = new SimpleStringProperty("");
    private StringProperty preco = new SimpleStringProperty(""); 
    private StringProperty descricao = new SimpleStringProperty("");
    private StringProperty categoria = new SimpleStringProperty("");
    private StringProperty idLoja = new SimpleStringProperty(""); 

    private ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

    // Sua variável oficial padrão!
    private ProdutoDAO pDao = new ProdutoDAOImpl();

    public void salvar() {
        Produto p = new Produto();
        p.setNome(nome.get());
        p.setDescricao(descricao.get());
        p.setCategoria(categoria.get());
        try {
            p.setPreco(Double.parseDouble(preco.get()));
        } catch (NumberFormatException e) {
            p.setPreco(0.0);
        }
        
        try {
            Loja loja = new Loja();
            loja.setId(Long.parseLong(idLoja.get())); // Perfeito com a sua entidade long!
            p.setLoja(loja);
        } catch (NumberFormatException e) {
            System.out.println("ID da Loja inválido.");
            return;
        }
        
        pDao.cadastrar(p);
        limparCampos();
        pesquisar();
    }
    public void apagar(long id) {
        pDao.apagar(id);
        pesquisar();
    }

    public void pesquisar() {
        listaProdutos.clear();
        List<Produto> encontrados = pDao.consultar(nome.get());
        listaProdutos.addAll(encontrados);
    }

    public void limparCampos() {
        nome.set("");
        preco.set("");
        descricao.set("");
        categoria.set("");
        idLoja.set("");
    }

    public StringProperty nomeProperty() { 
        return nome; 
    }
    public StringProperty precoProperty(){ 
        return preco; 
    }
    public StringProperty descricaoProperty(){ 
        return descricao; 
    }
    public StringProperty categoriaProperty(){ 
        return categoria; 
    }
    public StringProperty idLojaProperty(){ 
        return idLoja; }

    public ObservableList<Produto> getListaProdutos() {
        return listaProdutos;
    }
}