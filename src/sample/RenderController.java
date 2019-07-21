package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseButton;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class RenderController implements Initializable
{

    @FXML private Canvas canvas;
    @FXML private FractalExplorer explorer;
    private Stack<Memento> history;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        history = new Stack<>();
        explorer = new FractalExplorer();
        explorer.setStrategy(new MandelbrotStrategy());
        explorer.render(canvas);

        canvas.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                double xCoord = event.getSceneX();
                double yCoord = event.getSceneY();

                if (event.getButton() == MouseButton.PRIMARY)
                {
                    history.push(explorer.saveState());
                    explorer.zoomIn(xCoord, yCoord);
                    System.out.println("Pushing " + history.size());
                    explorer.render(canvas);
                }

                else if (event.getButton() == MouseButton.SECONDARY)
                {
                    //explorer.zoomOut(xCoord,yCoord);
                    explorer.restoreState(history.pop());
                    System.out.println("Popping " + history.size());
                    explorer.render(canvas);
                }
            }

        });
    }
}
