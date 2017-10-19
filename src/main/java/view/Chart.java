package view;

import controller.ChartController;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import model.Berekenaar;

/**
 * Created by Tanja on 10-9-2017.
 */
public class Chart extends ScrollPane {

    Berekenaar model;
    ComboBox<String> type = new ComboBox();
    LineChart<Number,Number> chart;

    public Chart(ChartController controller, Berekenaar model, String onderwerp) {
        this.model = model;

        Pane pane = new Pane();

        // Create combo box
        type.getItems().add("Restbedrag");
        type.getItems().add("Aflossing");
        type.getItems().add("Rente");
        type.getItems().add("Bruto");
        type.valueProperty().addListener(controller);
        pane.getChildren().add(type);

        // Create chart
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Maand");
        chart = new LineChart(xAxis,yAxis);
        chart.setTitle("Hypotheken");
        chart.setCreateSymbols(false);
        chart.setPrefSize(800,800);
        pane.getChildren().add(chart);

        this.setContent(pane);
    }

    public Berekenaar getModel() {
        return model;
    }

    public ComboBox<String> getType() {
        return type;
    }

    public void setType(String value) {
        type.getSelectionModel().select(value);
    }

    public LineChart<Number, Number> getChart() {
        return chart;
    }

}