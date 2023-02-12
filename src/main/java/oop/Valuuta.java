package oop;

import java.util.Collections;
import java.util.List;

public class Valuuta {

    List<String> terad;
    private String tähis;
    private double kurss;

    public Valuuta(String tähis, double kurss) {
        this.tähis = tähis;
        this.kurss = kurss;
    }

    public void teisenda(double summa) throws Exception {

        System.out.println(summa + " EUR -> " + (Math.round((summa * kurss) * 100.0) / 100.0) + " " + tähis);

    }

    public void teisenda(double summa, Valuuta valuuta) throws Exception {

        System.out.println(summa + " " + valuuta.tähis + " -> " + (Math.round((summa / valuuta.kurss) * 100.0) / 100.0) + " EUR");

    }

    public double getKurss() {
        return kurss;
    }


    public void setKurss(double kurss) {
        this.kurss = kurss;
    }

    public String getTähis() {
        return tähis;
    }


    @Override
    public String toString() {
        return "EUR -> " + tähis + " kurss on " + kurss;
    }
}
