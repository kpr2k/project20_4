package gr4.controllers;

import com.opencsv.CSVReader;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import gr4.dane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.graalvm.compiler.nodeinfo.StructuralInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Second extends Component implements Initializable {
    @FXML
    private TextField parametrP;

    @FXML
    private TextField parametrK;

    @FXML
    private TextField rozmiar;
    @FXML
    private FontAwesomeIcon close;

    @FXML
    private Button ok;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onClickEvent(javafx.scene.input.MouseEvent event) throws IOException {
        if (event.getSource() == ok) {

            /*String sciezkaDoPlik;
            JFileChooser otworz = new JFileChooser();
            int wynik = otworz.showOpenDialog(this);
            if (wynik == JFileChooser.APPROVE_OPTION) {
                dane dane = new dane();
                sciezkaDoPlik = otworz.getSelectedFile().getPath();
                dane.odczytajPlik(sciezkaDoPlik);
                for (int i = 0; i < dane.daneOdczytane.length; i++) {
                    for (int j = 0; j < dane.daneOdczytane[i].length; j++) {
                        System.out.print(dane.daneOdczytane[i][j] + " ");
                    }
                    System.out.print("\n");

                }
                dane.podzialNaZbiory(Integer.parseInt(rozmiar.getText()));
                //System.out.println(dane.klasyfikujWektor(cancer, 2 ,3));
                System.out.println("h(x) = " + dane.wyznaczDokladnosc(Integer.parseInt(parametrP.getText()), Integer.parseInt(parametrK.getText()),dane.zbior_uczacy));
            }*/
        }
    }
    @FXML
    public void klikZamknij(javafx.scene.input.MouseEvent event) {
        if(event.getSource()==close) {
            System.exit(0);
        }
    }
}
