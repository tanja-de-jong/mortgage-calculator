package view;

import controller.OverviewHandlers.NieuweVerstrekkerController;
import controller.OverviewHandlers.OverviewController;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import model.Berekenaar;
import model.Verstrekker;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Tanja on 16-9-2017.
 */
public class Overview extends VBox {

    Map<Button, String> buttons = new HashMap();
    OverviewController controller;

    public Overview(Berekenaar model, OverviewController controller, NieuweVerstrekkerController nieuweVerstrekkerController) {
        for (Verstrekker verstrekker : model.verstrekkers) {
            verstrekker.berekenBedragen(330000, 0);
            System.out.println(verstrekker.name + ": " + String.format("€ %,6.0f", verstrekker.totaleRente));
        }

        Button nieuweVerstrekker = new Button("Nieuwe verstrekker");
        nieuweVerstrekker.setOnAction(nieuweVerstrekkerController);
        getChildren().add(nieuweVerstrekker);

        for (String type : model.verstrekkers.get(0).data.keySet()) {
            Button button = new Button(type);
            button.setOnAction(controller);
            buttons.put(button, type);
            getChildren().add(button);
        }
    }

    public Map<Button, String> getButtons() {
        return buttons;
    }
}
