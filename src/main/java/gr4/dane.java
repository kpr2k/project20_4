package gr4;

import org.apache.commons.lang3.math.NumberUtils;

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
    public String[][] zbior_uczacy;
    public String[][] zbior_testowy;
    public String[][] tabL;
    public static int typ_pliku = 0;
    public static int ilosc;

    public void odczytajPlik(String nazwaPliku) {
        // Tworzymy obiekt typu Path
        Path sciezka = Paths.get(nazwaPliku);
        // Lista do przechowywania kolejnych linii odczytanych pliku jako String
        ArrayList<String> odczyt = new ArrayList();
        try {
            // Linie pliku zostaja umieszoczne w liscie
            odczyt = (ArrayList) Files.readAllLines(sciezka);
        } catch (IOException ex) {
            System.out.println("Brak pliku!");
        }

        // Tablica dla odczytanych danych
        daneOdczytane = new String[odczyt.size()][];
        // Indeks linii
        int nrLinii = 0;
        // Pobranie kolejnych linii z listy
        for (String linia : odczyt) {
            // Rozbijamy linię (przedzielone przecinkami)
            String[] liniaDaneString = linia.split(",");
            // Tablica do przechowania danych w fomie liczb double
            int[] liniaDouble = new int[liniaDaneString.length];
            // Pętla pobiera z tablicy String liczbe i konwertuje ją na double i zapisuje w tablicy double[]
            for (int i = 0; i < liniaDouble.length; i++) {
                if (NumberUtils.isParsable(liniaDaneString[i]))
                    liniaDouble[i] = Integer.parseInt(liniaDaneString[i]);
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
    }

    public String klasyfikujWektor(String[] w, double p, int k, String[][] zbiorUczacy) {
        tabL = new String[k][2]; // tablica najbliższych sąsiadów
        String klasa = "";
        double L = 0;
        double tmp;
        int tmp2 = 0;
        double max = 0;
        double iloraz = 1 / p;
        boolean flag = false;
        for (int i = 0; i < zbiorUczacy.length; i++) { // zewnętrzena pętla po zbiorze uczących
            tmp = 0;
            for (int j = 0; j < zbiorUczacy[i].length; j++) { // wewnętrzna pętla po zbiore uczących
                if (NumberUtils.isParsable(zbiorUczacy[i][j])) {
                    tmp += pow(abs((Integer.parseInt(zbiorUczacy[i][j])) - Integer.parseInt(w[j])), p);
                }
                if (zbiorUczacy[i][j] instanceof String) {
                    klasa = zbiorUczacy[i][j];
                }
            }
            tmp = pow(tmp, iloraz);
            System.out.println("i = " + i + ", " + klasa + ", L= " + tmp);
            for (int n = 0; n < tabL.length; n++) { // pętla po tablicy najbliższych sąsiadów
                if (tabL[n][1] == null) { // wypełnienie tablicy startowymi wartościami
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

        System.out.print("\nTablica z najbliższymi sasiadami i ich długość:");
        for (int i = 0; i < tabL.length; i++) {
            System.out.println();
            for (int j = 0; j < tabL[i].length; j++) {
                System.out.print(tabL[i][j] + ", ");
            }
        }
        System.out.println("\n\nPosortowana tablica z najbliższymi sąsiadami:");
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
        return klasa;
    }


    public String klasyfikujWalidacja(String[] w, double p, int kSasiadow, int kWalidacja ) {

        int from = 0;
        int to  = daneOdczytane.length/kWalidacja;
        String[] wyniki = new String[kWalidacja];
        String klasa = "";
        for(int k = 0; k < kWalidacja; k++) {
            System.out.println("-----------------------Walidacja k = "+k+"-----------------------");
            String[][] uczacy = new String[((kWalidacja-1)*daneOdczytane.length)/kWalidacja][daneOdczytane[0].length];
            int j = 0;
            for (int i = 0; i < daneOdczytane.length; i++ ) {
                    if(!(i>=from && i<=to)) {
                        uczacy[j]=daneOdczytane[i];
                        j++;
                }
            }
            from = to + 1;
            to = from + (daneOdczytane.length/kWalidacja);
            if(to>=daneOdczytane.length) {
                to=daneOdczytane.length-1;
                from=to-daneOdczytane.length/kWalidacja;
            }
            wyniki[k] = klasyfikujWektor(w, p, kSasiadow, uczacy);
        }
        int[] results = new int[kWalidacja]; // ilości powtórzeń
        Arrays.sort(wyniki);
        int licznik = 1;
        int max = 0;
        int wierszMaxa = 0;
        for (int i = 1; i < wyniki.length; i++) { // zliczanie powtórzeń
            if (wyniki[i].equals(wyniki[i - 1]) == true) {
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
        results[wyniki.length - 1] = licznik;
        if (results[wyniki.length - 1] > max) {
            max = results[wyniki.length - 1];
            wierszMaxa = wyniki.length - 1;
        }
        klasa = wyniki[wierszMaxa]; // wynik końcowy

        System.out.println("\n\nPosortowana tablica z najbliższymi sąsiadami:");
        for (int i = 0; i < wyniki.length; i++) {
            System.out.print(wyniki[i] + ", ");
        }
        System.out.println("\n\nTablica z ilością powtórzeń");
        for (int i = 0; i < results.length; i++) {
            System.out.print(results[i] + ", ");
        }
        System.out.println();
        System.out.println();
        for (int i = 0; i < wyniki.length; i++) { //Wyświetlanie
            if (results[i] > 0) {
                System.out.println(wyniki[i] + " - " + results[i] + " (powtórzenia)");
            }
        }
        return klasa;




    }



    public void podzialNaZbiory(int rozmiar_uczacy) {

        String[][] dane1 = daneOdczytane;
        Integer[] dane2 = new Integer[daneOdczytane.length];

        for (int i = 0; i < dane1.length; i++) {
            dane2[i] = i;
        }
        List<Integer> lista = Arrays.asList(dane2);

        Collections.shuffle(lista);
        int ind1 = rozmiar_uczacy;
        //int ind1 = (lista.size() * rozmiar_uczacy) / 100;
        String[][] zbior_uczacy = new String[ind1][dane1.length];
        String[][] zbior_testowy = new String[dane1.length - ind1][dane1.length];


        int zm;
        List<Integer> l1 = lista.subList(0, ind1);
        for (int i = 0; i < l1.size(); i++) {
            zm = l1.get(i);
            zbior_uczacy[i] = dane1[zm];
        }

        List<Integer> l2 = lista.subList(ind1, lista.size());
        for (int i = 0; i < l2.size(); i++) {
            zm = l2.get(i);
            zbior_testowy[i] = dane1[zm];
        }

        this.zbior_uczacy = zbior_uczacy;
        this.zbior_testowy = zbior_testowy;

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

    public double wyznaczDokladnosc(double p, int k, String [][] zbiorUczacy){

        double h_x;
        int zbior_eq = 0;
        for(int i = 0; i<zbior_testowy.length; i++){
            //System.out.println((zbior_testowy[i][zbior_testowy[i].length-1]+" = "+klasyfikujWektor(zbior_testowy[i], p, k)));
            if(zbior_testowy[i][zbior_testowy[i].length-1].equals(klasyfikujWektor(zbior_testowy[i], p, k, zbiorUczacy))){
                zbior_eq++;
            }
        }
        h_x = ((double)zbior_eq/zbior_testowy.length);
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
}
