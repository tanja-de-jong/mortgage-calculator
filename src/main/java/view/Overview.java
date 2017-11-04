package view;

//import controller.OverviewHandlers.NieuweVerstrekkerController;
import controller.OverviewHandlers.OverviewController;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
    Button vergelijkButton = new Button("Vergelijk");
    OverviewController controller;

    public Overview(Berekenaar model, OverviewController controller) {
        HBox input = new HBox(10);
        TextField bedrag = new TextField();
        bedrag.setPromptText("Hypotheekbedrag");
        TextField datumHypotheek = new TextField();
        datumHypotheek.setPromptText("Datum hypotheek");
        TextField datumPasseren = new TextField();
        datumPasseren.setPromptText("Datum passeren");
        TextField extraAflossen = new TextField();
        extraAflossen.setPromptText("Extra aflossen");
        vergelijkButton.setOnAction(controller);

        getChildren().addAll(bedrag, datumHypotheek, datumPasseren, extraAflossen, vergelijkButton);

        //Button nieuweVerstrekker = new Button("Nieuwe verstrekker");
        //nieuweVerstrekker.setOnAction(nieuweVerstrekkerController);
        //getChildren().add(nieuweVerstrekker);

        for (String type : model.verstrekkers.get(0).getData().keySet()) {
            Button button = new Button(type);
            button.setOnAction(controller);
            buttons.put(button, type);
            getChildren().add(button);
        }
    }

    public Map<Button, String> getButtons() {
        return buttons;
    }

    public Button getVergelijkButton() {
        return vergelijkButton;
    }
}
