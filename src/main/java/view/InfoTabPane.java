package view;

import controller.ChartController;
import controller.InputController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Berekenaar;
import model.Offerte;
import model.Verstrekker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class InfoTabPane extends TabPane {

    public InputHBox inputHBox;

    public InfoTabPane(App app, final Berekenaar model) {
        this.inputHBox = new InputController(app, model, this).getView();

        update(app, model);
    }

    public void update(App app, Berekenaar model) {
        getTabs().clear();
        Tab totaleRenteTab = new Tab("Totale rente");
        Label label = new Label("Verstrekkers");
        TableColumn naamColumn = new TableColumn("Naam");
        naamColumn.setCellValueFactory(
                new PropertyValueFactory<Verstrekker, String>("name"));
        TableColumn totaleRenteColumn = new TableColumn("Totale rente");
        totaleRenteColumn.setCellValueFactory(
                new PropertyValueFactory<Verstrekker, String>("totaleRente"));

        totaleRenteColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn p) {
                TableCell cell = new TableCell<Verstrekker, Double>() {
                    @Override
                    public void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : getString());
                        setGraphic(null);
                    }

                    private String getString() {
                        Double totaleRente = getItem();
                        return totaleRente == null ? "Geen bedrag ingevuld" : String.format("€ %,6.0f", totaleRente);
                    }
                };

                cell.setStyle("-fx-alignment: top-right;");
                return cell;
            }
        });

        TableColumn offerteLooptijdCol = new TableColumn("Looptijd offerte");
        offerteLooptijdCol.setCellValueFactory(
                new PropertyValueFactory<Verstrekker, String>("offerte"));
        offerteLooptijdCol.setCellFactory(new Callback<TableColumn<Verstrekker, Offerte>,TableCell<Verstrekker,Offerte>>()
        {
            @Override
            public TableCell<Verstrekker,Offerte> call(TableColumn<Verstrekker,Offerte> p)
            {
                final TableCell<Verstrekker,Offerte> cell = new TableCell<Verstrekker,Offerte>()
                {
                    @Override
                    public void updateItem(final Offerte item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            this.setText("");
                        } else {
                            this.setText(item.getLooptijd() + " maanden");
                        }
                    }
                };
                return cell;
            }
        });

        TableColumn offerteVerlengingCol = new TableColumn("Verlenging");
        offerteVerlengingCol.setCellValueFactory(
                new PropertyValueFactory<Verstrekker, String>("offerte"));
        offerteVerlengingCol.setCellFactory(new Callback<TableColumn<Verstrekker,Offerte>,TableCell<Verstrekker,Offerte>>()
        {
            @Override
            public TableCell<Verstrekker,Offerte> call(TableColumn<Verstrekker,Offerte> p)
            {
                final TableCell<Verstrekker,Offerte> cell = new TableCell<Verstrekker,Offerte>()
                {
                    @Override
                    public void updateItem(final Offerte item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.getVerlenging() == null) {
                            this.setText("-");
                        } else {
                            this.setText(item.getVerlenging() + " maanden");
                        }
                    }
                };
                return cell;
            }
        });

        TableColumn offerteVerlengingKostenCol = new TableColumn("Kosten verlenging");
        offerteVerlengingKostenCol.setCellValueFactory(
                new PropertyValueFactory<Verstrekker, String>("offerte"));
        offerteVerlengingKostenCol.setCellFactory(new Callback<TableColumn<Verstrekker,Offerte>,TableCell<Verstrekker,Offerte>>()
        {
            @Override
            public TableCell<Verstrekker,Offerte> call(TableColumn<Verstrekker,Offerte> p)
            {
                final TableCell<Verstrekker,Offerte> cell = new TableCell<Verstrekker,Offerte>()
                {
                    @Override
                    public void updateItem(final Offerte item, boolean empty)
                    {
                        super.updateItem(item, empty);

                        if (empty || item == null) { // Geen offerte beschikbaar
                            this.setText("Geen offerte bekend");
                        } else {
                            double kosten = item.berekenKostenVerlenging(model.hypotheekDatum, model.notarisDatum, model.startBedrag);
                            if (kosten == Offerte.NIET_VERLENGD) {
                                setText("Geen verlenging");
                            } else if (kosten == Offerte.DATUM_NIET_INGEVULD) {
                                setText("Datum niet ingevuld");
                            } else if (kosten == Offerte.VERLENGING_NIET_MOGELIJK) {
                                setText("NIET MOGELIJK");
                            } else if (kosten == Offerte.PROVISIE_ONBEKEND) {
                                setText("Provisie onbekend");
                            } else if (kosten == Offerte.VERLENGING_ONBEKEND) {
                                setText("Verlenging onbekend");
                            } else {
                                setText(String.format("€ %,3.0f", kosten));
                            }
                        }
                    }
                };
                return cell;
            }
        });

        TableColumn totaleKostenCol = new TableColumn("Totale kosten");
        totaleKostenCol.setCellValueFactory(
                new PropertyValueFactory<Verstrekker, String>("offerte"));
        totaleKostenCol.setCellFactory(new Callback<TableColumn<Verstrekker,Offerte>,TableCell<Verstrekker,Offerte>>()
        {
            @Override
            public TableCell<Verstrekker,Offerte> call(TableColumn<Verstrekker,Offerte> p)
            {
                final TableCell<Verstrekker,Offerte> cell = new TableCell<Verstrekker,Offerte>()
                {
                    @Override
                    public void updateItem(final Offerte item, boolean empty)
                    {
                        super.updateItem(item, empty);

                        if (empty || item == null) { // Geen offerte beschikbaar
                            this.setText("Geen offerte bekend");
                        } else {
                            Verstrekker verstrekker = item.verstrekker;
                            double kosten = item.berekenKostenVerlenging(model.hypotheekDatum, model.notarisDatum, model.startBedrag);
                            if (kosten == Offerte.NIET_VERLENGD) {
                                setText(String.format("€ %,3.0f", verstrekker.totaleRente) + "");
                            } else if (kosten == Offerte.DATUM_NIET_INGEVULD) {
                                setText(String.format("€ %,3.0f", verstrekker.totaleRente) + " + ?");
                            } else if (kosten == Offerte.VERLENGING_NIET_MOGELIJK) {
                                setText("NIET MOGELIJK");
                            } else if (kosten == Offerte.PROVISIE_ONBEKEND) {
                                setText(String.format("€ %,3.0f", verstrekker.totaleRente) + " + ?");
                            } else if (kosten == Offerte.VERLENGING_ONBEKEND) {
                                setText(String.format("€ %,3.0f", verstrekker.totaleRente) + " + ?");
                            } else {
                                setText(String.format("€ %,3.0f", verstrekker.totaleRente + kosten));
                            }
                        }
                    }
                };
                return cell;
            }
        });

        TableView<Verstrekker> table = new TableView();
        table.setItems(FXCollections.observableList(model.verstrekkers));
        table.getColumns().addAll(naamColumn, totaleRenteColumn, offerteLooptijdCol, offerteVerlengingCol, offerteVerlengingKostenCol, totaleKostenCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(inputHBox, label, table);

        totaleRenteTab.setContent(vbox);
        getTabs().add(totaleRenteTab);

        for (String onderwerp: Verstrekker.vergelijkingsOnderwerpen) {
            InputHBox inputHBox2 = new InputController(app, model, this).getView();
            Tab tab = new Tab(onderwerp);
            Chart chart = new ChartController(model, onderwerp).getView();
            VBox vBox2 = new VBox();
            vBox2.getChildren().setAll(inputHBox2, chart);
            tab.setContent(vBox2);
            chart.setType(onderwerp);
            getTabs().add(tab);
        }
    }

}
