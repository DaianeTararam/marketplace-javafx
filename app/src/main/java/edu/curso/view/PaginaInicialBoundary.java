package edu.curso.view;

import edu.curso.controller.ProdutoControl;
import edu.curso.model.Produto;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PaginaInicialBoundary implements Tela {

    private TextField txtBusca = new TextField();
    private ComboBox<String> cmbCategoria = new ComboBox<>();
    private TableView<Produto> table = new TableView<>();

    private ProdutoControl control = new ProdutoControl();

    @Override
    public Pane render() {
        BorderPane bp = new BorderPane();

        //Para realizar esta parte de formatação, foi através da documentação
        //https://openjfx.io/javadoc/21/javafx.graphics/javafx/scene/doc-files/cssref.html
        Label lblTitulo = new Label("ArteCraft Marketplace");
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-padding: 16 16 4 16;");
        
        cmbCategoria.getItems().addAll(
            "Todas", "Crochê", "Tricô", "Bordado", "Pintura",
            "Cerâmica", "Macramê", "Bijuteria", "Outros"
        );
        cmbCategoria.setValue("Todas");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> buscar());

        txtBusca.setOnAction(e -> buscar());

        HBox barraBusca = new HBox(10, new Label("Categoria:"), cmbCategoria, txtBusca, btnBuscar);
        barraBusca.setStyle("-fx-padding: 8 16;");

        VBox topo = new VBox(lblTitulo, barraBusca);
        bp.setTop(topo);

        TableColumn<Produto, String> colNome = new TableColumn<>("Produto");
        colNome.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getNome()));
        colNome.setPrefWidth(200);

        TableColumn<Produto, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getCategoria()));
        colCategoria.setPrefWidth(120);

        TableColumn<Produto, String> colLoja = new TableColumn<>("Loja");
        colLoja.setCellValueFactory(item -> new ReadOnlyStringWrapper(
            item.getValue().getLoja() != null ? item.getValue().getLoja().getNome() : ""
        ));
        colLoja.setPrefWidth(150);

        TableColumn<Produto, String> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(item ->
            new ReadOnlyStringWrapper(String.format("R$ %.2f", item.getValue().getPreco()))
        );
        colPreco.setPrefWidth(100);

        TableColumn<Produto, String> colDesc = new TableColumn<>("Descrição");
        colDesc.setCellValueFactory(item -> new ReadOnlyStringWrapper(
            item.getValue().getDescricao() != null ? item.getValue().getDescricao() : ""
        ));
        colDesc.setPrefWidth(220);

        table.getColumns().addAll(colNome, colCategoria, colLoja, colPreco, colDesc);
        table.setItems(control.getListaProdutos());

        bp.setCenter(table);

        control.pesquisar();
        return bp;
    }

    private void buscar() {
        String categoria = cmbCategoria.getValue();
        String nome = txtBusca.getText().trim();

        control.nomeProperty().set(nome);
        control.pesquisar();

        if (categoria != null && !categoria.equals("Todas")) {
            control.getListaProdutos().removeIf(p ->
                !categoria.equalsIgnoreCase(p.getCategoria())
            );
        }
    }
}