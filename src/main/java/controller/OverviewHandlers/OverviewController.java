package controller.OverviewHandlers;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import model.Berekenaar;
import view.App;
import view.Overview;

/**
 * Created by Tanja on 16-9-2017.
 */
public class OverviewController implements EventHandler {

    private Overview view;
    private App app;

    public OverviewController(App app, Berekenaar model) {
        this.app = app;
        view = new Overview(model, this, new NieuweVerstrekkerController(app));
    }

    @Override
    public void handle(javafx.event.Event event) {
        String source = null;
        for (Button button : view.getButtons().keySet()) {
            if (event.getSource().equals(button)) source = view.getButtons().get(button);
        }

        assert(source != null);

        app.showChart(source);
    }

    public Overview getView() {
        return view;
    }
}
