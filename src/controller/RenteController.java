package controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

import java.util.Map;

/**
 * Created by Tanja on 16-9-2017.
 */
public class RenteController implements EventHandler {

    TextField minimaalPercentage;
    TextField rentePercentage;
    Map<Double, Double> rentePercentages;

    public RenteController(TextField minimaalPercentage, TextField rentePercentage, Map<Double, Double> rentePercentages) {
        this.minimaalPercentage = minimaalPercentage;
        this.rentePercentage = rentePercentage;
        this.rentePercentages = rentePercentages;
    }

    @Override
    public void handle(Event event) {
        if (!minimaalPercentage.getText().isEmpty() && !rentePercentage.getText().isEmpty()) {
            rentePercentages.put(Double.parseDouble(minimaalPercentage.getText()), Double.parseDouble(rentePercentage.getText()));
        }
    }
}
