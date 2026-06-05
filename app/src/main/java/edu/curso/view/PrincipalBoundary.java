package edu.curso.view;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PrincipalBoundary extends Application {
    private Map<String, Tela> telas = new HashMap();
    private Pane vendedorBoundary = new VendedorBoundary().render();
    private Pane lojaBoundary = new LojaBoundary().render();
    private Pane produtoBoundary = new ProdutoBoundary().render();

    @Override
    public void start(Stage stage){
        BorderPane panePrincipal = new BorderPane();
        Scene s = new Scene(panePrincipal, 800, 600);
        
        MenuBar menuBar = new MenuBar();

        Menu mnuInicio  = new Menu("Página Inicial");
        Menu mnuCadastro = new Menu("Cadastro");
        Menu mnuAjuda    = new Menu("Ajuda");

        menuBar.getMenus().addAll(mnuInicio, mnuCadastro, mnuAjuda);

        MenuItem mnuVendedor  = new MenuItem("Vendedores");
        MenuItem mnuProduto   = new MenuItem("Produtos");
        MenuItem mnuComprador = new MenuItem("Compradores");
        MenuItem mnuPedido    = new MenuItem("Pedidos");
        MenuItem mnuLoja    = new MenuItem("Lojas");

        mnuVendedor.setOnAction(e  -> panePrincipal.setCenter(vendedorBoundary));
        mnuLoja.setOnAction(e  -> panePrincipal.setCenter(lojaBoundary));
        mnuProduto.setOnAction(e   -> panePrincipal.setCenter(produtoBoundary));
        //mnuComprador.setOnAction(e -> panePrincipal.setCenter(compradorPane));
        //mnuPedido.setOnAction(e    -> panePrincipal.setCenter(pedidoPane));

        mnuCadastro.getItems().addAll(mnuVendedor, mnuLoja, mnuProduto, mnuComprador, mnuPedido);
        mnuInicio.getItems().addAll();
        panePrincipal.setTop(menuBar);

        stage.setScene(s);
        stage.show();

    }
}
