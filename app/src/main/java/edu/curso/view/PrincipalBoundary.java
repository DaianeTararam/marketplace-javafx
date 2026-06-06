package edu.curso.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PrincipalBoundary extends Application {

    private Pane paginaInicial = new PaginaInicialBoundary().render();
    private Pane vendedorBoundary = new VendedorBoundary().render();
    private Pane lojaBoundary = new LojaBoundary().render();
    private Pane produtoBoundary = new ProdutoBoundary().render();
    private Pane compradorBoundary = new CompradorBoundary().render();
    private Pane pedidoBoundary = new PedidoBoundary().render();
    private Pane historicoBoundary = new HistoricoBoundary().render();

    @Override
    public void start(Stage stage) {
        BorderPane panePrincipal = new BorderPane();
        Scene s = new Scene(panePrincipal, 900, 650);

        MenuBar menuBar = new MenuBar();

        Menu mnuInicio = new Menu("Página Inicial");
        Menu mnuCadastro = new Menu("Cadastro");

        menuBar.getMenus().addAll(mnuInicio, mnuCadastro);

        MenuItem mnuHome = new MenuItem("Ver produtos");
        MenuItem mnuVendedor = new MenuItem("Vendedores");
        MenuItem mnuLoja = new MenuItem("Lojas");
        MenuItem mnuProduto = new MenuItem("Produtos");
        MenuItem mnuComprador = new MenuItem("Compradores");
        MenuItem mnuPedido = new MenuItem("Novo pedido");
        MenuItem mnuHistorico = new MenuItem("Histórico de pedidos");

        mnuHome.setOnAction(e -> panePrincipal.setCenter(paginaInicial));
        mnuVendedor.setOnAction(e -> panePrincipal.setCenter(vendedorBoundary));
        mnuLoja.setOnAction(e -> panePrincipal.setCenter(lojaBoundary));
        mnuProduto.setOnAction(e -> panePrincipal.setCenter(produtoBoundary));
        mnuComprador.setOnAction(e -> panePrincipal.setCenter(compradorBoundary));
        mnuPedido.setOnAction(e -> panePrincipal.setCenter(pedidoBoundary));
        mnuHistorico.setOnAction(e -> panePrincipal.setCenter(historicoBoundary));

        mnuInicio.getItems().add(mnuHome);
        mnuCadastro.getItems().addAll(mnuVendedor, mnuLoja, mnuProduto, mnuComprador, mnuPedido, mnuHistorico);

        panePrincipal.setTop(menuBar);

        panePrincipal.setCenter(paginaInicial);

        stage.setTitle("ArteCraft — Marketplace");
        stage.setScene(s);
        stage.show();
    }
}