package view;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import model.Verstrekker;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class VerstrekkersListBox extends VBox {

    InfoTabPane parent;
    private Button create;

    public VerstrekkersListBox(List<Verstrekker> verstrekkers, InfoTabPane parent) {
        setSpacing(10);
        this.parent = parent;

        create = new Button("Maak nieuw");
        create.addEventHandler(ActionEvent.ACTION, (EventHandler<Event>) event -> parent.reset());

        update(verstrekkers);
    }

    public void update(List<Verstrekker> verstrekkers) {
        getChildren().clear();
        getChildren().add(create);

        Map<String, Verstrekker> verstrekkerMap = new TreeMap<>();
        for (Verstrekker v : verstrekkers) {
            verstrekkerMap.put(v.getNaam(), v);
        }

        for (String name : verstrekkerMap.keySet()) {
            Button button = new Button(name);
            button.addEventHandler(ActionEvent.ACTION, (EventHandler<Event>) event -> parent.getCreator().setVerstrekker(verstrekkerMap.get(name)));
            getChildren().add(button);
        }
    }
}
