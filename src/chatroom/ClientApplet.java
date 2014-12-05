package chatroom;

import java.applet.*;
import java.awt.LayoutManager;

//import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;

public class ClientApplet extends Applet {

    /**
     * Generated serial version ID
     */
    private static final long serialVersionUID = 5484563350299263809L;

    public void init() {
        String host = getParameter("host");
        int port = Integer.parseInt(getParameter("port"));
//        setLayout(new BorderLayout());
//        setLayout(new Border());
        setLayout((LayoutManager) new BorderPane());
        add("Center", new Client(host, port));
    }
}
