package edu.curso.view;

import java.util.HashMap;
import java.util.Map;

import edu.curso.controller.PedidoControl;
import edu.curso.controller.ProdutoControl;
import edu.curso.model.ItemPedido;
import edu.curso.model.Produto;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PedidoBoundary implements Tela {

    private TextField txtUsername = new TextField();
    private TextField txtSenha = new TextField();
    private TableView<Produto> tableProdutos = new TableView<>();
    private TableView<ItemPedido> tableCarrinho = new TableView<>();
    private PedidoControl pControl = new PedidoControl();
    private ProdutoControl prControl = new ProdutoControl();

    private final Map<Integer, TextField> qtdMap = new HashMap<>();

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

        TableColumn<Produto, String> colNome = new TableColumn<>("Produto");
        colNome.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getNome()));
        colNome.setPrefWidth(180);
        TableColumn<Produto, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getCategoria()));
        colCategoria.setPrefWidth(100);
        TableColumn<Produto, String> colLoja = new TableColumn<>("Loja");
        colLoja.setCellValueFactory(item -> new ReadOnlyStringWrapper(
                item.getValue().getLoja() != null ? item.getValue().getLoja().getNome() : ""));
        colLoja.setPrefWidth(120);
        TableColumn<Produto, String> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(item -> new ReadOnlyStringWrapper(
                String.format("R$ %.2f", item.getValue().getPreco())));
        colPreco.setPrefWidth(80);

        TableColumn<Produto, Void> colQtd = new TableColumn<>("Quantidade");
        colQtd.setPrefWidth(90);
        colQtd.setCellFactory(col -> new TableCell<>() {
            private final TextField txt = new TextField("1");
            { txt.setPrefWidth(70); }
            @Override 
            public void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                if (empty) {
                    setGraphic(null);
                    qtdMap.remove(getIndex());
                } else {
                    qtdMap.put(getIndex(), txt);
                    setGraphic(txt);
                }
            }
        });

        tableProdutos.getColumns().addAll(colNome, colCategoria, colLoja, colPreco, colQtd);
        tableProdutos.setItems(prControl.getListaProdutos());
        tableProdutos.setPrefHeight(220);
        prControl.nomeProperty().set("");
        prControl.pesquisar();

        Button btnAdd = new Button("Adicionar ao carrinho");
        btnAdd.setOnAction(e -> {
            int index = tableProdutos.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                new Alert(Alert.AlertType.WARNING, "Selecione um produto na tabela.").show();
                return;
            }
            Produto prod = tableProdutos.getItems().get(index);
            TextField qtdField = qtdMap.get(index);
            try {
                int qtd = Integer.parseInt(qtdField != null ? qtdField.getText().trim() : "");
                if (qtd <= 0) throw new NumberFormatException();
                pControl.adicionarAoCarrinho(prod, qtd);
                if (qtdField != null) qtdField.setText("1");
                tableProdutos.getSelectionModel().clearSelection();
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Informe uma quantidade válida.").show();
            }
        });

        Button btnFinalizar = new Button("Finalizar pedido");
        btnFinalizar.setOnAction(e -> {
            if (txtUsername.getText().trim().isEmpty() || txtSenha.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Informe username e senha para finalizar o pedido.").show();
                return;
            }
            pControl.finalizarPedido();
            new Alert(Alert.AlertType.INFORMATION, "Pedido finalizado com sucesso!").show();
        });
        Button btnLimpar = new Button("Limpar carrinho");
        btnLimpar.setOnAction(e -> pControl.limparCampos());

        TableColumn<ItemPedido, String> colProd = new TableColumn<>("Produto");
        colProd.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getProduto().getNome()));
        TableColumn<ItemPedido, String> colQtdC = new TableColumn<>("Qtd");
        colQtdC.setCellValueFactory(item -> new ReadOnlyStringWrapper(String.valueOf(item.getValue().getQuantidade())));
        TableColumn<ItemPedido, String> colValor = new TableColumn<>("Valor unit.");
        colValor.setCellValueFactory(item -> new ReadOnlyStringWrapper(
                String.format("R$ %.2f", item.getValue().getValorUnitario())));
        TableColumn<ItemPedido, String> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(item -> new ReadOnlyStringWrapper(
                String.format("R$ %.2f", item.getValue().getValorUnitario() * item.getValue().getQuantidade())));
        TableColumn<ItemPedido, Void> colRemover = new TableColumn<>("Remover");
        colRemover.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("X");
            { btn.setOnAction(e -> pControl.removerDoCarrinho(getIndex())); }
            @Override public void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });
        tableCarrinho.setItems(pControl.getCarrinho());
        tableCarrinho.getColumns().addAll(colProd, colQtdC, colValor, colSubtotal, colRemover);
        tableCarrinho.setPrefHeight(180);

        VBox vbox = new VBox(8,
                new Label("Produtos disponíveis:"),
                tableProdutos,
                new HBox(10, btnAdd, btnFinalizar, btnLimpar),
                new Label("Carrinho atual:"),
                tableCarrinho,
                pane, btnFinalizar
        );
        vbox.setStyle("-fx-padding: 10;");
        pControl.limparCampos();
        return vbox;
    }
}