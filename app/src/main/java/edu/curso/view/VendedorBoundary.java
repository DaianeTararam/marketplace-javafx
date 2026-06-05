package edu.curso.view;

import edu.curso.controller.VendedorControl;
import edu.curso.model.Vendedor;
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

public class VendedorBoundary implements Tela {
    private ObservableList<String> tipos = 
        FXCollections.observableArrayList("Produto", "Loja");
    
    private ComboBox<String> cmbTipo = new ComboBox<>();

    private TextField txtUsername = new TextField();
    private TextField txtCnpj = new TextField();
    private TextField txtNome = new TextField();
    private TextField txtEmail = new TextField();
    private TextField txtSenha = new TextField();
    
    
    private TableView<Vendedor> table = new TableView<>();
    
    private VendedorControl control = new VendedorControl();

    @Override
    public Pane render(){
        BorderPane bp = new BorderPane();
        GridPane pane = new GridPane();
        bp.setTop(pane);
        bp.setCenter(table);

        pane.add(new Label("Username"), 0, 0);
        pane.add(txtUsername, 1, 0);
        pane.add(new Label("CNPJ"), 0, 1);
        pane.add(txtCnpj, 1, 1);
        pane.add(new Label("Nome"), 0, 2);
        pane.add(txtNome, 1, 2);
        pane.add(new Label("Email"), 0, 3);
        pane.add(txtEmail, 1, 3);
        pane.add(new Label("Senha"), 0, 4);
        pane.add(txtSenha, 1, 4);
     
        cmbTipo.setItems(tipos);

        Button btnPesquisar = new Button("Pesquisar");
        btnPesquisar.setOnAction((e) -> {control.pesquisar();});
        
        //Button btnAtualizar = new Button("Atualizar");
        //btnAtualizar.setOnAction((e) -> {control.();});
 
        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction((e) -> {
            control.salvar();
            control.limparCampos();
            new Alert(Alert.AlertType.INFORMATION, "Salvo com Sucesso.").show();
        });

        Button btnLimpar = new Button("Limpar Campos");
        btnLimpar.setOnAction((e) ->{
            control.limparCampos();
        });

        
        

        pane.add(btnPesquisar, 0, 5);
        pane.add(btnSalvar, 1, 5);
        pane.add(btnLimpar, 2, 5);

        Bindings.bindBidirectional(txtUsername.textProperty(), control.getUsername());
        Bindings.bindBidirectional(txtCnpj.textProperty(), control.getCnpj());
        Bindings.bindBidirectional(txtNome.textProperty(), control.getNome());
        Bindings.bindBidirectional(txtEmail.textProperty(), control.getEmail());
        Bindings.bindBidirectional(txtSenha.textProperty(), control.getSenha());
        
        TableColumn<Vendedor, String> colUsername = new TableColumn<>("Username");
        colUsername.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getUsername())
        );
        TableColumn<Vendedor, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getNome())
        );
        TableColumn<Vendedor, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getEmail())
        );
        TableColumn<Vendedor, String> colCnpj = new TableColumn<>("CNPJ");
        colCnpj.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getCnpj())
        );
        
        TableColumn<Vendedor, Void> colAcoes = new TableColumn<>("Ações");
        Callback<TableColumn<Vendedor, Void>, TableCell<Vendedor, Void>> callBack = new Callback<>() {
            public TableCell<Vendedor, Void> call(TableColumn<Vendedor, Void> param) {
                return new TableCell<Vendedor, Void>() {
                    private Button btnApagar = new Button("Excluir");
                    {
                        btnApagar.setOnAction((e) -> {
                        control.apagar(getIndex());
                        });   
                    };
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
        
        table.getColumns().add(colUsername);
        table.getColumns().add(colNome);
        table.getColumns().add(colEmail);
        table.getColumns().add(colCnpj);
        table.getColumns().add(colAcoes);
 
        
        table.getSelectionModel().selectedItemProperty().addListener(
            (obj, antigo, novo) -> control.toBoundary(novo)
        );

        control.limparCampos();
        return bp;

    }

}
