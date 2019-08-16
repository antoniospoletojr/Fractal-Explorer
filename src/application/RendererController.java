package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class RendererController implements Initializable, Switchable
{
    @FXML
    private Button backButton;
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

    @Override
    @FXML
    public void changeScene(Event event)
    {
        if (event.getSource() == backButton)
        {
            try
            {
                Node currentNode = (Node) event.getSource(); //prendi l'oggetto che ha generato l'event
                Parent root = FXMLLoader.load(getClass().getResource("./FXML/landing.fxml")); //carica il root della nuova scena
                Scene scene = new Scene(root);//carica il grafo della nuova scena

                Stage window = (Stage) currentNode.getScene().getWindow();
                window.setScene(scene);
                window.show();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


}

