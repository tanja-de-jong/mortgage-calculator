package view;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenteGroepenPane extends VBox {

    private GridPane gridPane = new GridPane();

    private Map<TextField, TextField> renteGroepen = new HashMap<>();

    public RenteGroepenPane() {
        gridPane.setHgap(10);
        reset();

        Button nextLine = new Button("Nieuwe rij");
        nextLine.addEventHandler(ActionEvent.ACTION, (EventHandler<Event>) event -> addRow());

        getChildren().addAll(gridPane, nextLine);
    }

    public void reset() {
        renteGroepen.clear();
        gridPane.getChildren().clear();
        for (int i=0; i<3; i++) {
            addRow();
        }
    }

    public void setRente(Map<Double, Double> renteGroepen) {
        gridPane.getChildren().clear();

        for (Double key : renteGroepen.keySet()) {
            addRow(key, renteGroepen.get(key));
        }
    }

    public void addRow() {
        addRow(null, null);
    }

    public void addRow(Double groep, Double percentage) {
        TextField groepField = new TextField(groep != null ? groep + "" : "");
        HBox groepBox = new HBox(groepField, new Label("%"));

        TextField percentageField = new TextField(percentage != null ? percentage + "" : "");
        HBox percentageBox = new HBox(percentageField, new Label("%"));

        renteGroepen.put(groepField, percentageField);

        gridPane.addRow(renteGroepen.size(), groepBox, percentageBox);
    }

    public Map<Double,Double> get() {
        Map<Double,Double> result = new HashMap<>();
        for (TextField key : renteGroepen.keySet()) {
            Double groep = null;
            Double percentage = null;
            try {
                groep = Double.parseDouble(key.getText());
                percentage = Double.parseDouble(renteGroepen.get(key).getText());
            } catch (NumberFormatException e) {}

            if (groep != null && percentage != null) {
                result.put(groep, percentage);
            }
        }
        return result;
    }
}
