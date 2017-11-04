package view;

import controller.ChartController;
import controller.InputController;
import javafx.collections.FXCollections;
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

public class ChartTab extends HypotheekTab {

    protected InputHBox inputBox;

    public ChartTab(App app, Berekenaar model, String subject) {
        super(app, model, subject);

        this.inputBox = new InputController(app, model, this).getView();
    }

    public Node createNode() {
        Chart chart = new ChartController(model, subject).getView();
        VBox vBox2 = new VBox();
        vBox2.getChildren().setAll(new InputController(app, model, this).getView(), chart);
        chart.setType(subject);

        return chart;
    }


    public void setData(List<Verstrekker> data) {
    }

}
