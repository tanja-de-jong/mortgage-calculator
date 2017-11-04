package view;

import com.sun.jndi.ldap.Ber;
import controller.InputController;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import model.Berekenaar;
import model.Offerte;
import model.Verstrekker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class HypotheekTab extends Tab {

    protected InputHBox inputBox;
    Node node;

    protected App app;
    protected Berekenaar model;
    protected String subject;

    public HypotheekTab(App app, Berekenaar model, String subject) {
        this.app = app;
        this.model = model;
        this.subject = subject;

        this.inputBox = new InputController(app, model, this).getView();
        this.node = createNode();
        setText(subject);

        this.setContent(new ScrollPane(new VBox(10, inputBox, node)));
    }

    abstract Node createNode();

    public abstract void setData(List<Verstrekker> verstrekkers);

    public List<Verstrekker> toonVerstrekkers(boolean toonOnmogelijk) {
        List<Verstrekker> result = new ArrayList<>(model.getVerstrekkers());
        if (!toonOnmogelijk) {
            Iterator<Verstrekker> iter = result.iterator();

            while (iter.hasNext()) {
                Verstrekker v = iter.next();

                Offerte o = v.getOfferte();
                boolean isVerlengingMogelijk = o != null ? o.isVerlengingMogelijk(model.hypotheekDatum, model.notarisDatum) : true;
                if (!isVerlengingMogelijk) iter.remove();
            }
        }
        return result;
    }

}
