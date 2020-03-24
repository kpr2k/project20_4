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
                if(NumberUtils.isParsable(liniaDaneString[i]))
                    liniaDouble[i] = Integer.parseInt(liniaDaneString[i]);
            }
            // Dodajemy tablicę z serią danych do tablicy z wszystkimi danymi
            daneOdczytane[nrLinii] = liniaDaneString;
            nrLinii++;
        }

    }

    public String klasyfikujWektor(String[] w){
        String klasa = "";
        double L=0;
        double tmp;
        double p = 2;
        double iloraz = 1/p;
        for(int i = 0; i<daneOdczytane.length; i++)
        {
            tmp=0;
            for(int j =0; j<daneOdczytane[i].length;j++){
                if(NumberUtils.isParsable(daneOdczytane[i][j])) {
                    tmp += pow(abs((Integer.parseInt(daneOdczytane[i][j])) - Integer.parseInt(w[j])), p);
                }
            }
            tmp=pow(tmp,iloraz);
            System.out.println("i = "+i+", L= "+tmp);
            if(L==0||tmp<L){
                L=tmp;
                for(int k =0; k<daneOdczytane[i].length;k++){
                    if( daneOdczytane[i][k] instanceof String ) {
                        klasa = daneOdczytane[i][k];
                    }
                }
            }
        }
        System.out.print("L= "+L+", ");
        return klasa;
    }

    public void podzialNaZbiory() {
        String[][] dane1 =  daneOdczytane;
        Integer[] dane2 = new Integer[daneOdczytane.length];

        for(int i=0;i<dane1.length;i++){
            dane2[i] = i;
        }
        List<Integer> lista = Arrays.asList(dane2);

        Collections.shuffle(lista);

        int ind1 = (lista.size() * 8)/10;
        String[][] zbior_uczacy = new String[ind1][dane1.length];
        String[][] zbior_testowy = new String[dane1.length-ind1][dane1.length];


        int zm;
        List<Integer> l1 =  lista.subList(0, ind1);
        for(int i=0;i<l1.size();i++){
            zm = l1.get(i);
            zbior_uczacy[i] = dane1[zm];
        }

        List<Integer> l2 = lista.subList(ind1, lista.size());
        for(int i=0;i<l2.size();i++){
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


}
