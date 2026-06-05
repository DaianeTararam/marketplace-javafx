package edu.curso.view;

import edu.curso.controller.LojaControl;
import edu.curso.model.Loja;
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

public class LojaBoundary implements Tela{
    private ObservableList<String> tipos = 
        FXCollections.observableArrayList();
    
    private ComboBox<String> cmbTipo = new ComboBox<>();

    private TextField txtId = new TextField();
    private TextField txtNome = new TextField();
    private TextField txtUserVendedor= new TextField();

    
    
    private TableView<Loja> table = new TableView<>();
    
    private LojaControl control = new LojaControl();

    @Override
    public Pane render(){
        BorderPane bp = new BorderPane();
        GridPane pane = new GridPane();
        bp.setTop(pane);
        bp.setCenter(table);

        pane.add(new Label("ID"), 0, 0);
        pane.add(txtId, 1, 0);
        pane.add(new Label("Nome"), 0, 1);
        pane.add(txtNome, 1, 1);
        pane.add(new Label("Vendedor(Username)"), 0, 2);
        pane.add(txtUserVendedor, 1, 2);
       
        cmbTipo.setItems(tipos);

        Button btnPesquisar = new Button("Pesquisar");
        btnPesquisar.setOnAction((e) -> {control.pesquisar();});
        
        //Button btnAtualizar = new Button("Atualizar");
        //btnAtualizar.setOnAction((e) -> {control.();});
 
        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction((e) -> {
            control.salvar();
            control.limparCampos();
            new Alert(Alert.AlertType.INFORMATION, "Salva com Sucesso.").show();
        });

        Button btnLimpar = new Button("Limpar Campos");
        btnLimpar.setOnAction((e) ->{
            control.limparCampos();
        });

        pane.add(btnPesquisar, 0, 5);
        pane.add(btnSalvar, 1, 5);
        pane.add(btnLimpar, 2, 5);

        Bindings.bindBidirectional(txtId.textProperty(), control.getId());
        Bindings.bindBidirectional(txtNome.textProperty(), control.getNome());
        Bindings.bindBidirectional(txtUserVendedor.textProperty(), control.getVendedor());
        
        TableColumn<Loja, String> colId= new TableColumn<>("ID");
        colId.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(String.valueOf(item.getValue().getId()))
        );
        TableColumn<Loja, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getNome())
        );
        TableColumn<Loja, String> colVendedor = new TableColumn<>("Vendedor");
        colVendedor.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getVendedor().getUsername())
        );
        
        TableColumn<Loja, Void> colAcoes = new TableColumn<>("Ações");
        Callback<TableColumn<Loja, Void>, TableCell<Loja, Void>> callBack = new Callback<>() {
            public TableCell<Loja, Void> call(TableColumn<Loja, Void> param) {
                return new TableCell<Loja, Void>() {
                    private Button btnApagar = new Button("Excluir");
                    {
                        btnApagar.setOnAction((e) -> {
                        control.apagar(getIndex());
                        });   
                    };
                    @Override
                    public void updateItem(Void item, boolean empty) {
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
        
        table.setItems(control.getLista());
        
        table.getColumns().add(colId);
        table.getColumns().add(colNome);
        table.getColumns().add(colVendedor);
        table.getColumns().add(colAcoes);
         
        table.getSelectionModel().selectedItemProperty().addListener(
            (obj, antigo, novo) -> control.toBoundary(novo)
        );

        control.limparCampos();
        return bp;
    }
}
