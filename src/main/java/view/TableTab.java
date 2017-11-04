package view;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Berekenaar;
import model.Offerte;
import model.Verstrekker;

import java.util.List;

public class TableTab extends HypotheekTab {

    TableView<Verstrekker> table;

    public TableTab(App app, Berekenaar model, InfoTabPane tabPane) {
        super(app, model, "Tabel");
    }

    public Node createNode() {
        table = new TableView<>();
        table.setPrefSize(1000, 1000);
        setColumns(model);
        setData(model.verstrekkers);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(new Label("Verstrekkers"), table);

        return vbox;
    }

    private void setColumns(Berekenaar model) {
        TableColumn naamColumn = new TableColumn("Naam");
        naamColumn.setCellValueFactory(
                new PropertyValueFactory<Verstrekker, String>("naam"));
        TableColumn totaleRenteColumn = new TableColumn("Totale renteGroepen");
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
                            this.setText(null);
                        } else {
                            this.setText(item.getStandaardLooptijd() + " maanden");
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
                        if (empty) {
                            this.setText(null);
                        } else if (item == null || item.getMogelijkeVerlenging() == null) {
                            this.setText(null);
                        } else if (item.getMogelijkeVerlenging() == 999) {
                            this.setText("Oneindig");
                        } else {
                            this.setText(item.getMogelijkeVerlenging() + " maanden");
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

                        if (empty) {
                            this.setText(null);
                        } else if (item == null) { // Geen offerte beschikbaar
                            this.setText("Onbekend");
                        } else {
                            double kosten = item.berekenKostenVerlenging(model.hypotheekDatum, model.notarisDatum, model.startBedrag);
                            if (kosten == Offerte.NIET_VERLENGD) {
                                setText("N.v.t.");
                            } else if (kosten == Offerte.DATUM_NIET_INGEVULD) {
                                setText("Onbekend");
                            } else if (kosten == Offerte.VERLENGING_NIET_MOGELIJK) {
                                setText("NIET MOGELIJK");
                            } else if (kosten == Offerte.PROVISIE_ONBEKEND) {
                                setText("Onbekend");
                            } else if (kosten == Offerte.VERLENGING_ONBEKEND) {
                                setText("Onbekend");
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

                        if (empty || item == null) {
                            this.setText(null);
                        } else {
                            Verstrekker verstrekker = item.verstrekker;
                            double kosten = item.berekenKostenVerlenging(model.hypotheekDatum, model.notarisDatum, model.startBedrag);
                            if (kosten == Offerte.NIET_VERLENGD) {
                                setText(String.format("€ %,3.0f", verstrekker.totaleRente));
                            } else if (kosten == Offerte.DATUM_NIET_INGEVULD || kosten == Offerte.PROVISIE_ONBEKEND || kosten == Offerte.VERLENGING_ONBEKEND) {
                                this.setText(null);
                            } else if (kosten == Offerte.VERLENGING_NIET_MOGELIJK) {
                                setText("NIET MOGELIJK");
                            } else {
                                setText(String.format("€ %,3.0f", verstrekker.totaleRente + kosten));
                            }
                        }
                    }
                };
                return cell;
            }
        });

        table.getColumns().addAll(naamColumn, totaleRenteColumn, offerteLooptijdCol, offerteVerlengingCol, offerteVerlengingKostenCol, totaleKostenCol);
    }

    public void setData(List<Verstrekker> data) {
        table.setItems(FXCollections.observableList(data));
    }

}
