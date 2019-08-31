package application;

import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.Stack;

public class RendererController implements Initializable, Switchable
{
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button backButton;
    @FXML
    private Canvas canvas;
    @FXML
    private ProgressIndicator indicator;
    @FXML
    private Slider slider;
    @FXML
    private Text timeText;

    private FractalExplorer explorer;
    private Stack<Memento> history;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        anchorPane.setOnKeyPressed(
                keyEvent -> {
                    undoHandler(keyEvent);
                }
        );
//        slider.setFocusTraversable(false);
        slider.valueProperty().addListener((obs, oldval, newVal) -> slider.setValue(newVal.intValue()));
        canvas.setFocusTraversable(true); //Affinchè possa leggere gli eventi da tastiera
        history = new Stack<>();
        explorer = new FractalExplorer(indicator, canvas, timeText);
        explorer.setStrategy(new MandelbrotStrategy());
        explorer.render();
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
            explorer.render();
        } else if (event.getButton() == MouseButton.SECONDARY)
        {
            history.push(explorer.saveState());
            explorer.zoomOut(xCoord, yCoord);
            explorer.render();
        }
    }

    @FXML
    public void undoHandler(KeyEvent event)
    {
        if (!history.empty()) explorer.restoreState(history.pop());
        explorer.render();
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
                FadeTransition ft = new FadeTransition(Duration.millis(400), anchorPane);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.play();
                ft.setOnFinished(e ->
                {
                    window.setScene(scene);
                });
                } catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        @FXML
        void slideHandler()
        {
            explorer.setIterations((int)slider.getValue());
            explorer.render();
        }
    }

