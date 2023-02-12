package oop;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main extends Application {
    private static List<Valuuta> andmed = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        //Loeme failist kursside andmed
        andmed = FailiLugeja.loeKursid("currency-rates.csv");
        System.out.println("Lugesin andmed failist \"currency-rates.csv\", vajadusel uuenda faili");
        System.out.println("Andmed kuupäevaga: " + FailiLugeja.loeKuupaev("currency-rates.csv"));

        launch(args);

    }

    //Abimeetod tähise alusel valuuta leidmiseks
    public static Valuuta leiaValuuta(String sisend, List<Valuuta> andmed) throws EbasobivSisendErind {
        Valuuta valitud = null;

        //Otsime listist sisendina saadud tähist
        for (Valuuta valuuta : andmed) {
            if (valuuta.getTähis().equals(sisend.strip())) {
                valitud = valuuta;

            }
        }
        if (valitud == null) {
            throw new EbasobivSisendErind("Valuutat ei leitud");
        }
        return valitud;
    }

    //Meetod nupu Näita kursse handleri jaoks
    public static void naitaKursseGraafika(List<Valuuta> andmed) {
        Stage kursid = new Stage();
        VBox vbox = new VBox();
        ScrollPane sp = new ScrollPane();
        sp.setContent(vbox);

        for (Valuuta valuuta : andmed) {
            Text tekst = new Text(valuuta.toString());
            vbox.getChildren().add(tekst);
        }

        Scene stseen = new Scene(sp, 400, 400);
        kursid.setTitle("Valuutakursid");
        kursid.setScene(stseen);
        kursid.show();
    }

    //Meetod nupi Uus valuuta handleri jaoks
    public static void uusValuutaGraafika() {
        Stage uusValuuta = new Stage();
        VBox vbox = new VBox();
        TextField tahis = new TextField();
        TextField kurss = new TextField();
        Button nupp = new Button("OK");

        nupp.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                try {
                    andmed.add(new Valuuta(tahis.getText(), Double.parseDouble(kurss.getText())));
                    Alert alert = new Alert(Alert.AlertType.NONE, "Valuuta edukalt lisatud!", ButtonType.OK);
                    alert.showAndWait();
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Sisesta korrektne kursi väärtus", ButtonType.OK);
                    alert.showAndWait();
                }

            }
        });
        vbox.getChildren().add(new Text("Valuuta tähis"));
        vbox.getChildren().add(tahis);
        vbox.getChildren().add(new Text("Valuuta kurss"));
        vbox.getChildren().add(kurss);
        vbox.getChildren().add(nupp);


        Scene stseen = new Scene(vbox, 400, 300);
        uusValuuta.setTitle("Lisa uus valuuta");
        uusValuuta.setScene(stseen);
        uusValuuta.show();
    }

    //Meetod nupu Näita kurssi handleri jaoks
    public static void naitaKurssGraafika(List<Valuuta> andmed) {
        Stage kurss = new Stage();
        VBox vbox = new VBox();
        TextField tahis = new TextField();
        TextField kursiVaartus = new TextField();
        Button nupp = new Button("OK");
        //nupp.setOnMouseClicked(event -> kursiVaartus.setText(Double.toString(leiaValuuta(tahis.getText(), andmed).getKurss())));

        nupp.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                try {
                    kursiVaartus.setText(Double.toString(leiaValuuta(tahis.getText(), andmed).getKurss()));
                } catch (EbasobivSisendErind e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                    alert.showAndWait();
                }

            }
        });
        vbox.getChildren().add(new Text("Valuuta tähis"));
        vbox.getChildren().add(tahis);
        vbox.getChildren().add(new Text("Valuuta kurss"));
        vbox.getChildren().add(kursiVaartus);
        vbox.getChildren().add(nupp);
        Scene stseen = new Scene(vbox, 400, 300);
        kurss.setTitle("Valuutakurss");
        kurss.setScene(stseen);
        kurss.show();
    }

    public static void muudaKurssiGraafika(List<Valuuta> andmed) {
        Stage kurss = new Stage();
        VBox vbox = new VBox();
        TextField tahis = new TextField();
        TextField kursiVaartus = new TextField();
        Button nupp = new Button("OK");

        nupp.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                try {
                    try {
                        Double.parseDouble(kursiVaartus.getText());
                    } catch (NumberFormatException n) {
                        throw new EbasobivSisendErind("Sisesta korrektne kursi väärtus");
                    }
                    leiaValuuta(tahis.getText(), andmed).setKurss(Double.parseDouble(kursiVaartus.getText()));
                    Alert alert = new Alert(Alert.AlertType.NONE, "Kurss edukalt muudetud", ButtonType.OK);
                    alert.showAndWait();

                } catch (EbasobivSisendErind e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                    alert.showAndWait();
                }

            }
        });
        vbox.getChildren().add(new Text("Valuuta tähis"));
        vbox.getChildren().add(tahis);
        vbox.getChildren().add(new Text("Uus kurss"));
        vbox.getChildren().add(kursiVaartus);
        vbox.getChildren().add(nupp);
        Scene stseen = new Scene(vbox, 400, 300);
        kurss.setTitle("Valuutakursi muutmine");
        kurss.setScene(stseen);
        kurss.show();
    }

    @Override
    public void start(Stage pealava) throws Exception {
        // https://camposha.info/javafx-textfields-and-buttons-get-and-set-values-onclick/
        // https://docs.oracle.com/javase/8/javafx/layout-tutorial/builtin_layouts.htm#CHDCJDBD
        BorderPane border = new BorderPane();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 100, 100, 10));

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        Text title = new Text("Funktsioonid");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        //Kirjeldame nupud
        Button nupp1 = new Button("Näita kursse");
        Button nupp2 = new Button("Näita kurssi");
        Button nupp3 = new Button("Muuda kurssi");
        Button nupp6 = new Button("Uus valuuta");
        Button nupp7 = new Button("Välju");

        //Lisame nuppudele handlerid
        nupp1.setOnMouseClicked(event -> naitaKursseGraafika(andmed));
        nupp2.setOnMouseClicked(event -> naitaKurssGraafika(andmed));
        nupp3.setOnMouseClicked(event -> muudaKurssiGraafika(andmed));
        nupp6.setOnMouseClicked(event -> uusValuutaGraafika());
        nupp7.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Aja ja valuutade salvestamine väljumise hetkel ning sulgeme akna
                Date kp = new Date();
                SimpleDateFormat ft1 = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat ft2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                try (PrintWriter pw = new PrintWriter(new File(ft1.format(kp) + ".txt"), "UTF-8")) {
                    pw.println("Valuutakalkulaator 2.0");
                    pw.println("Muudetud: " + ft2.format(kp));
                    pw.println("");

                    for (Valuuta valuuta : andmed) {
                        pw.println(valuuta);
                    }
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Alert alert = new Alert(Alert.AlertType.NONE,
                        "Muudatused salvestatud faili nimega " + ft1.format(kp) + ".txt", ButtonType.OK);
                alert.showAndWait();

                System.exit(0);
            }
        });

        //Lisame nupud ühte massivi
        Button nupud[] = new Button[]{nupp1, nupp2, nupp3, nupp6, nupp7};

        //Lisame nupud VBoxi
        for (Button nupp : nupud) {
            nupp.setPrefSize(100, 50);
            nupp.setWrapText(true);
            vbox.getChildren().add(nupp);
        }
        //Taustapilt
        ImageView imageView = new ImageView();
        try (InputStream stream = new FileInputStream("logo.png")) {
            Image image = new Image(stream);
            imageView.setImage(image);
            imageView.setFitWidth(400);
            imageView.setPreserveRatio(true);
        }

        //Üllatused
        ImageView pilt1 = new ImageView();
        try (InputStream stream = new FileInputStream("pilt1.jpg")) {
            Image image = new Image(stream);
            pilt1.setImage(image);
            pilt1.setFitWidth(400);
            pilt1.setPreserveRatio(true);
        }
        ImageView pilt2 = new ImageView();
        try (InputStream stream = new FileInputStream("pilt2.jpg")) {
            Image image = new Image(stream);
            pilt2.setImage(image);
            pilt2.setFitWidth(400);
            pilt2.setPreserveRatio(true);
        }
        ImageView pilt3 = new ImageView();
        try (InputStream stream = new FileInputStream("pilt3.jpg")) {
            Image image = new Image(stream);
            pilt3.setImage(image);
            pilt3.setFitWidth(400);
            pilt3.setPreserveRatio(true);
        }
        ImageView pilt4 = new ImageView();
        try (InputStream stream = new FileInputStream("pilt4.jpg")) {
            Image image = new Image(stream);
            pilt4.setImage(image);
            pilt4.setFitWidth(400);
            pilt4.setPreserveRatio(true);
        }

        ImageView[] pildid = {pilt1, pilt2, pilt3, pilt4};


        grid.getChildren().add(vbox);

        //Faili kuupäeva andmed
        border.setBottom(new Text("Andmed seisuga: " + FailiLugeja.loeKuupaev("currency-rates.csv")));
        border.getChildren().add(grid);
        border.setRight(imageView);


        Scene stseen = new Scene(border, 550, 310);

        //Üllatus
        stseen.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    Random random = new Random();
                    ImageView pilt = pildid[random.nextInt(4)];
                    Stage ullatus = new Stage();
                    Group grupp = new Group();
                    grupp.getChildren().add(pilt);
                    Scene stseen = new Scene(grupp);
                    ullatus.setTitle(":)");
                    ullatus.setScene(stseen);
                    ullatus.setResizable(false);
                    ullatus.show();
                }
            }
        });
        pealava.setTitle("Valuutakalkulaator 2.0");
        pealava.setScene(stseen);
        pealava.setResizable(true);
        pealava.show();
    }
}
