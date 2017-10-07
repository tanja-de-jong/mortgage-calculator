package view;

import controller.ChartController;
import controller.OverviewHandlers.OverviewController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Berekenaar;

/**
 * Created by Tanja on 16-9-2017.
 */
public class App extends Application {

    private Berekenaar model;
    private ChartController chart;
    private Stage chartStage = new Stage();
    private Stage primaryStage;
    public Scene overview;

    public static void main(final String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        model = new Berekenaar(330000);
        chart = new ChartController(model);
        final Scene chartScene = new Scene(chart.getView());
        chartStage.setMaximized(true);
        chartStage.setScene(chartScene);

        overview = new Scene(new OverviewController(this, model).getView());
        primaryStage.setScene(overview);
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void showChart(String type) {
        chart.getView().setType(type);
        chartStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Berekenaar getModel() {
        return model;
    }

    public void setScene(Scene scene) {
        primaryStage.setScene(scene);
    }
}
