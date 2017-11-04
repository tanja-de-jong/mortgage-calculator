package view;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Offerte;
import model.Verstrekker;

public class OfferteInfoBox extends VBox {

    private Offerte offerte;
    private TextField standaardLooptijd = new TextField();
    private TextField mogelijkeVerlenging = new TextField();
    private TextField provisie = new TextField();
    private ChoiceBox<String> provisieLooptijd = new ChoiceBox<>();

    private Verstrekker verstrekker;

    public OfferteInfoBox(Verstrekker verstrekker) {
        setSpacing(10);
        setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        getChildren().add(new Label("OFFERTE"));

        GridPane offertePane = new GridPane();
        getChildren().add(offertePane);

        offertePane.setHgap(10);
        offertePane.add(new Label("Standaard looptijd:"), 0, 0);
        offertePane.add(standaardLooptijd, 1, 0);
        offertePane.add(new Label("maanden"), 2, 0);

        offertePane.add(new Label("Mogelijke verlenging:"), 0, 1);
        offertePane.add(mogelijkeVerlenging, 1, 1);
        offertePane.add(new Label("maanden"), 2, 1);

        offertePane.add(new Label("Provisie:"), 0, 2);
        offertePane.add(provisie, 1, 2);
        offertePane.add(provisieLooptijd, 2, 2);

        provisieLooptijd.getItems().addAll("over gehele looptijd", "over verlenging");
    }

    public OfferteInfoBox(Offerte offerte) {
        this(offerte.verstrekker);
        setOfferte(offerte);
    }

    public void reset() {
        this.offerte = new Offerte();
        standaardLooptijd.clear();
        mogelijkeVerlenging.clear();
        provisie.clear();
        provisieLooptijd.getSelectionModel().clearSelection();
    }

    public void setOfferte(Offerte offerte) {
        this.offerte = offerte;
        if (offerte != null) {
            Integer sl = offerte.getStandaardLooptijd();
            if (sl != null) {
                standaardLooptijd.setText(sl + "");
            }
            Integer mv = offerte.getMogelijkeVerlenging();
            if (mv != null) {
                mogelijkeVerlenging.setText(mv + "");
            }
            Double p = offerte.getProvisie();
            if (p != null) {
                provisie.setText(p + "");
            }
            boolean vr = offerte.isVasteRente();
            provisieLooptijd.getSelectionModel().select(vr ? "over gehele looptijd" : "over verlenging");
        }
    }

    public TextField getStandaardLooptijd() {
        return standaardLooptijd;
    }

    public void setStandaardLooptijd(TextField standaardLooptijd) {
        this.standaardLooptijd = standaardLooptijd;
    }

    public TextField getMogelijkeVerlenging() {
        return mogelijkeVerlenging;
    }

    public void setMogelijkeVerlenging(TextField mogelijkeVerlenging) {
        this.mogelijkeVerlenging = mogelijkeVerlenging;
    }

    public TextField getProvisie() {
        return provisie;
    }

    public void setProvisie(TextField provisie) {
        this.provisie = provisie;
    }

    public ChoiceBox<String> getProvisieLooptijd() {
        return provisieLooptijd;
    }

    public void setProvisieLooptijd(ChoiceBox<String> provisieLooptijd) {
        this.provisieLooptijd = provisieLooptijd;
    }

    public Offerte updateOfferte() {
        if (offerte == null) {
            offerte = new Offerte();
            offerte.setVerstrekker(verstrekker);
        }

        try {
            offerte.setStandaardLooptijd(Integer.parseInt(getStandaardLooptijd().getText()));
            offerte.setMogelijkeVerlenging(Integer.parseInt(getMogelijkeVerlenging().getText()));
            offerte.setProvisie(Double.parseDouble(getProvisie().getText()));
            offerte.setVasteRente(getProvisieLooptijd().getSelectionModel().getSelectedItem().equals("over gehele looptijd"));
        } catch (Exception e) {}
        // TODO: 02/11/2017 Request 'tussentijdseDalingMogelijk' and 'kostenBijRenteStijging' from user and set them in Offerte
        
        return offerte;
    }
}
