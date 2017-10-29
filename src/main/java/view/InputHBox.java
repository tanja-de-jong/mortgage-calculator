package view;

import controller.InputController;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class InputHBox extends HBox {

    private TextField woningWaarde;
    private TextField bedrag;
    private TextField datumHypotheek;
    private TextField datumPasseren;
    private TextField extraAflossen;
    private Button vergelijkButton;

    public InputHBox(InputController controller) {
        woningWaarde = new TextField("350000");
        woningWaarde.setPromptText("Woningwaarde");
        
        bedrag = new TextField("330000");
        bedrag.setPromptText("Hypotheekbedrag");
         
        datumHypotheek = new TextField("01112017");
        datumHypotheek.setPromptText("Datum hypotheek");
         
        datumPasseren = new TextField("31042018");
        datumPasseren.setPromptText("Datum passeren");
         
        extraAflossen = new TextField();
        extraAflossen.setPromptText("Extra aflossen");
        
        vergelijkButton = new Button("Vergelijk");
        vergelijkButton.setOnAction(controller);

        getChildren().addAll(woningWaarde, bedrag, datumHypotheek, datumPasseren, extraAflossen, vergelijkButton);
    }

    public TextField getBedrag() {
        return bedrag;
    }

    public void setBedrag(TextField bedrag) {
        this.bedrag = bedrag;
    }

    public TextField getDatumHypotheek() {
        return datumHypotheek;
    }

    public void setDatumHypotheek(TextField datumHypotheek) {
        this.datumHypotheek = datumHypotheek;
    }

    public TextField getDatumPasseren() {
        return datumPasseren;
    }

    public void setDatumPasseren(TextField datumPasseren) {
        this.datumPasseren = datumPasseren;
    }

    public TextField getExtraAflossen() {
        return extraAflossen;
    }

    public void setExtraAflossen(TextField extraAflossen) {
        this.extraAflossen = extraAflossen;
    }

    public ButtonBase getVergelijkButton() {
        return vergelijkButton;
    }

    public TextField getWoningWaarde() {
        return woningWaarde;
    }
}
