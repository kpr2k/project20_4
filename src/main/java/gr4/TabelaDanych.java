package gr4;

import gr4.tableadapter.DefaultTableAdapter;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class TabelaDanych extends Component implements Initializable {
    public TableView view;
    public TableView viewTest;
    public TableView viewUcz;
    public TableView viewUczodleglosc;
    public DefaultTableAdapter dta;
    public BorderPane pnlRoot;
    public void piszDane(Object[][] rowData, String[] columnNames, String[] columnNamesOdleglosc, Object[][] test, Object[][] ucz, Object[][] ucz_odleglosc) {
        AnchorPane anchorPane = new AnchorPane();
        view = new TableView();
        dta = new DefaultTableAdapter(view, rowData, columnNames);
        viewTest = new TableView();
        dta = new DefaultTableAdapter(viewTest, test, columnNames);
        viewUcz = new TableView();
        dta = new DefaultTableAdapter(viewUcz, ucz, columnNames);
        viewUczodleglosc = new TableView();
        dta = new DefaultTableAdapter(viewUczodleglosc, ucz_odleglosc, columnNamesOdleglosc);
        Stage stage = new Stage();
        stage.setTitle("Tabela danych");
        stage.setWidth(400);
        stage.setHeight(480);
        pnlRoot = new BorderPane();
        pnlRoot.prefWidthProperty().set(350);
        pnlRoot.prefHeightProperty().set(450);
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(new Tab("Dane",view));
        tabPane.getTabs().add(new Tab("Zbior testujacych",viewTest));
        tabPane.getTabs().add(new Tab("Zbior uczacych",viewUcz));
        tabPane.getTabs().add(new Tab("Dane z odlegloscia od wektora",viewUczodleglosc));
        pnlRoot.setTop(tabPane);
        pnlRoot.setCenter(view);
        Scene scene = new Scene(pnlRoot);
        stage.setScene(scene);
        stage.show();
    }

    public void sprzatanko() {

    }
    public void widok() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}


