package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Verstrekker;

public class URLBox extends HBox {

    App app;

    Verstrekker verstrekker;
    TextField editField = new TextField();
    Hyperlink urlField = new Hyperlink();
    Button edit = new Button("edit");
    Button save = new Button("save");

    String url;

    public URLBox(App app) {
        setSpacing(10);

        this.app = app;

        urlField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                app.getHostServices().showDocument(url);
            }
        });

        editField.setPromptText("Voer URL in");
        edit = new Button("Edit");
        save = new Button("Save");
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                editField.setText(url);
                getChildren().setAll(editField, save);
            }
        });
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                url = editField.getText();
                urlField.setText("Renteoverzicht");
                getChildren().setAll(urlField, edit);
            }
        });

        reset();
        getChildren().setAll(editField, save);

    }

    public void reset() {
        editField.clear();
        getChildren().setAll(editField, save);
    }

    public void updateVerstrekker(Verstrekker verstrekker) {
        this.verstrekker = verstrekker;

        final String actueleRenteURL = verstrekker.getActueleRenteURL();
        if (actueleRenteURL == null || actueleRenteURL.equals("")) {
            getChildren().setAll(editField, save);
        } else {
            url = actueleRenteURL;
            urlField.setText("Renteoverzicht");
            getChildren().setAll(urlField, edit);
        }
    }

}
