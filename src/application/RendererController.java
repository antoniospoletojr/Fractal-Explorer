package application;

import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.security.Key;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.Stack;

public class RendererController implements Initializable, Switchable
{
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button homeButton;
    @FXML
    private Button backButton;
    @FXML
    private Canvas canvas;
    @FXML
    private ProgressBar indicator;
    @FXML
    private Slider slider;
    @FXML
    private Text timeText;
    @FXML
    private Text xCoordinate;
    @FXML
    private Text yCoordinate;

    private FractalStrategy fractal;
    private FractalExplorer explorer;
    private Stack<Memento> history;

    RendererController(FractalStrategy fractal)
    {
        this.fractal = fractal;
        history = new Stack<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        slider.valueProperty().addListener((obs, oldval, newVal) -> slider.setValue(newVal.intValue()));
        slider.setValue(fractal.getIterations());
        canvas.setFocusTraversable(true); //Affinchè possa leggere gli eventi da tastiera

        explorer = new FractalExplorer(indicator, canvas, timeText, xCoordinate, yCoordinate);
        explorer.setStrategy(fractal);
        explorer.render();
    }

    @FXML
    void showLocation(MouseEvent event)
    {
        double xCoord = event.getX();
        double yCoord = event.getY();
        explorer.showCurrentLocation(xCoord,yCoord);
    }

    @FXML
    public void mouseListener(MouseEvent event)
    {
        double xCoord = event.getX();
        double yCoord = event.getY();
        if (event.getButton() == MouseButton.PRIMARY)                  //If left click, zoom in
        {
            history.push(explorer.saveState());
            explorer.zoomIn(xCoord, yCoord);
            explorer.render();
        } else if (event.getButton() == MouseButton.SECONDARY)         //If right click, zoom out
        {
            history.push(explorer.saveState());
            explorer.zoomOut(xCoord, yCoord);
            explorer.render();
        }
    }

    @FXML
    public void keyListener(KeyEvent event)
    {
        if(event.getCode()== KeyCode.SPACE)
        {
            if (!history.empty())
                explorer.restoreState(history.pop());
            explorer.render();
        }
        else if(event.getCode()== KeyCode.W)
        {
            history.push(explorer.saveState());
            explorer.shift(300,240);
            explorer.render();
        }
        else if(event.getCode()== KeyCode.S)
        {
            history.push(explorer.saveState());
            explorer.shift(300,260);
            explorer.render();
        }
        else if(event.getCode()== KeyCode.A)
        {
            history.push(explorer.saveState());
            explorer.shift(290,250);
            explorer.render();
        }
        else if(event.getCode()== KeyCode.D)
        {
            history.push(explorer.saveState());
            explorer.shift(310,250);
            explorer.render();
        }
    }

    @FXML
    void changeIterations()
    {
        fractal.setIterations((int) slider.getValue());
        explorer.render();
    }

    @Override
    @FXML
    public void changeScene(Event event)
    {
        if (event.getSource() == homeButton)
        {
            try
            {
                Node currentNode = (Node) event.getSource(); //prendi l'oggetto che ha generato l'event
                Parent root = FXMLLoader.load(getClass().getResource("./FXML/landing.fxml")); //carica il root della nuova scena
                Scene newScene = new Scene(root);//carica il grafo della nuova scena
                Scene currentScene = anchorPane.getScene(); //prendi la sua scena
                Stage window = (Stage) currentNode.getScene().getWindow();
                FadeTransition ft = new FadeTransition(Duration.millis(400), anchorPane);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.play();
                ft.setOnFinished(e ->
                {
                    window.setScene(newScene);
                });
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (event.getSource() == backButton)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("./FXML/family.fxml"));
                FamilyController controller = new FamilyController(fractal.getPalette());
                loader.setController(controller);
                Parent root = loader.load(); //carica il root della nuova scena
                Scene newScene = new Scene(root);//carica il grafo della nuova scena
                Stage window = (Stage) anchorPane.getScene().getWindow();
                FadeTransition ft = new FadeTransition(Duration.millis(400), anchorPane);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.play();
                ft.setOnFinished(e ->
                {
                    window.setScene(newScene);
                });
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

