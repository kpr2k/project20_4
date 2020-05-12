package gr4.controllers;

import com.opencsv.CSVReader;
import gr4.dane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.SubScene;
import javafx.scene.chart.LineChart;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

import java.io.*;

import java.net.URL;
import java.util.*;

public class Controller extends Component implements Initializable {
    @FXML
    private Button btnRead;

    @FXML
    private Button btnChart;

    @FXML
    private Button btnWektor;
    @FXML
    private Pane chart;
    @FXML
    private TextField kolumnaX;

    @FXML
    private TextField kolumnaY;

    @FXML
    private Button ok;

    @FXML
    private ScatterChart<Number,Number> obszarWykresu;

    @FXML
    private Pane chart;

    @FXML
    private TextField kolumnaX;

    @FXML
    private TextField kolumnaY;

    @FXML
    private TextField parametrP;

    @FXML
    private TextField parametrK;

    @FXML
    private TextField rozmiar;

    @FXML
    private TextField parametrKwalidacja;

    @FXML
    private TextField wektor;

    @FXML
    private TextArea output;

    @FXML
    private Button ok;


    JFrame f;
    JTable k;

    JFrame f;
    JTable k;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onClickEvent(javafx.scene.input.MouseEvent event) throws IOException {
        if(event.getSource()==btnRead) {
            ArrayList<String> odczyt = new ArrayList();
            String sciezkaDoPlik;
            JFileChooser otworz= new JFileChooser();
            int wynik = otworz.showOpenDialog(this);
            if(wynik== JFileChooser.APPROVE_OPTION)
            {
                /*dane dane = new dane(Double.parseDouble(parametrP.getText()),
                        Integer.parseInt(parametrK.getText()),
                        Integer.parseInt(rozmiar.getText()));*/
                dane dane = new dane();
                sciezkaDoPlik= otworz.getSelectedFile().getPath();
                dane.odczytajPlik(sciezkaDoPlik);
                for(int i=0; i<dane.daneOdczytane[i].length;i++) {
                    if(dane.daneOdczytane[0][i]!=null) {
                        TableColumn tmp = new TableColumn("Kolumna "+(i+1));
                        odczyt.add("Kolumna"+(i+1));
                    }
                }
                f = new JFrame();
                f.setTitle("Szo to za staroc");
                String[][] data = dane.daneOdczytane;
                String[] columnNames = new String[odczyt.size()];
                for(int i=0;i<odczyt.size();i++) {
                    columnNames[i]=odczyt.get(i);
                }
                k = new JTable(data, columnNames);
                k.getAutoResizeMode();
                JScrollPane sp = new JScrollPane(k);
                f.add(sp);
                f.setVisible(true);
            }
        } else if (event.getSource()==btnChart) {

            rysujWykres();


        }else if (event.getSource()==btnWektor) {
            /**
             * Użytkownik musi mieć pole do wpisania:
             * - liczby p
             * - liczby k
             */
            //String sciezkaDoPlik;
            //JFileChooser otworz= new JFileChooser();
            // wynik = otworz.showOpenDialog(this);
            if(dane.typ_pliku > 0)
            {
                klasyfikacjaKWalidacja();
            }
        }
    }

    public void onAction(javafx.event.ActionEvent event) throws  IOException{
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/gr4/second.fxml").toURI().toURL());
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Main.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    public void addPoint(javafx.event.ActionEvent event) throws  IOException{
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/gr4/Punkt.fxml").toURI().toURL());
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Main.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
        rysujWykres();
    }

    public void rysujWykres(){
        if(dane.typ_pliku > 0){
            //drawChart(Integer.parseInt(kolumnaX.getText()),Integer.parseInt(kolumnaY.getText()));
            if(!kolumnaX.getText().isEmpty() || !kolumnaY.getText().isEmpty()){
                drawChart(Integer.parseInt(kolumnaX.getText()),Integer.parseInt(kolumnaY.getText()));
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Nie wypełniono wszystkich pól");
                alert.setContentText("Aby narysować wykres należy podać numery kolumn w zakładce Rysowanie!");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Nie wczytano pliu");
            alert.setContentText("Aby narysować wykres należy wczytać plik z danymi!");
            alert.showAndWait();
        }
    }

    public void klasyfikacjaKNN(){
        if(!parametrP.getText().isEmpty() && !parametrK.getText().isEmpty() && !rozmiar.getText().isEmpty() && !wektor.getText().isEmpty()){
            //dane dane = new dane();
            dane.setParametry(Double.parseDouble(parametrP.getText()),
                    Integer.parseInt(parametrK.getText()),
                    Integer.parseInt(rozmiar.getText()));

            String str = wektor.getText();
            String[] wektor = str.split(",");

            //System.out.println(dane.klasyfikujWektor(wektor, dane.zbior_uczacy));
            output.appendText("Wynik klasyfikacji kNN: "+dane.klasyfikujWektor(wektor, dane.zbior_uczacy)+"\n");
            output.appendText("Dokladnosc kNN: "+String.format("%.2f", dane.wyznaczDokladnosc(dane.zbior_uczacy))+"\n");
            //output.appendText(dane.wyznaczDokladnosc(dane.zbior_uczacy)+"\n");
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Nie wypełniono wszystkich pól");
            alert.setContentText("Aby sklasyfikować wektor należy podać wszystkie parametry w zakładce Klasyfikacja kNN!");
            alert.showAndWait();
        }
    }

    public void klasyfikacjaKWalidacja(){
        if(!parametrP.getText().isEmpty() && !parametrK.getText().isEmpty() && !parametrKwalidacja.getText().isEmpty()) {
            //dane dane = new dane();
            if(!rozmiar.getText().isEmpty()){
                dane.setParametry(Double.parseDouble(parametrP.getText()),
                        Integer.parseInt(parametrK.getText()),
                        Integer.parseInt(rozmiar.getText()));
            }else{
                dane.setParametry(Double.parseDouble(parametrP.getText()),
                        Integer.parseInt(parametrK.getText()));
            }
            dane.setParametrKWalidacja(Integer.parseInt(parametrKwalidacja.getText()));
            //sciezkaDoPlik= otworz.getSelectedFile().getPath();
            //dane.odczytajPlik(sciezkaDoPlik);
            for (int i = 0; i < dane.daneOdczytane.length; i++) {
                for (int j = 0; j < dane.daneOdczytane[i].length; j++) {
                    System.out.print(dane.daneOdczytane[i][j] + " ");
                }

                System.out.print("\n");

            }
            String str = wektor.getText();
            String[] wektor = str.split(",");


            //String[] cancer = {"4","1","2","4","2","1","2","1","1"};
            //String[] klasy = {"3","6"};
            //dane.podzialNaZbiory();
            //System.out.println(dane.klasyfikujWektor(cancer, 2 ,3,dane.zbior_uczacy));
            String wynik = String.format("%.2f", dane.klasyfikujWalidacja(wektor));
            System.out.println(wynik);
            output.appendText("Dokladnosc (srednia) klasyfikacji metoda k-krotnej walidacji:" + wynik + "\n");
            //output.appendText(wynik + "\n");
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Nie wypełniono wszystkich pól");
            alert.setContentText("Aby sklasyfikować wektor  metodą k-krotnej walidacji należy podać wszystkie parametry w zakładkach Klasyfikacja kNN oraz Klasyfikacja Metodą k-krotnej walidacji!");
            alert.showAndWait();
        }
    }


    public void drawChart(int kolumnaX, int kolumnaY) {
        ArrayList<XYChart.Series> seriesArrayList = new ArrayList<>();
        seriesArrayList.clear();

        final NumberAxis yAxis = new NumberAxis();
        final NumberAxis xAxis = new NumberAxis();
        final ScatterChart<Number, Number> lineChart = new ScatterChart<>(xAxis, yAxis);
        yAxis.setLabel("OSY");
        xAxis.setLabel("OSX");
        HashSet<String> h = new HashSet<String>();
        for(int i = 0; i<dane.daneOdczytane.length; i++) {
            if(dane.daneOdczytane[i][dane.daneOdczytane[i].length-1]!=null &&!(dane.daneOdczytane[i][dane.daneOdczytane[i].length-1].equals("Y"))
                    && !(dane.daneOdczytane[i][dane.daneOdczytane[i].length-1].equals("default payment next month"))) {
                h.add(dane.daneOdczytane[i][dane.daneOdczytane[i].length-1]);

            }

        }
        Iterator<String> c = h.iterator();
        System.out.println(h.toString());
        while (c.hasNext()) {
            XYChart.Series tmp = new XYChart.Series();
            tmp.setName(c.next());
            seriesArrayList.add(tmp);
        };
        if(seriesArrayList.get(0).getName().equals("0")&&seriesArrayList.get(1).getName().equals("1")){
            System.out.println(dane.daneOdczytane.length);
            Iterator<XYChart.Series> seriesIterator = seriesArrayList.iterator();
            System.out.println(seriesArrayList.toString());
            for(int i = 2; i<dane.daneOdczytane.length; i++)
            {
                for (int k=0; k<seriesArrayList.size();k++) {
                    if ((dane.daneOdczytane[i][dane.daneOdczytane[i].length - 1]).equals(seriesArrayList.get(k).getName())) {
                        double axisX = Double.parseDouble(dane.daneOdczytane[i][kolumnaX-1]);
                        double axisY = Double.parseDouble(dane.daneOdczytane[i][kolumnaY-1]);
                        seriesArrayList.get(k).getData().add(new XYChart.Data(axisX, axisY));
                    }
                }
            }
            while (seriesIterator.hasNext()) lineChart.getData().addAll(seriesIterator.next());

            obszarWykresu.setData(lineChart.getData());
            /*Scene scene = new Scene(lineChart, 800, 600);
            Stage stage = new Stage();
            stage.setTitle("Wykres");
            stage.setScene(scene);
            stage.show();*/
        }else {
            System.out.println(dane.daneOdczytane.length);
            Iterator<XYChart.Series> seriesIterator = seriesArrayList.iterator();
            System.out.println(seriesArrayList.toString());
            for (int i = 0; i < dane.daneOdczytane.length; i++) {
                for (int k = 0; k < seriesArrayList.size(); k++) {
                    if ((dane.daneOdczytane[i][dane.daneOdczytane[i].length - 1]).equals(seriesArrayList.get(k).getName())) {
                        int axisX = Integer.parseInt(dane.daneOdczytane[i][kolumnaX - 1]);
                        int axisY = Integer.parseInt(dane.daneOdczytane[i][kolumnaY - 1]);
                        seriesArrayList.get(k).getData().add(new XYChart.Data(axisX, axisY));
                    }
                }
            }
            while (seriesIterator.hasNext()) lineChart.getData().addAll(seriesIterator.next());
            obszarWykresu.setData(lineChart.getData());
            /*Scene scene = new Scene(lineChart, 500, 400);
            Stage stage = new Stage();
            stage.setTitle("Wykres");
            stage.setScene(scene);
            stage.show();*/

        }
    }
}
