package controller;

import controller.OverviewHandlers.NieuweVerstrekkerController;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Berekenaar;
import model.Offerte;
import model.Verstrekker;
import view.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tanja on 16-9-2017.
 */
public class InputController implements EventHandler {

    private InputHBox view;
    private App app;
    private Berekenaar model;

    public InputController(App app, Berekenaar model, TabPane tabPane) {
        this.app = app;
        this.model = model;
        view = new InputHBox(this);
    }

    public void handle(javafx.event.Event event) {
        double woningWaarde = Double.parseDouble(view.getWoningWaarde().getText());
        Double startBedrag = Double.parseDouble(view.getBedrag().getText());
        Date hypotheekDatum = getDatum(view.getDatumHypotheek());
        Date notarisDatum = getDatum(view.getDatumPasseren());
        String vasteExtraAflossing = view.getExtraAflossen().getText();
        if (vasteExtraAflossing.equals("")) vasteExtraAflossing = null;

        model.update(woningWaarde, startBedrag, hypotheekDatum, notarisDatum, vasteExtraAflossing);
        app.tabPane.update(app, model);
    }

    public Date getDatum(TextField veld) {
        Date result = null;
        String veldWaarde = veld.getText();
        if (!veldWaarde.equals("")) {
            try { result = new SimpleDateFormat("ddMMyyyy").parse(veldWaarde); }
            catch (ParseException e) {}
        }
        return result;
    }

    public InputHBox getView() {
        return view;
    }

    public void setView(InputHBox view) {
        this.view = view;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Berekenaar getModel() {
        return model;
    }

    public void setModel(Berekenaar model) {
        this.model = model;
    }
}
