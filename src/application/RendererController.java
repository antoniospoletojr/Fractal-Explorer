package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class RendererController implements Initializable
{
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Canvas canvas;
    @FXML
    private ProgressIndicator indicator;

    private FractalExplorer explorer;
    private Stack<Memento> history;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        canvas.setFocusTraversable(true); //Affinchè possa leggere gli eventi da tastiera
        history = new Stack<>();
        explorer = new FractalExplorer(indicator);
        explorer.setStrategy(new MandelbrotStrategy());
        explorer.render(canvas);
    }

    @FXML
    public void zoomHandler(MouseEvent event)
    {
        double xCoord = event.getSceneX();
        double yCoord = event.getSceneY();
        if (event.getButton() == MouseButton.PRIMARY)
        {
            history.push(explorer.saveState());
            explorer.zoomIn(xCoord, yCoord);
            explorer.render(canvas);
        } else if (event.getButton() == MouseButton.SECONDARY)
        {
            history.push(explorer.saveState());
            explorer.zoomOut(xCoord, yCoord);
            explorer.render(canvas);
        }
    }

    @FXML
    public void undoHandler(KeyEvent event)
    {
        if (!history.empty())
            explorer.restoreState(history.pop());
        explorer.render(canvas);
    }


}

