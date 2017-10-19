package view;

import controller.ChartController;
import controller.OverviewHandlers.OverviewController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
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
    public InfoTabPane tabPane;

    public static void main(final String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        model = new Berekenaar();
        chart = new ChartController(model, "Bruto");
//        final Scene chartScene = new Scene(chart.getView());
   //     chartStage.setMaximized(true);
 //       chartStage.setScene(chartScene);

        overview = new Scene(new OverviewController(this, model).getView());
        //primaryStage.setScene(overview);
        primaryStage = chartStage;
        tabPane = new InfoTabPane(this, model);
        tabPane.inputHBox.getVergelijkButton().fire();
        chartStage.setScene(new Scene(tabPane));
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void showChart(String type) {
        chart.getView().setType(type);
        chartStage.show();
    }

    public void setPane() {
        chartStage.setScene(new Scene(tabPane));
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
