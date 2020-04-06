package gr4.controllers;

import com.opencsv.CSVReader;
import gr4.dane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends Component implements Initializable {
    @FXML
    private Button btnRead;

    @FXML
    private Button btnChart;

    @FXML
    private Button btnWektor;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onClickEvent(javafx.scene.input.MouseEvent event) throws IOException {
        if(event.getSource()==btnRead) {
            String sciezkaDoPlik;
            JFileChooser otworz= new JFileChooser();
            int wynik = otworz.showOpenDialog(this);
            if(wynik== JFileChooser.APPROVE_OPTION)
            {
                dane dane = new dane();
                sciezkaDoPlik= otworz.getSelectedFile().getPath();
                dane.odczytajPlik(sciezkaDoPlik);
                for(int i = 0; i<dane.daneOdczytane.length; i++)
                {
                    for(int j = 0; j<dane.daneOdczytane[i].length; j++){
                        System.out.print(dane.daneOdczytane[i][j]+" ");
                    }
                    System.out.print("\n");
                }
                //dane.podzialNaZbiory(15);
            }
        } else if (event.getSource()==btnChart) {
            final NumberAxis yAxis = new NumberAxis(0, 15, 1);
            final NumberAxis xAxis = new NumberAxis(-2, 20, 1);

            final ScatterChart<Number, Number> lineChart = new ScatterChart<>(xAxis, yAxis);
            yAxis.setLabel("OSY");
            xAxis.setLabel("OSX");
            lineChart.setTitle("Klasy");

            XYChart.Series seriesA = new XYChart.Series();
            XYChart.Series seriesB = new XYChart.Series();
            XYChart.Series seriesC = new XYChart.Series();

            seriesA.setName("Klasa A");
            seriesB.setName("Klasa B");
            seriesC.setName("Klasa C");

            CSVReader dataReader = new CSVReader(new FileReader("A:\\Semestr 5\\Semestr 6\\przyklad1.csv");
                String[] nextLine;
                while ((nextLine = dataReader.readNext()) != null) {
                    int axisX = Integer.parseInt(nextLine[0]);
                    int axisY = Integer.parseInt(nextLine[1]);
                    String klasa = String.valueOf(nextLine[2]);
                    if (klasa.equals("KlasaA")) {
                        seriesA.getData().add(new XYChart.Data(axisX, axisY));

                    } else if (klasa.equals("KlasaB")) {
                        seriesB.getData().add(new XYChart.Data(axisX, axisY));
                    } else if (klasa.equals("KlasaC")) {
                        seriesC.getData().add(new XYChart.Data(axisX, axisY));
                    }
                }

                lineChart.getData().addAll(seriesA, seriesB, seriesC);
                Scene scene = new Scene(lineChart, 500, 400);
                Stage stage = new Stage();
                stage.setTitle("Wykres");
                stage.setScene(scene);
                stage.show();
            }


        } else if (event.getSource()==btnWektor) {
            /**
             * Użytkownik musi mieć pole do wpisania:
             * - liczby p
             * - liczby k
             */
            String sciezkaDoPlik;
            JFileChooser otworz= new JFileChooser();
            int wynik = otworz.showOpenDialog(this);
            if(wynik== JFileChooser.APPROVE_OPTION)
            {
                dane dane = new dane();
                sciezkaDoPlik= otworz.getSelectedFile().getPath();
                dane.odczytajPlik(sciezkaDoPlik);
                for(int i = 0; i<dane.daneOdczytane.length; i++)
                {
                    for(int j = 0; j<dane.daneOdczytane[i].length; j++){
                        System.out.print(dane.daneOdczytane[i][j]+" ");
                    }
                    System.out.print("\n");

                }
                String[] cancer = {"4","1","2","4","2","1","2","1","1"};
                String[] klasy = {"3","6"};
                dane.podzialNaZbiory(150);
                //System.out.println(dane.klasyfikujWektor(cancer, 2 ,3));
                System.out.println("h(x) = "+dane.wyznaczDokladnosc(2,3));
            }
        }
        }
    }
