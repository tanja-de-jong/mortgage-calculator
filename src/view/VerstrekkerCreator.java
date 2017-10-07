package view;

import controller.MaakVerstrekkerController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tanja on 16-9-2017.
 */
public class VerstrekkerCreator extends GridPane {

    TextField name = new TextField();
    Map<TextField, TextField> rentePercentages = new HashMap<>();
    //Map<Double, Double> rentePercentages = new HashMap<>();

    public VerstrekkerCreator(MaakVerstrekkerController controller) {

        setPadding(new Insets(10, 10, 10, 10));
        add(new Label("Naam: "), 0, 0);         add(name, 1, 0);
        add(new Label("Rentegroepen "), 0, 1);  add(new Label("Vanaf %: "), 0, 2);
        add(new Label("Rente"), 1, 2);

        for (int i=0; i<10; i++) {
            TextField minimaalPercentage = new TextField();
            TextField rentePercentage = new TextField();

            add(minimaalPercentage, 0, i + 4);
            add(rentePercentage, 1, i + 4);
            rentePercentages.put(minimaalPercentage, rentePercentage);
            //Button addRente = new Button("Voeg toe");
            //addRente.setOnAction(new RenteController(minimaalPercentage, rentePercentage, rentePercentages));
            //add(addRente, 2, i + 4);
        }

        Button maak = new Button("Maak verstrekker");
        maak.setOnAction(controller);

        add(maak, 0, 14);
    }

    public String getName() {
        return name.getText();
    }

    public Map<TextField, TextField> getRentePercentages() {
        return rentePercentages;
    }
}
