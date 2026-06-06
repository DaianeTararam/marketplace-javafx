package edu.curso.view;

import edu.curso.controller.ProdutoControl;
import edu.curso.model.Produto;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class ProdutoBoundary implements Tela {

    private TextField txtNome = new TextField();
    private TextField txtPreco = new TextField();
    private TextField txtDescricao = new TextField();
    private ComboBox<String> cmbCategoria = new ComboBox<>();
    private TextField txtIdLoja = new TextField();

    private TableView<Produto> table = new TableView<>();
    private ProdutoControl control = new ProdutoControl();

    @Override
    public Pane render() {
        BorderPane bp   = new BorderPane();
        GridPane   pane = new GridPane();
        bp.setTop(pane);
        bp.setCenter(table);

        cmbCategoria.getItems().addAll(
            "Crochê", "Tricô", "Bordado", "Pintura",
            "Cerâmica", "Macramê", "Bijuteria", "Outros"
        );
        cmbCategoria.setValue("Crochê");

        pane.add(new Label("Nome do Produto"), 0, 0);
        pane.add(txtNome,      1, 0);
        pane.add(new Label("Preço (R$)"),  0, 1);
        pane.add(txtPreco,     1, 1);
        pane.add(new Label("Descrição"),   0, 2);
        pane.add(txtDescricao, 1, 2);
        pane.add(new Label("Categoria"),   0, 3);
        pane.add(cmbCategoria, 1, 3);           // ← ComboBox no lugar do TextField
        pane.add(new Label("ID da Loja"),  0, 4);
        pane.add(txtIdLoja,    1, 4);

        Button btnPesquisar = new Button("Pesquisar");
        btnPesquisar.setOnAction(e -> control.pesquisar());

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            control.categoriaProperty().set(cmbCategoria.getValue()); 
            control.salvar();
            control.limparCampos();
            cmbCategoria.setValue("Crochê");
            new Alert(Alert.AlertType.INFORMATION, "Produto salvo com sucesso.").show();
        });

        Button btnLimpar = new Button("Limpar Campos");
        btnLimpar.setOnAction(e -> {
            control.limparCampos();
            cmbCategoria.setValue("Crochê");
        });

        pane.add(btnPesquisar, 0, 5);
        pane.add(btnSalvar, 1, 5);
        pane.add(btnLimpar, 2, 5);

        Bindings.bindBidirectional(txtNome.textProperty(), control.nomeProperty());
        Bindings.bindBidirectional(txtPreco.textProperty(), control.precoProperty());
        Bindings.bindBidirectional(txtDescricao.textProperty(), control.descricaoProperty());
        Bindings.bindBidirectional(txtIdLoja.textProperty(), control.idLojaProperty());

        TableColumn<Produto, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(item -> 
            new ReadOnlyStringWrapper(item.getValue().getNome()));

        TableColumn<Produto, String> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(item ->
            new ReadOnlyStringWrapper(String.format("R$ %.2f", item.getValue().getPreco())));

        TableColumn<Produto, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(item ->
            new ReadOnlyStringWrapper(item.getValue().getCategoria()));

        TableColumn<Produto, String> colLoja = new TableColumn<>("ID da Loja");
        colLoja.setCellValueFactory(item ->
            new ReadOnlyStringWrapper(item.getValue().getLoja() != null
                ? String.valueOf(item.getValue().getLoja().getId()) : ""));

        TableColumn<Produto, Void> colAcoes = new TableColumn<>("Ações");
        Callback<TableColumn<Produto, Void>, TableCell<Produto, Void>> cb = new Callback<>() {
            @Override 
            public TableCell<Produto, Void> call(TableColumn<Produto, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Excluir");
                    { btn.setOnAction(e -> {
                        Produto p = getTableView().getItems().get(getIndex());
                        control.apagar(p.getId());
                    }); }
                    @Override
                    public void updateItem(Void v, boolean empty) {
                        super.updateItem(v, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        };
        colAcoes.setCellFactory(cb);

        table.setItems(control.getListaProdutos());
        table.getColumns().addAll(colNome, colPreco, colCategoria, colLoja, colAcoes);

        table.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                control.nomeProperty().set(novo.getNome());
                control.precoProperty().set(String.valueOf(novo.getPreco()));
                control.descricaoProperty().set(novo.getDescricao());
                control.categoriaProperty().set(novo.getCategoria());
                cmbCategoria.setValue(novo.getCategoria());
                control.idLojaProperty().set(novo.getLoja() != null
                    ? String.valueOf(novo.getLoja().getId()) : "");
            }
        });

        control.limparCampos();
        return bp;
    }
}