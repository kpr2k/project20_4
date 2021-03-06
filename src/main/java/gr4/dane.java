package gr4;

import gr4.controllers.Controller;
import javafx.scene.Node;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import org.apache.commons.lang3.math.NumberUtils;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.pow;

public class dane {
    public static String[][] daneOdczytane;
    public static String[][] zbior_uczacy;
    public static String[][] zbior_testowy;
    public static String[][] tabL;

    public static String[][] zbior_uczacy_odleglosci;
    public static String[][] zbior_danych_odleglosci;

    public static String[][][] obszary;
    public static int typ_pliku = 0;
    public static int ilosc;
    public static int parametrP;
    public static int parametrK;
    public static int rozmiar_uczacy = 0;
    public static int parametrKwalidacja;
    public static Wezel[] wezlytablica;
    public static Wezel[] wezlytablicaUczacy;
    public static Wezel[] wezlytablicaTest;
    public static Wezel[] wezlytablicaObszary;
    public static int[] najblizsiSasiedzi;
    public static boolean flaga = false;
    public static int[] porownanie;
    public static int[] porownanie2;
    public static boolean flaga_sasiedzi = false;
    public static boolean flaga_sasiedzi_2 = false;
    public static boolean flaga_parametrK = false;
    public static boolean flaga_parametrP = false;


    public static void setParametry(int p, int k, int r){
        parametrP = p;
        parametrK = k;
        rozmiar_uczacy = r;
        podzialNaZbiory();
        wezlytablicaUczacy = new Wezel[zbior_uczacy.length];
        wezlytablicaTest = new Wezel[zbior_testowy.length];
    }
    public static void setParametry(int p, int k){
        parametrP = p;
        parametrK = k;
        flaga_parametrP = true;
        flaga_parametrK = true;
        //rozmiar_uczacy = 0;
        //podzialNaZbiory();
        //wezlytablicaUczacy = new Wezel[zbior_uczacy.length];
        //wezlytablicaTest = new Wezel[zbior_testowy.length];
    }

    public static void setDaneZbiory(int r){
        rozmiar_uczacy = r;
        podzialNaZbiory();
        wezlytablicaUczacy = new Wezel[zbior_uczacy.length];
        wezlytablicaTest = new Wezel[zbior_testowy.length];
    }

    public static boolean sprawdzRozmiar(int r){
        if(r > daneOdczytane.length-1){
            return false;
        }else{
            return true;
        }
    }

    public static void setParametrKWalidacja(int kw){
        parametrKwalidacja = kw;
    }

    public void odczytajPlik(String nazwaPliku) {
        // Tworzymy obiekt typu Path
        Path sciezka = Paths.get(nazwaPliku);
        // Lista do przechowywania kolejnych linii odczytanych pliku jako String
        ArrayList<String> odczyt = new ArrayList();
        try {
            // Linie pliku zostaja umieszoczne w liscie
            odczyt = (ArrayList) Files.readAllLines(sciezka);
            for(int i = 0; i < odczyt.size();i++) {
                if (odczyt.get(i).isEmpty()) odczyt.remove(i);
            }
        } catch (IOException ex) {
            System.out.println("Brak pliku!");
        }
        zbior_uczacy_odleglosci=null;
        zbior_danych_odleglosci=null;
        rozmiar_uczacy = 0;

        // Tablica dla odczytanych danych
        daneOdczytane = new String[odczyt.size()][];
        // Indeks linii
        int nrLinii = 0;
        // Pobranie kolejnych linii z listy
        for (String linia : odczyt) {
            // Rozbijamy linię (przedzielone przecinkami)
            String[] liniaDaneString = linia.split(",");
            // Tablica do przechowania danych w fomie liczb double
            double[] liniaDouble = new double[liniaDaneString.length];
            // Pętla pobiera z tablicy String liczbe i konwertuje ją na double i zapisuje w tablicy double[]
            for (int i = 0; i < liniaDouble.length; i++) {
                if (NumberUtils.isParsable(liniaDaneString[i]))
                    liniaDouble[i] = Double.parseDouble(liniaDaneString[i]);
            }
            // Dodajemy tablicę z serią danych do tablicy z wszystkimi danymi
            daneOdczytane[nrLinii] = liniaDaneString;
            nrLinii++;
        }
        if(daneOdczytane[0].length == 3) {
            typ_pliku = 1;
            ilosc = 3;
        }else if(daneOdczytane[0].length == 10) {
            typ_pliku = 2;
            ilosc = 10;
        }else{
            typ_pliku = 3;
            ilosc = 25;
        }
        podzialNaZbiory();
        wezlytablica = new Wezel[daneOdczytane.length];
        wezlytablicaUczacy = new Wezel[zbior_uczacy.length];
        wezlytablicaTest = new Wezel[zbior_testowy.length];
    }

    public static String klasyfikujWektor(String[] w, String[][] zbiorUczacy) {
        double p = parametrP;
        int k = parametrK;
        if(flaga){
            k++;
        }
        tabL = new String[k][3]; // tablica najbliższych sąsiadów
        String klasa = "";
        double L = 0;
        double tmp;
        int tmp2 = 0;
        double max = 0;
        double iloraz = 1 / p;
        boolean flag = false;
        for (int i = 0; i < w.length; i++) {
            System.out.print(w[i]+", ");
        }
        zbior_uczacy_odleglosci = new String[zbiorUczacy.length][(zbiorUczacy[0].length)+1];
        zbior_danych_odleglosci = new String[daneOdczytane.length][(daneOdczytane[0].length)+1];;
        for (int i = 0; i < daneOdczytane.length; i++) { // zewnętrzena pętla po zbiorze danych
            tmp = 0;
            for (int j = 0; j < daneOdczytane[i].length; j++) { // wewnętrzna pętla po zbiore danych
                zbior_danych_odleglosci[i][j] = daneOdczytane[i][j];
                if (NumberUtils.isParsable(daneOdczytane[i][j]) && j < daneOdczytane[i].length - 1) {
                    tmp += pow(abs((Double.parseDouble(daneOdczytane[i][j])) - Double.parseDouble(w[j])), p);
                }
                if (daneOdczytane[i][j] instanceof String || j == daneOdczytane[i].length - 1) {
                    klasa = daneOdczytane[i][j];
                }
            }
            tmp = pow(tmp, iloraz);
            zbior_danych_odleglosci[i][daneOdczytane[0].length] = Double.toString(tmp);
        }
        for (int i = 0; i < zbiorUczacy.length; i++) { // zewnętrzena pętla po zbiorze uczących
            tmp = 0;
            for (int j = 0; j < zbiorUczacy[i].length; j++) { // wewnętrzna pętla po zbiore uczących
                zbior_uczacy_odleglosci[i][j]=zbiorUczacy[i][j];
                if (NumberUtils.isParsable(zbiorUczacy[i][j]) && j<zbiorUczacy[i].length-1 ) {
                    tmp += pow(abs((Double.parseDouble(zbiorUczacy[i][j])) - Double.parseDouble(w[j])), p);
                }
                if (zbiorUczacy[i][j] instanceof String || j==zbiorUczacy[i].length-1) {
                    klasa = zbiorUczacy[i][j];
                }
            }
            tmp = pow(tmp, iloraz);
            zbior_uczacy_odleglosci[i][zbiorUczacy[0].length] = Double.toString(tmp);
            //System.out.println("i = " + i + ", " + klasa + ", L= " + tmp);
            for (int n = 0; n < tabL.length; n++) { // pętla po tablicy najbliższych sąsiadów
                if (tabL[n][1] == null) { // wypełnienie tablicy startowymi wartościami
                    tabL[n][2] = Integer.toString(i);
                    tabL[n][1] = Double.toString(tmp);
                    tabL[n][0] = klasa;
                    max = Double.parseDouble(tabL[0][1]);
                    tmp2 = 0;
                    break;
                } else if (i >= tabL.length) {
                    flag = true;
                    if (Double.parseDouble(tabL[n][1]) > max) { // wyznaczenie maksymalnej odległości w tablicy najbliższych sąsiadów
                        max = Double.parseDouble(tabL[n][1]);
                        tmp2 = n;
                    }
                }
            }
            if (Double.parseDouble(tabL[tmp2][1]) > tmp && flag == true) { // zamiana maxa na mniejszą wartość z tablicy uczących
                tabL[tmp2][2] = Integer.toString(i);
                tabL[tmp2][1] = Double.toString(tmp);
                tabL[tmp2][0] = klasa;
                max = Double.parseDouble(tabL[0][1]);
                tmp2 = 0;
            }
        }
        int[] results = new int[k]; // ilości powtórzeń
        String[] tabKlasy = new String[tabL.length]; // sąsiadujące klasy
        for (int i = 0; i < tabL.length; i++) {
            tabKlasy[i] = tabL[i][0];
        }
        Arrays.sort(tabKlasy);
        int licznik = 1;
        max = 0;
        int wierszMaxa = 0;
        for (int i = 1; i < tabKlasy.length; i++) { // zliczanie powtórzeń
            if (tabKlasy[i].equals(tabKlasy[i - 1]) == true) {
                licznik++;
            } else {
                results[i - 1] = licznik;
                licznik = 1;
                if (results[i - 1] > max) {
                    max = results[i - 1];
                    wierszMaxa = i - 1;
                }
            }
        }
        results[tabKlasy.length - 1] = licznik;
        if (results[tabKlasy.length - 1] > max) {
            max = results[tabKlasy.length - 1];
            wierszMaxa = tabKlasy.length - 1;
        }
        klasa = tabKlasy[wierszMaxa]; // wynik końcowy

        najblizsiSasiedzi = new int[tabL.length];
        for (int i = 0; i < tabL.length; i++) {
            najblizsiSasiedzi[i] = Integer.parseInt(tabL[i][2]);
        }

        System.out.print("\nTablica z najbliższymi sasiadami i ich długość:");
        for (int i = 0; i < tabL.length; i++) {
            System.out.println();
            for (int j = 0; j < tabL[i].length; j++) {
                System.out.print(tabL[i][j] + ", ");
            }
        }
        /*System.out.println("\n\nPosortowana tablica z najbliższymi sąsiadami:");
        for (int i = 0; i < tabKlasy.length; i++) {
            System.out.print(tabKlasy[i] + ", ");
        }
        System.out.println("\n\nTablica z ilością powtórzeń");
        for (int i = 0; i < results.length; i++) {
            System.out.print(results[i] + ", ");
        }
        System.out.println();
        System.out.println();
        for (int i = 0; i < tabKlasy.length; i++) { //Wyświetlanie
            if (results[i] > 0) {
                System.out.println(tabKlasy[i] + " - " + results[i] + " (powtórzenia)");
            }
        }
        System.out.println("\nWynik: "+klasa+"\n");
        */
        System.out.println(klasa);
        return klasa;
    }

    public static String klasyfikujWektor2(String[] w, String[][] zbiorUczacy) {
        double p = parametrP;
        int k = parametrK;
        if(flaga){
            k++;
        }
        tabL = new String[k][3]; // tablica najbliższych sąsiadów
        String klasa = "";
        double L = 0;
        double tmp;
        int tmp2 = 0;
        double max = 0;
        double iloraz = 1 / p;
        boolean flag = false;
        for (int i = 0; i < w.length; i++) {
            System.out.print(w[i]+", ");
        }
        for (int i = 0; i < zbiorUczacy.length; i++) { // zewnętrzena pętla po zbiorze uczących
            tmp = 0;
            for (int j = 0; j < zbiorUczacy[i].length; j++) { // wewnętrzna pętla po zbiore uczących
                if (NumberUtils.isParsable(zbiorUczacy[i][j]) && j<zbiorUczacy[i].length-1 ) {
                    tmp += pow(abs((Double.parseDouble(zbiorUczacy[i][j])) - Double.parseDouble(w[j])), p);
                }
                if (zbiorUczacy[i][j] instanceof String || j==zbiorUczacy[i].length-1) {
                    klasa = zbiorUczacy[i][j];
                }
            }
            tmp = pow(tmp, iloraz);
            //System.out.println("i = " + i + ", " + klasa + ", L= " + tmp);
            for (int n = 0; n < tabL.length; n++) { // pętla po tablicy najbliższych sąsiadów
                if (tabL[n][1] == null) { // wypełnienie tablicy startowymi wartościami
                    tabL[n][2] = Integer.toString(i);
                    tabL[n][1] = Double.toString(tmp);
                    tabL[n][0] = klasa;
                    max = Double.parseDouble(tabL[0][1]);
                    tmp2 = 0;
                    break;
                } else if (i >= tabL.length) {
                    flag = true;
                    if (Double.parseDouble(tabL[n][1]) > max) { // wyznaczenie maksymalnej odległości w tablicy najbliższych sąsiadów
                        max = Double.parseDouble(tabL[n][1]);
                        tmp2 = n;
                    }
                }
            }
            if (Double.parseDouble(tabL[tmp2][1]) > tmp && flag == true) { // zamiana maxa na mniejszą wartość z tablicy uczących
                tabL[tmp2][2] = Integer.toString(i);
                tabL[tmp2][1] = Double.toString(tmp);
                tabL[tmp2][0] = klasa;
                max = Double.parseDouble(tabL[0][1]);
                tmp2 = 0;
            }
        }
        int[] results = new int[k]; // ilości powtórzeń
        String[] tabKlasy = new String[tabL.length]; // sąsiadujące klasy
        for (int i = 0; i < tabL.length; i++) {
            tabKlasy[i] = tabL[i][0];
        }
        Arrays.sort(tabKlasy);
        int licznik = 1;
        max = 0;
        int wierszMaxa = 0;
        for (int i = 1; i < tabKlasy.length; i++) { // zliczanie powtórzeń
            if (tabKlasy[i].equals(tabKlasy[i - 1]) == true) {
                licznik++;
            } else {
                results[i - 1] = licznik;
                licznik = 1;
                if (results[i - 1] > max) {
                    max = results[i - 1];
                    wierszMaxa = i - 1;
                }
            }
        }
        results[tabKlasy.length - 1] = licznik;
        if (results[tabKlasy.length - 1] > max) {
            max = results[tabKlasy.length - 1];
            wierszMaxa = tabKlasy.length - 1;
        }
        klasa = tabKlasy[wierszMaxa]; // wynik końcowy

        najblizsiSasiedzi = new int[tabL.length];
        for (int i = 0; i < tabL.length; i++) {
            najblizsiSasiedzi[i] = Integer.parseInt(tabL[i][2]);
        }

        return klasa;
    }

    public static double klasyfikujWalidacja(String[] w) {
        String[][] zbior_uczacy_tmp=null;
        String[][] zbior_testowy_tmp=null;
        int kWalidacja = parametrKwalidacja;
        int from = 0;
        int to  = daneOdczytane.length/kWalidacja;
        double[] wyniki = new double[kWalidacja];
        double dokladnosc = 0;
        double sredniaWynikow = 0;
        List<String[]> listaDanychOdczytanych = new ArrayList<>();
        String [][] wymieszaneDane = new String[daneOdczytane.length][daneOdczytane[0].length];

        // Wymieszanie danych odczytanych
        for(int i = 0; i < daneOdczytane.length; i++) {
            listaDanychOdczytanych.add(daneOdczytane[i]);
        }
        Collections.shuffle(listaDanychOdczytanych);
        for(int i = 0; i < wymieszaneDane.length; i++) {
            wymieszaneDane[i] = listaDanychOdczytanych.get(i);
        }

        System.out.println("\nDane wymieszane");
        for(int i = 0; i < wymieszaneDane.length; i++) {
            System.out.print("id_"+i+" ");
            for(int j = 0; j < wymieszaneDane[i].length; j++) {
                System.out.print(wymieszaneDane[i][j]+" ");
            }
            System.out.print("\n");
        }

        // Podział danych na zbiory
        for(int k = 0; k < kWalidacja; k++) {
            System.out.println("\n-----------------------Walidacja k = "+k+"-----------------------");
            zbior_uczacy_tmp = new String[wymieszaneDane.length-(wymieszaneDane.length/kWalidacja)][wymieszaneDane[0].length];
            zbior_testowy_tmp = new String[to-from][wymieszaneDane[0].length];

            int indeksUczacych = 0;
            int indeksTestujacych = 0;
            for (int i = 0; i < wymieszaneDane.length; i++ ) {
                if(!(i>=from && i<to)) {
                    zbior_uczacy_tmp[indeksUczacych]=wymieszaneDane[i];
                    indeksUczacych++;
                }
                else {
                    zbior_testowy_tmp[indeksTestujacych]=wymieszaneDane[i];
                    indeksTestujacych++;
                }
            }
            System.out.println("Zbiór uczących");
            for(int i = 0; i < zbior_uczacy_tmp.length; i++) {
                System.out.print("id_"+i+" ");
                for(int j = 0; j < zbior_uczacy_tmp[i].length; j++) {
                    System.out.print(zbior_uczacy_tmp[i][j]+" ");
                }
                System.out.print("\n");
            }
            System.out.println("\nZbiór testujących");
            for(int i = 0; i < zbior_testowy_tmp.length; i++) {
                System.out.print("id_"+i+" ");
                for(int j = 0; j < zbior_testowy_tmp[i].length; j++) {
                    System.out.print(zbior_testowy_tmp[i][j]+" ");
                }
                System.out.print("\n");
            }
            System.out.print("\n");
            dokladnosc = wyznaczDokladnosc(zbior_uczacy_tmp);
            wyniki[k]=dokladnosc;

            from = to;
            to = from + (wymieszaneDane.length/kWalidacja);
            if(to>=wymieszaneDane.length) {
                to=wymieszaneDane.length;
                from=to-wymieszaneDane.length/kWalidacja;
            }
            /*
            wyniki[k] = klasyfikujWektor(w, p, kSasiadow, uczacy);
            */
        }
        System.out.println("----------------------------------------");
        System.out.println("Wyniki dokładności:");
        for(int i = 0; i < wyniki.length; i++) {
            System.out.println("k = "+i+", "+wyniki[i]);
            sredniaWynikow += wyniki[i];
        }
        sredniaWynikow = sredniaWynikow/wyniki.length;
        System.out.print("Średnia dokładnośc: ");

        return sredniaWynikow;
    }
  
    public static void podzialNaZbiory() {
        String[][] dane1 = daneOdczytane;
        Integer[] dane2 = new Integer[daneOdczytane.length];

        for (int i = 0; i < dane1.length; i++) {
            dane2[i] = i;
        }
        List<Integer> lista = Arrays.asList(dane2);
        Collections.shuffle(lista);
        porownanie = new int[daneOdczytane.length];
        porownanie2 = new int[daneOdczytane.length];
        for (int i = 0; i < daneOdczytane.length; i++) {
            porownanie[i] = dane2[i];
            porownanie2[dane2[i]] = i;
        }
        int ind1;
        if(rozmiar_uczacy == 0) {
            ind1 = daneOdczytane.length/2;
        }else{
            ind1 = rozmiar_uczacy;
        }
        //int ind1 = (lista.size() * rozmiar_uczacy) / 100;
        String[][] zbior_uczacy2 = new String[ind1][dane1.length];
        String[][] zbior_testowy2 = new String[dane1.length - ind1][dane1.length];
        int zm;
        List<Integer> l1 = lista.subList(0, ind1);
        for (int i = 0; i < l1.size(); i++) {
            zm = l1.get(i);
            zbior_uczacy2[i] = dane1[zm];
        }
        List<Integer> l2 = lista.subList(ind1, lista.size());
        for (int i = 0; i < l2.size(); i++) {
            zm = l2.get(i);
            zbior_testowy2[i] = dane1[zm];
        }
        zbior_uczacy = zbior_uczacy2;
        zbior_testowy = zbior_testowy2;

        // Wypisanie zbiorów
        System.out.println();
        System.out.println("Podział na zbiory:");
        System.out.println("Zbiór uczący");
        for (int i = 0; i < zbior_uczacy.length; i++) {
            for (int j = 0; j < zbior_uczacy[0].length; j++) {
                System.out.print(zbior_uczacy[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Zbiór testowy");
        for (int i = 0; i < zbior_testowy.length; i++) {
            for (int j = 0; j < zbior_testowy[0].length; j++) {
                System.out.print(zbior_testowy[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static double wyznaczDokladnosc(String [][] zbiorUczacy){
        double h_x;
        int zbior_eq = 0;
        for(int i = 0; i<zbior_testowy.length; i++){
            /*System.out.println("Wektor testujący o id = "+i);
            System.out.println("Klasyfikacja: "+zbior_testowy[i][zbior_testowy[i].length-1]);
            System.out.print("Klasyfikacja knn: ");*/
            //System.out.println((zbior_testowy[i][zbior_testowy[i].length-1]+" = "+klasyfikujWektor(zbior_testowy[i], p, k)));
            if(zbior_testowy[i][zbior_testowy[i].length-1].equals(klasyfikujWektor2(zbior_testowy[i], zbiorUczacy))){
                zbior_eq++;
            }
            //System.out.println();
        }
        h_x = ((double)zbior_eq/zbior_testowy.length);
        //System.out.println("Dokladnosc klasyfikacji: "+h_x);
        return h_x;
    }

    public static void dodajPunkt(String[] t){
        if(typ_pliku != 0){
            if(t.length == ilosc) {
            String[] wsp = new String[ilosc];
            for(int i = 0; i < ilosc; i++) {
                wsp[i] = t[i];
            }
            //wsp[ilosc-1] = klasyfikujWektor(t,1,1);
            String[][] daneOdczytane_nowe = new String[daneOdczytane.length+1][daneOdczytane[0].length];
            for (int i = 0; i < daneOdczytane.length; i++) {
                for (int j = 0; j < daneOdczytane[0].length; j++) {
                    daneOdczytane_nowe[i][j] = daneOdczytane[i][j];
                }
            }
            for (int j = 0; j < daneOdczytane[0].length; j++) {
                daneOdczytane_nowe[daneOdczytane_nowe.length-1][j] = wsp[j];
            }
            daneOdczytane = daneOdczytane_nowe;
            }else{
                System.out.println("Podano za mało danych.");
            }
        }else{
            System.out.println("Nie wczytano pliku z danymi");
        }
    }

    public static int[] WybierzNajblizsiSasiedzi() {
        return najblizsiSasiedzi;
    }

}
