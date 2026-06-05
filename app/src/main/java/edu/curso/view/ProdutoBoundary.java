package edu.curso.view;

import edu.curso.controller.ProdutoControl;
import edu.curso.model.Produto;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ObservableList<String> tipos = 
        FXCollections.observableArrayList();
    
    private ComboBox<String> cmbTipo = new ComboBox<>();

    private TextField txtNome = new TextField();
    private TextField txtPreco = new TextField();
    private TextField txtDescricao = new TextField();
    private TextField txtCategoria = new TextField();
    private TextField txtIdLoja = new TextField(); 
    
    private TableView<Produto> table = new TableView<>();
    
    private ProdutoControl control = new ProdutoControl();

    @Override
    public Pane render(){
        BorderPane bp = new BorderPane();
        GridPane pane = new GridPane();
        bp.setTop(pane);
        bp.setCenter(table);

        pane.add(new Label("Nome do Produto"), 0, 0);
        pane.add(txtNome, 1, 0);
        pane.add(new Label("Preço (R$)"), 0, 1);
        pane.add(txtPreco, 1, 1);
        pane.add(new Label("Descrição"), 0, 2);
        pane.add(txtDescricao, 1, 2);
        pane.add(new Label("Categoria"), 0, 3);
        pane.add(txtCategoria, 1, 3);
        pane.add(new Label("ID da Loja"), 0, 4);
        pane.add(txtIdLoja, 1, 4);
     
        cmbTipo.setItems(tipos);

        Button btnPesquisar = new Button("Pesquisar");
        btnPesquisar.setOnAction((e) -> { control.pesquisar(); });
 
        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction((e) -> {
            control.salvar();
            control.limparCampos();
            new Alert(Alert.AlertType.INFORMATION, "Produto salvo com sucesso.").show();
        });

        Button btnLimpar = new Button("Limpar Campos");
        btnLimpar.setOnAction((e) -> {
            control.limparCampos();
        });

        pane.add(btnPesquisar, 0, 5);
        pane.add(btnSalvar, 1, 5);
        pane.add(btnLimpar, 2, 5);

        Bindings.bindBidirectional(txtNome.textProperty(), control.nomeProperty());
        Bindings.bindBidirectional(txtPreco.textProperty(), control.precoProperty());
        Bindings.bindBidirectional(txtDescricao.textProperty(), control.descricaoProperty());
        Bindings.bindBidirectional(txtCategoria.textProperty(), control.categoriaProperty());
        Bindings.bindBidirectional(txtIdLoja.textProperty(), control.idLojaProperty());
        
        TableColumn<Produto, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getNome())
        );
        TableColumn<Produto, String> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(String.format("R$ %.2f", item.getValue().getPreco()))
        );
        TableColumn<Produto, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getCategoria())
        );
        TableColumn<Produto, String> colLoja = new TableColumn<>("ID da Loja");
        colLoja.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(
                item.getValue().getLoja() != null ? String.valueOf(item.getValue().getLoja().getId()) : ""
            )
        );
        
        TableColumn<Produto, Void> colAcoes = new TableColumn<>("Ações");
        Callback<TableColumn<Produto, Void>, TableCell<Produto, Void>> callBack = new Callback<> () {
            @Override
            public TableCell<Produto, Void> call(TableColumn<Produto, Void> param) {
                return new TableCell<Produto, Void>() {
                    private Button btnApagar = new Button("Excluir");
                    {
                        btnApagar.setOnAction((e) -> {
                            Produto p = getTableView().getItems().get(getIndex());
                            control.apagar(p.getId());
                        });   
                    };
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            setGraphic(btnApagar); 
                        } else {
                            setGraphic(null); 
                        }
                    }
                };
            }
        };
        colAcoes.setCellFactory(callBack);
        
        table.setItems(control.getListaProdutos());
        
        table.getColumns().add(colNome);
        table.getColumns().add(colPreco);
        table.getColumns().add(colCategoria);
        table.getColumns().add(colLoja);
        table.getColumns().add(colAcoes);
 
        table.getSelectionModel().selectedItemProperty().addListener(
            (obj, antigo, novo) -> {
                if (novo != null) {
                    control.nomeProperty().set(novo.getNome());
                    control.precoProperty().set(String.valueOf(novo.getPreco()));
                    control.descricaoProperty().set(novo.getDescricao());
                    control.categoriaProperty().set(novo.getCategoria());
                    if (novo.getLoja() != null) {
                        control.idLojaProperty().set(String.valueOf(novo.getLoja().getId()));
                    } else {
                        control.idLojaProperty().set("");
                    }
                }
            }
        );

        control.limparCampos();
        return bp;
    }
}