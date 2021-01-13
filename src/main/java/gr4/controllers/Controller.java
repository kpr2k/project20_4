package gr4.controllers;

import com.opencsv.CSVReader;
import gr4.TabelaDanych;
import gr4.Wezel;
import gr4.dane;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.prefs.Preferences;

public class Controller extends Component implements Initializable {
    private static final String LAST_USED_FOLDER = "";
    @FXML
    private Button btnRead;

    @FXML
    private Button btnChart;

    @FXML
    private Button btnWektor;

    @FXML
    private Button btnKlasa;

    @FXML
    private Button aktualizacja;

    @FXML
    private Button aktualizacjaRozmiar;

    @FXML
    private ScatterChart<Number,Number> obszarWykresu;

    @FXML
    private ScatterChart<Number,Number> obszarWykresuUczacy;

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
    @FXML
    private Button btnWyswietl;

    JFrame f;
    JTable k;
    double max_height;
    double max_width;
    TabelaDanych tabelaDanych;
    public String[] columnNames;

    public String[] columnNamesOdleglosc;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onClickEvent(javafx.scene.input.MouseEvent event) throws IOException {

        if(event.getSource()==btnRead) {
            obszarWykresu.getData().clear();
            ArrayList<String> odczyt = new ArrayList();
            String sciezkaDoPlik;
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "csv");
            Preferences prefs = Preferences.userRoot().node(getClass().getName());
            JFileChooser otworz= new JFileChooser(prefs.get(LAST_USED_FOLDER,
                    new File(".").getAbsolutePath()));
            otworz.setFileFilter(filter);
            int wynik = otworz.showOpenDialog(this);
            if(wynik== JFileChooser.APPROVE_OPTION)
            {
                /*dane dane = new dane(Double.parseDouble(parametrP.getText()),
                        Integer.parseInt(parametrK.getText()),
                        Integer.parseInt(rozmiar.getText()));*/
                dane dane = new dane();
                sciezkaDoPlik= otworz.getSelectedFile().getPath();
                dane.odczytajPlik(sciezkaDoPlik);
                for(int i=0; i<dane.daneOdczytane[0].length;i++) {
                    if(dane.daneOdczytane[0][i]!=null) {
                        TableColumn tmp = new TableColumn("Kolumna "+(i+1));
                        odczyt.add("Kolumna"+(i+1));
                    }
                }

                columnNames = new String[odczyt.size()];

                columnNamesOdleglosc = new String[(odczyt.size())+1];
                columnNamesOdleglosc[columnNamesOdleglosc.length-1] = "Odleglosc";

                for(int i=0;i<odczyt.size();i++) {
                    columnNames[i]=odczyt.get(i);
                    columnNamesOdleglosc[i]=odczyt.get(i);
                }
                prefs.put(LAST_USED_FOLDER, otworz.getSelectedFile().getParent());
            }
        } else if (event.getSource()==btnChart) {
            btnChart.setDisable(true);
            rysujWykres(1);
            rysujWykres(2);

            btnChart.setDisable(false);
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
        }else if (event.getSource()==aktualizacja) {
            aktualizacja.setDisable(true);
            btnChart.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(toUTF8Conversion("Błąd"));
            alert.setHeaderText(toUTF8Conversion("Nie wypełniono wszystkich pól"));
            if(dane.daneOdczytane != null){
                if(!parametrP.getText().equals("") && parametrK.getText().equals("")){
                    alert.setContentText(toUTF8Conversion("Nie został podany parametr K!"));
                    alert.showAndWait();
                }else if(parametrP.getText().equals("") && !parametrK.getText().equals("")){

                    alert.setContentText(toUTF8Conversion("Nie został podany parametr P"));
                    alert.showAndWait();
                }else if(parametrP.getText().equals("") && parametrK.getText().equals("")){
                    alert.setContentText(toUTF8Conversion("Nie zostały podane parametry K i P"));
                    alert.showAndWait();
                }else{
                    if(Integer.parseInt(parametrK.getText())>dane.zbior_uczacy.length) {parametrK.setText(Integer.toString(dane.zbior_uczacy.length));}
                    dane.setParametry(Integer.parseInt(parametrP.getText()),
                            Integer.parseInt(parametrK.getText()));
                    output.appendText("Zaktualizowano parametry."+"\nParametr K: " + dane.parametrK + "\nParametr P: "+dane.parametrP+
                            "\n....................................................................\n");
                    if(!kolumnaX.getText().equals("") && !kolumnaY.getText().equals("")){
                        //rysujWykres(1);
                        //rysujWykres(2);
                        if(!rozmiar.getText().isEmpty()){
                            //rysujWykres(2);
                        }
                    }

                }
            }else{
                alert.setTitle(toUTF8Conversion("Błąd"));
                alert.setHeaderText(toUTF8Conversion("Brak danych"));
                alert.setContentText(toUTF8Conversion("Nie zostały wczytane żadne dane"));
                alert.showAndWait();
            }
        }else if (event.getSource()==btnKlasa) {
            btnKlasa.setDisable(true);
            klasyfikacjaKNN();
            btnKlasa.setDisable(false);

        }else if (event.getSource()==aktualizacjaRozmiar) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if(dane.daneOdczytane != null){
                if(rozmiar.getText()!=null){
                    if(!rozmiar.getText().equals("")){
                        if(dane.sprawdzRozmiar(Integer.parseInt(rozmiar.getText()))){
                            dane.setDaneZbiory(Integer.parseInt(rozmiar.getText()));
                            output.appendText("Zaktualizowano rozmiar uczacy."+"\nRozmiar zbioru uczacego: " + dane.rozmiar_uczacy +
                                    "\n....................................................................\n");
                            if(!kolumnaX.getText().equals("") && !kolumnaY.getText().equals("")){
                                //rysujWykres(1);
                                //rysujWykres(2);
                            }
                        }else{
                            alert.setTitle(toUTF8Conversion("Błąd"));
                            alert.setHeaderText(toUTF8Conversion("Złe dane"));
                            alert.setContentText(toUTF8Conversion("Podano zbyt duży rozmiar uczący!"));
                            alert.showAndWait();
                        }
                    }else{
                        alert.setTitle(toUTF8Conversion("Błąd"));
                        alert.setHeaderText(toUTF8Conversion("Nie wypełniono wszystkich pól"));
                        alert.setContentText(toUTF8Conversion("Nie podano rozmiaru zbioru!"));
                        alert.showAndWait();
                    }
                }
            }else{
                alert.setTitle(toUTF8Conversion("Błąd"));
                alert.setHeaderText(toUTF8Conversion("Brak danych"));
                alert.setContentText(toUTF8Conversion("Nie zostały wczytane żadne dane"));
                alert.showAndWait();
            }

        }else if (event.getSource()==btnWyswietl) {
            tabelaDanych = new TabelaDanych();

            tabelaDanych.piszDane(dane.daneOdczytane,columnNames, columnNamesOdleglosc, dane.zbior_testowy,dane.zbior_uczacy, dane.zbior_danych_odleglosci);

        }
        aktualizacja.setDisable(false);
        btnChart.setDisable(false);
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

    public String toUTF8Conversion(String s){
        return s;
    }

    public void addPoint(javafx.event.ActionEvent event) throws  IOException{
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/gr4/Punkt.fxml").toURI().toURL());
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Main.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
        rysujWykres(1);
    }

    public void rysujWykres(int i){
        if(dane.typ_pliku > 0){
            //drawChart(Integer.parseInt(kolumnaX.getText()),Integer.parseInt(kolumnaY.getText()));
            if(!kolumnaX.getText().isEmpty() || !kolumnaY.getText().isEmpty()){

                if(Integer.parseInt(kolumnaX.getText())>dane.daneOdczytane[0].length-1 ||
                        Integer.parseInt(kolumnaY.getText())>dane.daneOdczytane[0].length-1
                ){
                    if(i == 1){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Błąd");
                        alert.setHeaderText("Podano nieprawidłowe dane");
                        alert.setContentText("Podane kolumny nie mogą być narysowane dla wczytanych danych");
                        alert.showAndWait();
                    }else if(i == 2){
                    }
                }else{
                    if(dane.flaga_parametrP == false || dane.flaga_parametrK == false){
                        if(i == 1){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Błąd");
                            alert.setHeaderText("Nie wypełniono wszystkich pól");
                            alert.setContentText("Aby sklasyfikować wektor należy uzupełnić parametry K i P");
                            alert.showAndWait();
                        }else if(i == 2){
                        }
                    }else{
                        if(i == 1){
                            String rozmiar ="";
                            if(dane.rozmiar_uczacy == 0) {
                                rozmiar = ""+(int)dane.daneOdczytane.length/2;
                            }else{
                                rozmiar = ""+dane.rozmiar_uczacy;
                            }
                            drawChart(Integer.parseInt(kolumnaX.getText()),Integer.parseInt(kolumnaY.getText()));
                            output.appendText("Narysowano wykres dla podanych parametrow."+"\nParametr K: " + dane.parametrK + "\nParametr P: "+dane.parametrP
                                    + "\nRozmiar zbioru uczacego: "+rozmiar+
                                    "\n....................................................................\n");
                        }else if(i == 2){
                            drawChart2(Integer.parseInt(kolumnaX.getText()),Integer.parseInt(kolumnaY.getText()));
                        }
                    }
                }

            }else{
                if(i == 1){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setHeaderText("Nie wypełniono wszystkich pól");
                    alert.setContentText("Aby narysować wykres należy podać numery kolumn w zakładce Rysowanie!");
                    alert.showAndWait();
                }else if(i == 2){
                }
            }
        }else{
            if(i == 1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Nie wczytano pliku");
                alert.setContentText("Aby narysować wykres należy wczytać plik z danymi!");
                alert.showAndWait();
            }else if(i == 2){
            }
        }

    }

    public void klasyfikacjaKNN(){
        //!parametrP.getText().isEmpty() && !parametrK.getText().isEmpty() && !rozmiar.getText().isEmpty() && !wektor.getText().isEmpty()

        if(dane.flaga_parametrP == true && dane.flaga_parametrK == true && !wektor.getText().isEmpty()){
            //dane dane = new dane();
            dane.setParametry(Integer.parseInt(parametrP.getText()),
                    Integer.parseInt(parametrK.getText()));

            String str = wektor.getText();
            String[] wektor = str.split(",");

            String rozmiar ="";
            if(dane.rozmiar_uczacy == 0) {
                rozmiar = ""+(int)dane.daneOdczytane.length/2;
                //dane.setDaneZbiory((int)dane.daneOdczytane.length/2);
            }else{
                rozmiar = ""+dane.rozmiar_uczacy;
                //dane.setDaneZbiory(dane.rozmiar_uczacy);
            }


            //System.out.println(dane.klasyfikujWektor(wektor, dane.zbior_uczacy));
            output.appendText("Parametr K: " +Integer.parseInt(parametrK.getText()) + "\nParametr P: "+Integer.parseInt(parametrP.getText())+
            "\nRozmiar zbioru uczacego: " +Integer.parseInt(rozmiar));

            output.appendText("\nWynik klasyfikacji kNN: "+dane.klasyfikujWektor(wektor, dane.zbior_uczacy)+"\n");
            output.appendText("Dokladnosc kNN: "+String.format("%.2f", dane.wyznaczDokladnosc(dane.zbior_uczacy))+
                    "\n....................................................................\n");
            //output.appendText(dane.wyznaczDokladnosc(dane.zbior_uczacy)+"\n");
            /*if(!kolumnaX.getText().equals("") && !kolumnaY.getText().equals("")){
                rysujWykres(1);

                if(!rozmiar.getText().isEmpty()){
                    rysujWykres(2);
                }
            }*/
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
            dane.setParametry(Integer.parseInt(parametrP.getText()),
                    Integer.parseInt(parametrK.getText()));
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

            String wynik = String.format("%.6f", dane.klasyfikujWalidacja(wektor));
            System.out.println(wynik);
            output.appendText("Parametr K: " +Integer.parseInt(parametrK.getText()) + "\nParametr P: "+Integer.parseInt(parametrP.getText())+
                    "\nIlosc walidacji: "+Integer.parseInt(parametrKwalidacja.getText())+"\nDokladnosc klasyfikacji: " + wynik +
                    "\n....................................................................\n");
            //output.appendText(wynik + "\n");
            /*if(!kolumnaX.getText().equals("") && !kolumnaY.getText().equals("")){
                rysujWykres(1);
                if(!rozmiar.getText().isEmpty()){
                    rysujWykres(2);
                }
            }*/
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Nie wypełniono wszystkich pól");
            alert.setContentText("Aby sklasyfikować wektor  metodą n-krotnej walidacji należy podać wszystkie parametry w zakładkach Klasyfikacja kNN oraz Klasyfikacja Metodą n-krotnej walidacji!");
            alert.showAndWait();
        }
    }

    public void drawChart(int kolumnaX, int kolumnaY) {
        ArrayList<XYChart.Series> seriesArrayList = new ArrayList<>();
        seriesArrayList.clear();
        ArrayList<XYChart.Series> seriesArrayListObszary = new ArrayList<>();
        seriesArrayListObszary.clear();
        ArrayList<String> seriesArrayListWezly = new ArrayList<>();
        seriesArrayListWezly.clear();
        final NumberAxis yAxis = new NumberAxis();
        final NumberAxis xAxis = new NumberAxis();
        final ScatterChart<Number, Number> lineChart = new ScatterChart<>(xAxis, yAxis);
        final ScatterChart<Number, Number> lineChart2 = new ScatterChart<>(xAxis, yAxis);
        //lineChart.setStyle("Obszary.css");
        dane.obszary = plaszczyznaDecyzji(true);
        dane.wezlytablicaObszary = new Wezel[dane.obszary.length];
        yAxis.setLabel("OSY");
        xAxis.setLabel("OSX");
        HashSet<String> h = new HashSet<String>();

        for(int i = 0; i<dane.obszary.length; i++) {
            for(int j = 0; j<dane.obszary[0].length; j++) {
                h.add(dane.obszary[i][j][dane.obszary[i][j].length-1]);
            }
        }

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
            /*tmp.setNode(new Node() {
                @Override
                public Node lookup(String selector) {
                    return super.lookup(selector);
                }
            });*/
            seriesArrayList.add(tmp);

        };

        if(seriesArrayList.get(0).getName().equals("0")&&seriesArrayList.get(1).getName().equals("1")){
            System.out.println(dane.daneOdczytane.length);
            Iterator<XYChart.Series> seriesIterator = seriesArrayList.iterator();
            //Iterator<XYChart.Series> seriesIteratorObszary = seriesArrayListObszary.iterator();
            System.out.println(seriesArrayList.toString());
            for(int i = 2; i<dane.daneOdczytane.length; i++)
            {
                for (int k=0; k<seriesArrayList.size();k++) {
                    if ((dane.daneOdczytane[i][dane.daneOdczytane[i].length - 1]).equals(seriesArrayList.get(k).getName())) {
                        double axisX = Double.parseDouble(dane.daneOdczytane[i][kolumnaX-1]);
                        double axisY = Double.parseDouble(dane.daneOdczytane[i][kolumnaY-1]);
                        /*dane.wezlytablica[i] = new XYChart.Data(axisX, axisY);
                        seriesArrayList.get(k).getData().add(dane.wezlytablica[i]);*/
                        dane.wezlytablica[i] = new Wezel(axisX,axisY,i);
                        dane.wezlytablica[i].data.getNode().setId(seriesArrayList.get(k).getName());
                        seriesArrayList.get(k).getData().add(dane.wezlytablica[i].getData());
                        seriesArrayListWezly.add(seriesArrayList.get(k).getName());
                    }
                }
            }
            for (int i = 0; i < dane.obszary.length; i++) {
                for (int j = 0; j < dane.obszary[i].length; j++) {
                    for (int k = 0; k < seriesArrayList.size(); k++) {
                        if ((dane.obszary[i][j][dane.obszary[i][j].length - 1]).equals(seriesArrayList.get(k).getName())) {
                            double axisX = Double.parseDouble(dane.obszary[i][j][kolumnaX-1]);
                            double axisY = Double.parseDouble(dane.obszary[i][j][kolumnaY-1]);
                            /*dane.wezlytablica[i] = new XYChart.Data(axisX, axisY);
                            seriesArrayList.get(k).getData().add(dane.wezlytablica[i]);*/
                            dane.wezlytablicaObszary[i] = new Wezel(axisX,axisY,i);
                            seriesArrayList.get(k).getData().add(dane.wezlytablicaObszary[i].getData());
                            seriesArrayListWezly.add(seriesArrayList.get(k).getName());
                        }
                    }

                }
            }

            while (seriesIterator.hasNext()) lineChart.getData().addAll(seriesIterator.next());
            obszarWykresu.setData(lineChart.getData());

        }else {
            for (int i = 0; i < dane.obszary.length; i++) {
                for (int j = 0; j < dane.obszary[i].length; j++) {
                    for (int k = 0; k < seriesArrayList.size(); k++) {
                        if ((dane.obszary[i][j][dane.obszary[i][j].length - 1]).equals(seriesArrayList.get(k).getName())) {
                            double axisX = Double.parseDouble(dane.obszary[i][j][kolumnaX-1]);
                            double axisY = Double.parseDouble(dane.obszary[i][j][kolumnaY-1]);
                            /*dane.wezlytablica[i] = new XYChart.Data(axisX, axisY);
                            seriesArrayList.get(k).getData().add(dane.wezlytablica[i]);*/
                            dane.wezlytablicaObszary[i] = new Wezel(axisX,axisY,i);
                            seriesArrayList.get(k).getData().add(dane.wezlytablicaObszary[i].getData());
                            seriesArrayListWezly.add(seriesArrayList.get(k).getName());
                        }
                    }

                }
            }
            System.out.println(dane.daneOdczytane.length);
            Iterator<XYChart.Series> seriesIterator = seriesArrayList.iterator();
            System.out.println(seriesArrayList.toString());
            for (int i = 0; i < dane.obszary.length; i++) {
                for (int j = 0; j < dane.obszary[i].length; j++) {
                    for (int k = 0; k < seriesArrayList.size(); k++) {
                        if ((dane.obszary[i][j][dane.obszary[i][j].length - 1]).equals(seriesArrayList.get(k).getName())) {
                            double axisX = Double.parseDouble(dane.obszary[i][j][kolumnaX-1]);
                            double axisY = Double.parseDouble(dane.obszary[i][j][kolumnaY-1]);
                            /*dane.wezlytablica[i] = new XYChart.Data(axisX, axisY);
                            seriesArrayList.get(k).getData().add(dane.wezlytablica[i]);*/
                            dane.wezlytablicaObszary[i] = new Wezel(axisX,axisY,i);
                            //dane.wezlytablica[i].data.setExtraValue(seriesArrayList.get(k).getName());
                            //dane.indeks[i] = i;
                            seriesArrayList.get(k).getData().add(dane.wezlytablicaObszary[i].getData());
                            seriesArrayListWezly.add(seriesArrayList.get(k).getName());
                        }
                    }

                }
            }
            for (int i = 0; i < dane.daneOdczytane.length; i++) {
                for (int k = 0; k < seriesArrayList.size(); k++) {
                    if ((dane.daneOdczytane[i][dane.daneOdczytane[i].length - 1]).equals(seriesArrayList.get(k).getName())) {
                        int axisX = Integer.parseInt(dane.daneOdczytane[i][kolumnaX - 1]);
                        int axisY = Integer.parseInt(dane.daneOdczytane[i][kolumnaY - 1]);
                        dane.wezlytablica[i] = new Wezel(axisX,axisY,i);

                        seriesArrayList.get(k).getData().add(dane.wezlytablica[i].getData());
                        seriesArrayListWezly.add(seriesArrayList.get(k).getName());
                    }
                }

            }

            while (seriesIterator.hasNext()) lineChart.getData().addAll(seriesIterator.next());
            obszarWykresu.setData(lineChart.getData());
            for (int i = 0; i < dane.wezlytablica.length; i++) {
                //dane.wezlytablica[i].getNode().setOnMouseClicked(mouseEvent -> System.out.println("dupa"));
                int finalI = i;
                dane.wezlytablica[i].data.getNode().setOnMouseClicked(mouseEvent -> najblizsiSasiedzi(finalI));
            }
        }


        XYChart.Series tmp;

        for (int k = 0; k < seriesArrayList.size(); k++) {
             tmp = seriesArrayList.get(k);
            if(tmp.getName().substring(tmp.getName().length() - 1).equals("O")){
                Set<Node> nodes = tmp.getChart().lookupAll(".chart-symbol");
                for (Node n : nodes) {
                        StringBuilder style = new StringBuilder();
                        style.append(" -fx-background-radius: 0;\n" +
                                "    -fx-shape: null;\n" +
                                "    -fx-padding: 5px;");
                        n.setStyle(style.toString());
                }
            }else{
                Set<Node> nodes = tmp.getChart().lookupAll(".chart-symbol");
                for (Node n : nodes) {
                    StringBuilder style = new StringBuilder();
                    style.append(" -fx-background-radius: 0;\n" +
                            "    -fx-shape: null;\n" +
                            "    -fx-padding: 5px;");
                    n.setStyle(style.toString());
                }
            }
        }

        /*for (int k = 0; k < seriesArrayList.size(); k++) {
            tmp = seriesArrayList.get(k);
            Node n = tmp.getNode().lookup(".default-color"+k+".chart-line-symbol");
            StringBuilder style = new StringBuilder();
            style.append(" -fx-background-radius: 0;\n" +
                    "                                    -fx-shape: null;");
            n.setStyle(style.toString());
        }*/
        lineChart.applyCss();

    }

    public void drawChart2(int kolumnaX, int kolumnaY) {
        ArrayList<XYChart.Series> seriesArrayList = new ArrayList<>();
        seriesArrayList.clear();
        final NumberAxis yAxis = new NumberAxis();
        final NumberAxis xAxis = new NumberAxis();
        final ScatterChart<Number, Number> lineChart = new ScatterChart<>(xAxis, yAxis);
        yAxis.setLabel("OSY");
        xAxis.setLabel("OSX");
        HashSet<String> h = new HashSet<String>();
        for(int i = 0; i<dane.zbior_uczacy.length; i++) {
            if(dane.zbior_uczacy[i][dane.zbior_uczacy[i].length-1]!=null &&!(dane.zbior_uczacy[i][dane.zbior_uczacy[i].length-1].equals("Y"))
                    && !(dane.zbior_uczacy[i][dane.zbior_uczacy[i].length-1].equals("default payment next month"))) {
                h.add(dane.zbior_uczacy[i][dane.zbior_uczacy[i].length-1]);

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
            System.out.println(dane.zbior_uczacy.length);
            Iterator<XYChart.Series> seriesIterator = seriesArrayList.iterator();
            System.out.println(seriesArrayList.toString());
            for(int i = 2; i<dane.zbior_uczacy.length; i++)
            {
                for (int k=0; k<seriesArrayList.size();k++) {
                    if ((dane.zbior_uczacy[i][dane.zbior_uczacy[i].length - 1]).equals(seriesArrayList.get(k).getName())) {
                        double axisX = Double.parseDouble(dane.zbior_uczacy[i][kolumnaX-1]);
                        double axisY = Double.parseDouble(dane.zbior_uczacy[i][kolumnaY-1]);
                        dane.wezlytablicaUczacy[i] = new Wezel(axisX,axisY,i);
                        seriesArrayList.get(k).getData().add(dane.wezlytablicaUczacy[i].getData());
                    }
                }
            }
            while (seriesIterator.hasNext()) lineChart.getData().addAll(seriesIterator.next());
            obszarWykresuUczacy.setData(lineChart.getData());
            /*Scene scene = new Scene(lineChart, 800, 600);
            Stage stage = new Stage();
            stage.setTitle("Wykres");
            stage.setScene(scene);
            stage.show();*/
        }else {
            System.out.println(dane.zbior_uczacy.length);
            Iterator<XYChart.Series> seriesIterator = seriesArrayList.iterator();
            System.out.println(seriesArrayList.toString());
            for (int i = 0; i < dane.zbior_uczacy.length; i++) {
                for (int k = 0; k < seriesArrayList.size(); k++) {
                    if ((dane.zbior_uczacy[i][dane.zbior_uczacy[i].length - 1]).equals(seriesArrayList.get(k).getName())) {
                        int axisX = Integer.parseInt(dane.zbior_uczacy[i][kolumnaX - 1]);
                        int axisY = Integer.parseInt(dane.zbior_uczacy[i][kolumnaY - 1]);
                        //seriesArrayList.get(k).getData().add(new XYChart.Data(axisX, axisY));
                        //XYChart.Data data = new XYChart.Data(axisX, axisY);
                        dane.wezlytablicaUczacy[i] = new Wezel(axisX,axisY,i);
                        dane.wezlytablicaUczacy[i].indeks = dane.porownanie[i];
                        seriesArrayList.get(k).getData().add(dane.wezlytablicaUczacy[i].getData());
                    }
                }
            }
            while (seriesIterator.hasNext()) lineChart.getData().addAll(seriesIterator.next());
            obszarWykresuUczacy.setData(lineChart.getData());
            /*Scene scene = new Scene(lineChart, 500, 400);
            Stage stage = new Stage();
            stage.setTitle("Wykres");
            stage.setScene(scene);
            stage.show();*/
            for (int i = 0; i < dane.wezlytablicaUczacy.length; i++) {
                //dane.wezlytablica[i].getNode().setOnMouseClicked(mouseEvent -> System.out.println("dupa"));
                int finalI = i;
                dane.wezlytablicaUczacy[i].data.getNode().setOnMouseClicked(mouseEvent -> najblizsiSasiedziUczacy(finalI));
            }

        }
    }

    public String[][][] plaszczyznaDecyzji(boolean zbior){
        int gestosc = 10;
        int x_min = Integer.MAX_VALUE;
        int x_max = Integer.MIN_VALUE;
        int y_min = Integer.MAX_VALUE;
        int y_max = Integer.MIN_VALUE;
        ArrayList<XYChart.Series> seriesArrayList = new ArrayList<>();
        seriesArrayList.clear();

        for(int i = 0; i<dane.daneOdczytane.length; i++){
            int x = Integer.parseInt(dane.daneOdczytane[i][Integer.parseInt(kolumnaX.getText())-1]);
            if(x > x_max){
                x_max = x;
            }
            if(x < x_min){
                x_min = x;
            }
            int y = Integer.parseInt(dane.daneOdczytane[i][Integer.parseInt(kolumnaY.getText())-1]);
            if(y > y_max){
                y_max = y;
            }
            if(y < y_min){
                y_min = y;
            }
        }
        x_min-=1;
        x_max+=1;
        y_min-=1;
        y_max+=1;

        max_height = y_max;
        max_width = x_max;

        /*double gestoscx =  (double) Math.abs(x_min-x_max)/gestosc;
        double gestoscy =  (double) Math.abs(y_min-y_max)/gestosc;*/
        int gestoscx =  Math.abs(x_min-x_max)*gestosc;
        int gestoscy =  Math.abs(y_min-y_max)*gestosc;
        double skalax =  (double) (x_max+1)/gestosc;
        double skalay =  (double) (y_max+1)/gestosc;
        skalax = (double)1/10;
        skalay = (double)1/10;
        String[][][] plaszczyzna = new String[gestoscx][gestoscy][dane.daneOdczytane[0].length-1];
        String[][][] plaszczyzna_klasy = new String[gestoscx][gestoscy][dane.daneOdczytane[0].length];
        for(int i = 0; i<plaszczyzna.length; i++){
            for(int j = 0; j<plaszczyzna[0].length; j++){
                for(int k = 0; k<plaszczyzna[0][0].length; k++){
                    plaszczyzna[i][j][k] = "0";
                    plaszczyzna_klasy[i][j][k] = "0";
                }
            }
        }
        /*for(int i = x_min; i<x_min; skalax+=skalax){
            plaszczyzna[i][Integer.parseInt(kolumnaX.getText())-1] = Double.toString((double)i);
            plaszczyzna[i][Integer.parseInt(kolumnaY.getText())-1] = Double.toString((double)i);
            plaszczyzna_klasy[i][Integer.parseInt(kolumnaX.getText())-1] = Double.toString(i*skalax);
            plaszczyzna_klasy[i][Integer.parseInt(kolumnaY.getText())-1] = Double.toString(i*skalay);
        }*/
        int tmp = 0;
        for(int z = 0; z<gestoscx; z++){
            for(int i = 0; i<gestoscy; i++){
                plaszczyzna[z][i][Integer.parseInt(kolumnaX.getText())-1] = Double.toString((double)(z*skalax)+x_min);
                plaszczyzna[z][i][Integer.parseInt(kolumnaY.getText())-1] = Double.toString((double)(i*skalay)+y_min);
                plaszczyzna_klasy[z][i][Integer.parseInt(kolumnaX.getText())-1] = Double.toString((double)(z*skalax)+x_min);
                plaszczyzna_klasy[z][i][Integer.parseInt(kolumnaY.getText())-1] = Double.toString((double)(i*skalay)+y_min);
            }
        }
        String[][] tmp_dane = new String[dane.daneOdczytane.length][dane.daneOdczytane[0].length];
        for(int i = 0; i<tmp_dane.length; i++){
            for(int j = 0; j<tmp_dane[i].length; j++){
                if(j == Integer.parseInt(kolumnaX.getText())-1 || j == Integer.parseInt(kolumnaY.getText())-1
                        || j == tmp_dane[i].length-1){
                    tmp_dane[i][j] = dane.daneOdczytane[i][j];
                }else{
                    tmp_dane[i][j] = "0";
                }
            }
        }
        for(int z = 0; z<gestoscx; z++){
            for(int i = 0; i<gestoscy; i++){
                if(zbior){
                    plaszczyzna_klasy[z][i][plaszczyzna_klasy[z][i].length-1] = dane.klasyfikujWektor2(plaszczyzna[z][i],tmp_dane)+"O";
                }else{
                    plaszczyzna_klasy[z][i][plaszczyzna_klasy[z][i].length-1] = dane.klasyfikujWektor2(plaszczyzna[z][i],dane.zbior_uczacy)+"O";
                }
            }
        }
        //System.out.println("Dziala plaszczyzna");
        //System.out.println("skalax "+skalax);
        //System.out.println("skalay "+skalay);
        /*for(int i = 0; i<plaszczyzna_klasy.length; i++){
            for(int j = 0; j<plaszczyzna_klasy[0].length; j++){
                System.out.print(plaszczyzna_klasy[i][j][Integer.parseInt(kolumnaX.getText())-1]+" ,");
                System.out.print(plaszczyzna_klasy[i][j][Integer.parseInt(kolumnaX.getText())-1]+" ,");
                System.out.print(plaszczyzna_klasy[i][j][plaszczyzna_klasy[i][j].length-1]);
            }
            System.out.println();
        }*/
        return plaszczyzna_klasy;
    }

    public void najblizsiSasiedzi(int nr){
        if(dane.parametrK >= 1){
            dane.flaga = true;
            //int nr2 = dane.wezlytablica[dane.porownanie[nr]].indeks;
            int nr2 = nr;

            System.out.println(nr+" "+nr2);



            output.appendText("Wspolrzedne: "+ dane.wezlytablica[nr].getXValue()+", "+dane.wezlytablica[nr].getYValue()+"\n");
            output.appendText("Numer: "+nr2+"\n");


            String[] w = new String[dane.daneOdczytane[nr2].length-1];
            for (int i=0;i<dane.daneOdczytane[nr2].length-1;i++){
                w[i]=dane.daneOdczytane[nr2][i];
            }
            //output.appendText("Wspolrzedne: "+w[0]+", "+w[1]+"\n");
            dane.klasyfikujWektor2(w,dane.daneOdczytane); ///

            int[] tab = dane.WybierzNajblizsiSasiedzi();

            for (int i=0;i<tab.length;i++){
                if(dane.wezlytablica[tab[i]].indeks != nr2){
                    output.appendText("SASIAD NR :"+dane.wezlytablica[tab[i]].indeks);
                    output.appendText("- ("+dane.wezlytablica[tab[i]].getXValue()+", "+dane.wezlytablica[tab[i]].getYValue()+")\n");
                    XYChart.Data xy = dane.wezlytablica[tab[i]].getData();
                    xy.getNode().setScaleX(1.6);
                    xy.getNode().setScaleY(1.6);
                }
            }
            output.appendText("....................................................................\n");
            dane.flaga = false;

            if(dane.flaga_sasiedzi_2) {
                obszarWykresu.setOnMouseClicked(mouseEvent -> wyrownajWykres(false));
                dane.flaga_sasiedzi_2=false;

            }else{
                obszarWykresu.setOnMouseClicked(null);
                dane.flaga_sasiedzi_2 = true;
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Nie wypełniono wszystkich pól");
            alert.setContentText("Aby wyswietlić najbliższych sąsiadów nalezy podać parametr K");
            alert.showAndWait();
        }
    }

    public void najblizsiSasiedziUczacy(int nr){
        int nr2 = dane.wezlytablica[dane.porownanie[nr]].indeks;
        dane.flaga = true;
        System.out.println(nr+" "+nr2);

        output.appendText("Wspolrzedne: "+ dane.wezlytablicaUczacy[nr].getXValue()+", "+dane.wezlytablicaUczacy[nr].getYValue()+"\n");
        output.appendText("Numer: "+nr2+"\n");
        String[] w = new String[dane.daneOdczytane[nr2].length-1];
        for (int i=0;i<dane.daneOdczytane[nr2].length-1;i++){
            w[i]=dane.daneOdczytane[nr2][i];
        }
        //output.appendText("Wspolrzedne: "+w[0]+", "+w[1]+"\n");
        dane.klasyfikujWektor2(w,dane.zbior_uczacy); ///

        int[] tab = dane.WybierzNajblizsiSasiedzi();

        for (int i=0;i<tab.length;i++){
            if(dane.wezlytablicaUczacy[tab[i]].indeks != nr2){
                output.appendText("SASIAD NR :"+dane.wezlytablicaUczacy[tab[i]].indeks);
                output.appendText("- ("+dane.wezlytablicaUczacy[tab[i]].getXValue()+", "+dane.wezlytablicaUczacy[tab[i]].getYValue()+")\n");
                XYChart.Data xy = dane.wezlytablicaUczacy[tab[i]].getData();
                xy.getNode().setScaleX(1.6);
                xy.getNode().setScaleY(1.6);
            }
        }
        output.appendText("....................................................................\n");
        dane.flaga = false;

        if(dane.flaga_sasiedzi) {

            //dane.wezlytablicaUczacy[nr2].data.getNode().setOnMouseClicked(mouseEvent -> wyrownajWykres(true));
            obszarWykresuUczacy.setOnMouseClicked(mouseEvent -> wyrownajWykres(true));
            dane.flaga_sasiedzi=false;

        }else{
            obszarWykresuUczacy.setOnMouseClicked(null);
            dane.flaga_sasiedzi = true;
        }

    }



    public void wyrownajWykres(boolean b){
        if(b){
            for (int i=0;i<dane.wezlytablicaUczacy.length;i++){
                XYChart.Data xy = dane.wezlytablicaUczacy[i].getData();
                xy.getNode().setScaleX(1);
                xy.getNode().setScaleY(1);
            }
            //dane.flaga_sasiedzi=true;

        }else{
            for (int i=0;i<dane.wezlytablica.length;i++){
                XYChart.Data xy = dane.wezlytablica[i].getData();
                xy.getNode().setScaleX(1);
                xy.getNode().setScaleY(1);
            }
            //dane.flaga_sasiedzi_2=true;
            //

        }

        //System.out.println("wyrównuje");

        /*if(!kolumnaX.getText().isEmpty() && !kolumnaY.getText().isEmpty()){
            rysujWykres(1);
            if(!rozmiar.getText().isEmpty()){
                rysujWykres(2);
            }
        }*/
    }
}