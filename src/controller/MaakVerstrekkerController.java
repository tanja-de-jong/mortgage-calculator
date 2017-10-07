package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import view.App;
import view.VerstrekkerCreator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tanja on 16-9-2017.
 */
public class MaakVerstrekkerController implements EventHandler<ActionEvent> {

    private App app;
    private VerstrekkerCreator view = new VerstrekkerCreator(this);

    public MaakVerstrekkerController(App app) {
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event) {
        Map<Double, Double> rentePercentages = new HashMap<>();
        for (TextField rente : view.getRentePercentages().keySet()) {
            String minimaalPercentage = rente.getText();
            String rentePercentage = view.getRentePercentages().get(rente).getText();
            if (!minimaalPercentage.isEmpty() && !rentePercentage.isEmpty()){
                rentePercentages.put(Double.parseDouble(minimaalPercentage), Double.parseDouble(rentePercentage));
            }
        }

        app.getModel().addVerstrekker(true, view.getName(), rentePercentages);
        app.setScene(app.overview);

    }

    public VerstrekkerCreator getView() {
        return view;
    }
}
