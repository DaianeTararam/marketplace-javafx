package edu.curso.view;

import edu.curso.controller.CompradorControl;
import edu.curso.model.Comprador;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class CompradorBoundary implements Tela {

    private TextField txtNome = new TextField();
    private TextField txtEmail = new TextField();
    private TextField txtUsername = new TextField();
    private TextField txtSenha = new TextField();

    private TextField txtLogradouro = new TextField();
    private TextField txtNumero = new TextField();
    private TextField txtComplemento = new TextField();
    private TextField txtBairro = new TextField();
    private TextField txtCidade = new TextField();
    private TextField txtEstado = new TextField();
    private TextField txtCep = new TextField();

    private TextField txtDdd = new TextField();
    private TextField txtTelNumero = new TextField();

    private TableView<Comprador> table = new TableView<>();
    private CompradorControl control = new CompradorControl();

    @Override
    public Pane render() {
        BorderPane bp = new BorderPane();
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(5);
        
        bp.setTop(pane);
        bp.setCenter(table);

        Label lblDadosComp = new Label("Dados Pessoais");
        lblDadosComp.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 16 16 4 16;");
        Label lblEndComp = new Label("Endereço");
        lblEndComp.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 16 16 4 16;");
        

        pane.add(lblDadosComp, 0, 0, 2, 1);
        pane.add(new Label("Username (ID)"), 0, 1);
        pane.add(txtUsername, 1, 1);
        pane.add(new Label("Nome"), 0, 2);
        pane.add(txtNome, 1, 2);
        pane.add(new Label("Email"), 0, 3);
        pane.add(txtEmail, 1, 3);
        pane.add(new Label("Senha"), 0, 4);
        pane.add(txtSenha, 1, 4);

        pane.add(new Label("DDD"), 0, 5);
        pane.add(txtDdd, 1, 5);
        pane.add(new Label("Telefone"), 0, 6);
        pane.add(txtTelNumero, 1, 6);

        pane.add(lblEndComp, 2, 0, 2, 1);
        pane.add(new Label("Logradouro"), 2, 1);
        pane.add(txtLogradouro, 3, 1);
        pane.add(new Label("Número"), 2, 2);
        pane.add(txtNumero, 3, 2);
        pane.add(new Label("Complemento"), 2, 3);
        pane.add(txtComplemento, 3, 3);
        pane.add(new Label("Bairro"), 2, 4);
        pane.add(txtBairro, 3, 4);
        pane.add(new Label("Cidade"), 2, 5);
        pane.add(txtCidade, 3, 5);
        pane.add(new Label("Estado"), 2, 6);
        pane.add(txtEstado, 3, 6);
        pane.add(new Label("CEP"), 2, 7);
        pane.add(txtCep, 3, 7);

        Button btnPesquisar = new Button("Pesquisar");
        btnPesquisar.setOnAction((e) -> { control.pesquisar(); });

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction((e) -> {
            control.salvar();
            new Alert(Alert.AlertType.INFORMATION, "Comprador salvo com sucesso!").show();
        });

        Button btnLimpar = new Button("Limpar");
        btnLimpar.setOnAction((e) -> { control.limparCampos(); });

        pane.add(btnPesquisar, 0, 8);
        pane.add(btnSalvar, 1, 8);
        pane.add(btnLimpar, 2, 8);

        Bindings.bindBidirectional(txtUsername.textProperty(), control.usernameProperty());
        Bindings.bindBidirectional(txtNome.textProperty(), control.nomeProperty());
        Bindings.bindBidirectional(txtEmail.textProperty(), control.emailProperty());
        Bindings.bindBidirectional(txtSenha.textProperty(), control.senhaProperty());
        
        Bindings.bindBidirectional(txtLogradouro.textProperty(), control.logradouroProperty());
        Bindings.bindBidirectional(txtNumero.textProperty(), control.numeroProperty());
        Bindings.bindBidirectional(txtComplemento.textProperty(), control.complementoProperty());
        Bindings.bindBidirectional(txtBairro.textProperty(), control.bairroProperty());
        Bindings.bindBidirectional(txtCidade.textProperty(), control.cidadeProperty());
        Bindings.bindBidirectional(txtEstado.textProperty(), control.estadoProperty());
        Bindings.bindBidirectional(txtCep.textProperty(), control.cepProperty());
        
        Bindings.bindBidirectional(txtDdd.textProperty(), control.dddProperty());
        Bindings.bindBidirectional(txtTelNumero.textProperty(), control.telNumeroProperty());

        TableColumn<Comprador, String> colUser = new TableColumn<>("Username");
        colUser.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getUsername()));
        
        TableColumn<Comprador, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getNome()));
        
        TableColumn<Comprador, String> colCidade = new TableColumn<>("Cidade/UF");
        colCidade.setCellValueFactory(item -> new ReadOnlyStringWrapper(
            item.getValue().getEndereco() != null ? 
            item.getValue().getEndereco().getCidade() + "/" + item.getValue().getEndereco().getEstado() : ""
        ));

        table.setItems(control.getLista());
        table.getColumns().add(colUser);
        table.getColumns().add(colNome);
        table.getColumns().add(colCidade);

        table.getSelectionModel().selectedItemProperty().addListener((obj, antigo, novo) -> {
            if (novo != null) {
                control.toBoundary(novo);
            }
        });

        control.limparCampos();
        return bp;
    }
}