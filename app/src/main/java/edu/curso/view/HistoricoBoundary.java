package edu.curso.view;

import edu.curso.controller.PedidoControl;
import edu.curso.model.Pedido;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class HistoricoBoundary implements Tela {

    private TextField txtUsername = new TextField();
    private TextField txtSenha = new TextField();
    private TableView<Pedido> tableHistorico = new TableView<>();
    private PedidoControl pControl = new PedidoControl();

    @Override
    public Pane render() {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(5);
        pane.add(new Label("Username"), 0, 0);
        pane.add(txtUsername, 1, 0);
        pane.add(new Label("Senha"), 2, 0);
        pane.add(txtSenha, 3, 0);
        Bindings.bindBidirectional(txtUsername.textProperty(), pControl.usernameCompradorProperty());
        Bindings.bindBidirectional(txtSenha.textProperty(), pControl.senhaCompradorProperty());

        Button btnComprador = new Button("Histórico como comprador");
        btnComprador.setOnAction(e -> pControl.buscarHistoricoComprador());
        Button btnVendedor = new Button("Histórico como vendedor");
        btnVendedor.setOnAction(e -> {
            pControl.usernameVendedorProperty().set(txtUsername.getText().trim());
            pControl.senhaVendedorProperty().set(txtSenha.getText().trim());
            pControl.buscarHistoricoVendedor();
        });
        Button btnLimpar = new Button("Limpar");
        btnLimpar.setOnAction(e -> {
            pControl.limparCampos();
            tableHistorico.getItems().clear();
        });

        TableColumn<Pedido, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(item -> 
            new ReadOnlyStringWrapper(String.valueOf(item.getValue().getId())));
        TableColumn<Pedido, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(item -> 
            new ReadOnlyStringWrapper(
                item.getValue().getDataPedido().toLocalDate().toString()));
        TableColumn<Pedido, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(item -> 
            new ReadOnlyStringWrapper(item.getValue().getStatus()));
        TableColumn<Pedido, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(item -> 
            new ReadOnlyStringWrapper(
                String.format("R$ %.2f", item.getValue().getValorTotal())));
        tableHistorico.setItems(pControl.getHistorico());
        tableHistorico.getColumns().addAll(colId, colData, colStatus, colTotal);

        VBox vbox = new VBox(8,
                pane,
                new HBox(10, btnComprador, btnVendedor, btnLimpar),
                new Label("Histórico de pedidos:"),
                tableHistorico
        );
        vbox.setStyle("-fx-padding: 10;");
        pControl.limparCampos();
        return vbox;
    }
}