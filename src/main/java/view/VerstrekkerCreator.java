package view;

import com.sun.deploy.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import model.Berekenaar;
import model.Verstrekker;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tanja on 16-9-2017.
 */
public class VerstrekkerCreator extends VBox {

    App app;
    InfoTabPane parent;
    Verstrekker verstrekker;
    private OfferteInfoBox offerteInfoBox = new OfferteInfoBox(verstrekker);

    private TextField naam = new TextField();
    private TextField laatstBijgewerkt = new TextField();
    private URLBox urlBox;
    private RenteGroepenPane renteGroepen = new RenteGroepenPane();
    private VBox opmerkingen = new VBox(10);
    Button submit = new Button();
    Button delete = new Button("Verwijder");

    //Map<Double, Double> rentePercentages = new HashMap<>();

    public VerstrekkerCreator(App app, InfoTabPane parent) {
        setSpacing(10);

        this.app = app;
        this.parent = parent;

        urlBox = new URLBox(app);

        submit.addEventHandler(ActionEvent.ACTION, (EventHandler<Event>) event -> updateVerstrekker());
        delete.addEventHandler(ActionEvent.ACTION, (EventHandler<Event>) event -> deleteVerstrekker());

        HBox buttons = new HBox(10, submit, delete);

        getChildren().addAll(naam, laatstBijgewerkt, urlBox, renteGroepen, offerteInfoBox, opmerkingen, buttons);
        reset();
    }

    public void reset() {
        this.verstrekker = null;

        naam.clear();
        laatstBijgewerkt.clear();
        urlBox.reset();
        renteGroepen.reset();
        offerteInfoBox.reset();
        opmerkingen.getChildren().clear();
        opmerkingen.getChildren().add(new TextField());
        submit.setText("Maak verstrekker");
    }

    public void setVerstrekker(Verstrekker verstrekker) {
        reset();

        this.verstrekker = verstrekker;

        naam.setText(verstrekker.getNaam());
        laatstBijgewerkt.setText(verstrekker.getLaatstBijgewerkt());
        urlBox.updateVerstrekker(verstrekker);
        renteGroepen.setRente(verstrekker.getRenteGroepen());
        offerteInfoBox.setOfferte(verstrekker.getOfferte());
        if (verstrekker.getOpmerkingen() != null) {
            for (String o : verstrekker.getOpmerkingen()) {
                opmerkingen.getChildren().add(new TextField(o));
            }
        }

        submit.setText("Update verstrekker");
    }

    public void updateVerstrekker() {
        if (verstrekker == null) verstrekker = new Verstrekker();

        verstrekker.setNaam(naam.getText());
        verstrekker.setLaatstBijgewerkt(laatstBijgewerkt.getText());
        verstrekker.setActueleRenteURL(urlBox.url);

        verstrekker.setRenteGroepen(renteGroepen.get());
        verstrekker.setOfferte(offerteInfoBox.updateOfferte());
        List<String> opmerkingenList = new ArrayList<>();
        for (Node n : opmerkingen.getChildren()) {
            TextField o = (TextField) n;
            if (!o.getText().isEmpty()) opmerkingenList.add(o.getText());
        }
        verstrekker.setOpmerkingen(opmerkingenList);

        // TODO: 02/11/2017 Request 'extraKorting', 'maximaalAflosbaar', 'maandelijkseKortingen' and 'opmerkingen' from
        // user and set them in Verstrekker

        app.getModel().updateVerstrekkers(verstrekker, Berekenaar.CREATE);
        parent.reset();
    }

    private void deleteVerstrekker() {
        app.getModel().updateVerstrekkers(verstrekker, Berekenaar.DELETE);
        parent.reset();
    }
}
