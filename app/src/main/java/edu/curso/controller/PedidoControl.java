package edu.curso.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.curso.model.Comprador;
import edu.curso.model.ItemPedido;
import edu.curso.model.Pedido;
import edu.curso.model.Produto;
import edu.curso.persistence.PedidoDAO;
import edu.curso.persistence.impl.PedidoDAOImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PedidoControl {

    private StringProperty usernameComprador = new SimpleStringProperty("");
    private StringProperty senhaComprador = new SimpleStringProperty("");
    private StringProperty usernameVendedor = new SimpleStringProperty("");
    private StringProperty senhaVendedor = new SimpleStringProperty("");
    private StringProperty idProduto = new SimpleStringProperty("");
    private StringProperty quantidade = new SimpleStringProperty("");
    private ObservableList<ItemPedido> carrinho = FXCollections.observableArrayList();
    private ObservableList<Pedido> historico = FXCollections.observableArrayList();
    private PedidoDAO pDao = new PedidoDAOImpl();

    public void adicionarAoCarrinho(Produto produto, int qtd) {
        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(qtd);
        item.setValorUnitario(produto.getPreco());
        carrinho.add(item);
    }

    public void finalizarPedido() {
        if (carrinho.isEmpty()) return;
        double total = carrinho.stream()
                .mapToDouble(i -> i.getValorUnitario() * i.getQuantidade())
                .sum();
        Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");
        pedido.setValorTotal(total);
        Comprador c = new Comprador();
        c.setUsername(usernameComprador.get());
        pedido.setComprador(c);
        List<ItemPedido> itensCopia = new ArrayList<>(carrinho);
        pDao.finalizarVenda(pedido, itensCopia);
        carrinho.clear();
    }

    public void buscarHistoricoComprador() {
        historico.clear();
        List<Pedido> lista = pDao.buscarHistoricoComprador(usernameComprador.get(), senhaComprador.get());
        historico.addAll(lista);
    }

    public void buscarHistoricoVendedor() {
        historico.clear();
        List<Pedido> lista = pDao.buscarHistoricoVendedor(usernameVendedor.get(), senhaVendedor.get());
        historico.addAll(lista);
    }

    public void removerDoCarrinho(int index) {
        if (index >= 0 && index < carrinho.size()) {
            carrinho.remove(index);
        }
    }

    public void limparCampos() {
        usernameComprador.set("");
        senhaComprador.set("");
        usernameVendedor.set("");
        senhaVendedor.set("");
        idProduto.set("");
        quantidade.set("");
    }

    public StringProperty usernameCompradorProperty(){ 
        return usernameComprador;
    }

    public StringProperty senhaCompradorProperty(){ 
        return senhaComprador;
    }

    public StringProperty usernameVendedorProperty(){
        return usernameVendedor; 
    }

    public StringProperty senhaVendedorProperty(){ 
        return senhaVendedor; 
    }

    public StringProperty idProdutoProperty(){ 
        return idProduto; 
    }
    public StringProperty quantidadeProperty(){ 
        return quantidade; 
    }

    public ObservableList<ItemPedido> getCarrinho(){ 
        return carrinho; 
    }

    public ObservableList<Pedido> getHistorico(){ 
        return historico; 
    }
}