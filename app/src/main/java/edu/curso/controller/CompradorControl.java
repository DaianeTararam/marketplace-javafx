package edu.curso.controller;

import edu.curso.model.Comprador;
import edu.curso.model.Endereco;
import edu.curso.model.Telefone;
import edu.curso.persistence.CompradorDAO;
import edu.curso.persistence.impl.CompradorDAOImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CompradorControl {

    private StringProperty nome = new SimpleStringProperty("");
    private StringProperty email = new SimpleStringProperty("");
    private StringProperty username = new SimpleStringProperty("");
    private StringProperty senha = new SimpleStringProperty("");

    private StringProperty logradouro = new SimpleStringProperty("");
    private StringProperty numero = new SimpleStringProperty("");
    private StringProperty complemento = new SimpleStringProperty("");
    private StringProperty bairro = new SimpleStringProperty("");
    private StringProperty city = new SimpleStringProperty(""); // alterado para evitar conflito com classe
    private StringProperty estado = new SimpleStringProperty("");
    private StringProperty cep = new SimpleStringProperty("");

    private StringProperty ddd = new SimpleStringProperty("");
    private StringProperty telNumero = new SimpleStringProperty("");

    private ObservableList<Comprador> listaCompradores = FXCollections.observableArrayList();
    private CompradorDAO cDao = new CompradorDAOImpl();

    public void salvar() {
        Comprador c = new Comprador();
        c.setNome(nome.get());
        c.setEmail(email.get());
        c.setUsername(username.get());
        c.setSenha(senha.get());

        Endereco end = new Endereco();
        end.setLogradouro(logradouro.get());
        end.setNumero(numero.get());
        end.setComplemento(complemento.get());
        end.setBairro(bairro.get());
        end.setCidade(city.get());
        end.setEstado(estado.get());
        end.setCep(cep.get());
        c.setEndereco(end);

        Telefone tel = new Telefone();
        tel.setDdd(ddd.get());
        tel.setNumero(telNumero.get());
        c.setTelefone(tel);

        cDao.cadastrarCompleto(c);
        pesquisar();
        limparCampos();
    }

    public void pesquisar() {
        listaCompradores.clear();
        if (username.get() != null && !username.get().trim().isEmpty()) {
            Comprador c = cDao.buscarPorUsername(username.get());
            if (c != null) {
                listaCompradores.add(c);
            }
        }
    }

    public void toBoundary(Comprador c) {
        if (c != null) {
            nome.set(c.getNome());
            email.set(c.getEmail());
            username.set(c.getUsername());
            senha.set(c.getSenha());

            if (c.getEndereco() != null) {
                logradouro.set(c.getEndereco().getLogradouro());
                numero.set(c.getEndereco().getNumero());
                complemento.set(c.getEndereco().getComplemento());
                bairro.set(c.getEndereco().getBairro());
                city.set(c.getEndereco().getCidade());
                estado.set(c.getEndereco().getEstado());
                cep.set(c.getEndereco().getCep());
            }

            if (c.getTelefone() != null) {
                ddd.set(c.getTelefone().getDdd());
                telNumero.set(c.getTelefone().getNumero());
            }
        }
    }

    public void limparCampos() {
        nome.set("");
        email.set("");
        username.set("");
        senha.set("");
        logradouro.set("");
        numero.set("");
        complemento.set("");
        bairro.set("");
        city.set("");
        estado.set("");
        cep.set("");
        ddd.set("");
        telNumero.set("");
    }

    public StringProperty nomeProperty(){ 
        return nome;
    }

    public StringProperty emailProperty(){
        return email;

    }
    public StringProperty usernameProperty(){ 
        return username; 
    }

    public StringProperty senhaProperty(){ 
        return senha;
    }

    public StringProperty logradouroProperty() {
        return logradouro;
    }

    public StringProperty numeroProperty(){
        return numero; 
    }

    public StringProperty complementoProperty(){ 
        return complemento; 
    }

    public StringProperty bairroProperty(){ 
        return bairro;
    }

    public StringProperty cidadeProperty(){ 
        return city; 
    }

    public StringProperty estadoProperty(){
        return estado; 
    }

    public StringProperty cepProperty(){
        return cep;
    }

    public StringProperty dddProperty(){
        return ddd;
    }

    public StringProperty telNumeroProperty(){ 
        return telNumero;
    }

    public ObservableList<Comprador> getLista(){ 
        return listaCompradores;
    }
}