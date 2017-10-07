package view;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Created by Tanja on 16-9-2017.
 */
public class HoverNode extends StackPane {
    public HoverNode(double amount) {//int year, Map<String, Double> verstrekkerValues) {
        setPrefSize(15, 15);

        final Label label = createDataThresholdLabel(amount);

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                VBox vBox = new VBox(label);
                getChildren().setAll(vBox);
                toFront();
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                getChildren().clear();
            }
        });
    }

    private Label createDataThresholdLabel(double amount) { //int year, Map<String, Double> verstrekkerValues) {
        String text = "";//"Na " + (year / 12) + " jaar:";
       // for (String verstrekker : verstrekkerValues.keySet()) {
            text += /*"\n" + verstrekker + ": " + */String.format("€ %,6.0f", amount);
       // }
        final Label label = new Label(text);

        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
        label.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

        label.setTextFill(Color.DARKGRAY);

        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }
}
