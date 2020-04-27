package gr4.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import gr4.dane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Punkt extends Component implements Initializable {
    @FXML
    private TextField wspx1;

    @FXML
    private FontAwesomeIcon close;

    @FXML
    private Button addPkt;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onClickEvent(javafx.scene.input.MouseEvent event) throws IOException {
        if (event.getSource() == addPkt) {
            String str = wspx1.getText();
            String[] t = str.split(",");
            dane.dodajPunkt(t);
            Controller.drawChart(1,2);
        }
    }
    @FXML
    public void klikZamknij(javafx.scene.input.MouseEvent event) {
        if(event.getSource()==close) {
            System.exit(0);
        }
    }
}





