package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import model.Berekenaar;
import model.Verstrekker;
import view.Chart;
import view.HoverNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tanja on 16-9-2017.
 */
public class ChartController implements ChangeListener {

    private Chart view;
    private Berekenaar model;

    public ChartController(Berekenaar model) {
        this.model = model;
        this.view = new Chart(this, model);
    }

    public Chart getView() {
        return view;
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        ObservableList data = FXCollections.observableArrayList();
        for (Verstrekker verstrekker : view.getModel().verstrekkers) {
            XYChart.Series<Number, Number> series = getSeries(verstrekker, newValue.toString());
            data.add(series);
        }
        view.getChart().setData(data);
    }

    public XYChart.Series getSeries(Verstrekker verstrekker, String onderwerp) {
        XYChart.Series series = new XYChart.Series();
        series.setName(verstrekker.name);
        //populating the series with data
        for (int year=0; year<
                verstrekker.brutos.size(); year+=12) {
            double value = verstrekker.data.get(onderwerp).get(year);
            XYChart.Data data = new XYChart.Data((year + 1) / 12, value);
            series.getData().add(data);
            Map<String, Double> verstrekkerValues = new HashMap<>();
            for (Verstrekker v : model.verstrekkers) {
                List<Double> onderwerpWaardes = v.data.get(onderwerp);
                double waarde = onderwerpWaardes.size() > year ? onderwerpWaardes.get(year) : 0.0;
                verstrekkerValues.put(v.name, waarde);
            }
            data.setNode(new HoverNode(verstrekker.data.get(onderwerp).get(year)));

        }
        return series;
    }




}
