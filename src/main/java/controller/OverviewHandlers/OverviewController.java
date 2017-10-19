package controller.OverviewHandlers;

import controller.ChartController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Berekenaar;
import model.Offerte;
import model.Verstrekker;
import view.App;
import view.Chart;
import view.InfoTabPane;
import view.Overview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tanja on 16-9-2017.
 */
public class OverviewController implements EventHandler {

    private Overview view;
    private App app;
    private Berekenaar model;

    public OverviewController(App app, Berekenaar model) {
        this.app = app;
        this.model = model;
        view = new Overview(model, this, new NieuweVerstrekkerController(app));
    }

    public void handle(javafx.event.Event event) {
        if (event.getSource().equals(view.getVergelijkButton())) {
            //app.showChart("Restbedrag");
            /*TabPane tabPane = new TabPane();

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
                            if (totaleRente == null) totaleRente = 0.0;
                            return String.format("€ %,6.0f", totaleRente);
                        }
                    };

                    cell.setStyle("-fx-alignment: top-right;");
                    return cell;
                }
            });

            TableColumn offerteLooptijdCol = new TableColumn("Looptijd offerte");
            offerteLooptijdCol.setCellValueFactory(
                    new PropertyValueFactory<Verstrekker, String>("offerte"));
            offerteLooptijdCol.setCellFactory(new Callback<TableColumn<Verstrekker,Offerte>,TableCell<Verstrekker,Offerte>>()
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
                            if (empty || item.getVerlenging() == -1) {
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
                            Date startDatum = null;
                            Date eindDatum = null;
                            try {
                                startDatum = new SimpleDateFormat( "dd-MM-yyyy" ).parse( "01-11-2017" );
                                eindDatum = new SimpleDateFormat( "dd-MM-yyyy" ).parse( "14-05-2018" );
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (empty) {
                                this.setText("-");
                            } else {
                                double kosten = item.berekenKostenVerlenging(startDatum, eindDatum, model.startBedrag);
                                if (kosten == -2.0) {
                                    // De verlenging is langer dan deze verstrekker toestaat
                                    // TODO: 12/10/2017 Fix zodat het daadwerkelijke aantal maanden gebruikt wordt
                                    this.setText("Niet mogelijk");
                                } else if (kosten == -1.0) {
                                    // Geen provisie bekend of geen verlenging
                                    this.setText("-");
                                } else {
                                    this.setText(String.format("€ %,3.0f", kosten, model.startBedrag));
                                }
                            }
                        }
                    };
                    return cell;
                }
            });

            TableView<Verstrekker> table = new TableView();
            table.setItems(FXCollections.observableList(model.verstrekkers));
            table.getColumns().addAll(naamColumn, totaleRenteColumn, offerteLooptijdCol, offerteVerlengingCol, offerteVerlengingKostenCol);

            final VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.setPadding(new Insets(10, 0, 0, 10));
            vbox.getChildren().addAll(label, table);

            /*GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            int row = 0;
            for (Verstrekker verstrekker : app.getModel().verstrekkers) {
                String naam = verstrekker.name;
                double totaleRente = verstrekker.totaleRente;
                gridPane.add(new Button(naam), 0, row);
                gridPane.add(new Label(String.format("€ %,6.0f", totaleRente)), 1, row);
                row++;
            }*/
            /*totaleRenteTab.setContent(vbox);
            tabPane.getTabs().add(totaleRenteTab);

            for (String onderwerp: Verstrekker.vergelijkingsOnderwerpen) {
                Tab tab = new Tab(onderwerp);
                Chart chart = new ChartController(app.getModel(), "Bruto").getView();
                tab.setContent(chart);
                chart.setType(onderwerp);
                tabPane.getTabs().add(tab);
            }*/
        } else {
            String source = null;
            for (Button button : view.getButtons().keySet()) {
                if (event.getSource().equals(button)) source = view.getButtons().get(button);
            }

            assert (source != null);

            app.showChart(source);
        }
    }

    public Overview getView() {
        return view;
    }
}
