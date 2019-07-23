package sample;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class RenderController implements Initializable
{

    @FXML
    private Canvas canvas;
    @FXML
    private FractalExplorer explorer;
    @FXML
    private ImageView landingScreen;
    private Stack<Memento> history;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        canvas.setFocusTraversable(true);
        history = new Stack<>();
        explorer = new FractalExplorer();
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

    @FXML
    void handle(MouseEvent event)
    {
        FadeTransition ft = new FadeTransition(Duration.millis(1500), landingScreen);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.play();
        landingScreen.setDisable(true);
    }
}

