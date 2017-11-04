package view;

import controller.ChartController;
import controller.InputController;
import controller.TransformUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import model.Berekenaar;
import model.Offerte;
import model.Verstrekker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InfoTabPane extends TabPane {

    private App app;
    private Berekenaar model;

    TableTab tableTab;
    private VerstrekkerCreator creator;
    private VerstrekkersListBox verstrekkersBox;

    public InfoTabPane(App app, final Berekenaar model) {
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        this.app = app;
        this.model = model;

        update();
    }

    public void update() {
        getTabs().clear();
        tableTab = new TableTab(app, model, this);
        getTabs().add(tableTab);

        for (String onderwerp: Verstrekker.vergelijkingsOnderwerpen) {
            getTabs().add(new ChartTab(app, model, onderwerp));
        }

        Tab verstrekkersTab = new Tab("Verstrekkers");
        verstrekkersBox = new VerstrekkersListBox(model.verstrekkers, this);
        creator = new VerstrekkerCreator(app, this);

        HBox overviewBox = new HBox(10);

        verstrekkersTab.setContent(overviewBox);
        overviewBox.getChildren().addAll(new ScrollPane(verstrekkersBox), creator);

        getTabs().add(verstrekkersTab);
    }

    public VerstrekkerCreator getCreator() {
        return creator;
    }

    public void reset() {
        creator.reset();
        verstrekkersBox.update(model.getVerstrekkers());
    }
}
