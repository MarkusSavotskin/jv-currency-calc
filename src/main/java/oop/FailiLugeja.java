package oop;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FailiLugeja {

    //loeKursid võtab argumendina www.eestipank.ee/valuutakursid lehelt currency-rates.csv faili
    public static List<Valuuta> loeKursid(String failinimi) throws Exception {

        //Tühi list valuuta isendite salvestamiseks
        List<Valuuta> kursid = new ArrayList<>();

        //Skänner faili lugemiseks
        try (Scanner sc = new Scanner(new File(failinimi), "UTF-8")) {
            //Arvestame reanumbrit et faili alguses read vahele jätta
            int reanumber = 0;
            while (sc.hasNextLine()) {
                String rida = sc.nextLine();
                if (reanumber > 2) {

                    String[] tükid = rida.split(",");
                    String tähis = tükid[0].substring(1, tükid[0].length() - 1);
                    double kurss = Double.parseDouble(tükid[1].substring(1, tükid[1].length() - 1));
                    //Salvestame uue valuuta isendi listi
                    kursid.add(new Valuuta(tähis, kurss));
                }
                reanumber++;
            }
        }
        return kursid;
    }
    //loeKuupäev kasutab sama faili, mis loeKursid, tagastab kuupäeva
    public static String loeKuupaev(String failinimi) throws Exception {

        String kuupaev = new String();

        //Skänner faili lugemiseks
        try (Scanner sc = new Scanner(new File(failinimi), "UTF-8")) {
            //Arvestame reanumbrit et leida oige rida
            int reanumber = 0;
            while (sc.hasNextLine()) {
                String rida = sc.nextLine();
                if (reanumber == 1) {

                    String[] tükid = rida.split(",")[0].split(" ");
                    kuupaev = tükid[tükid.length-1].split("\"")[0];

                }
                reanumber++;
            }
        }
        return kuupaev;
    }

}
