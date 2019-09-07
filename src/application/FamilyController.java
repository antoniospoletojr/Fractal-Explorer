package application;

import javafx.animation.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FamilyController implements Initializable, Switchable
{
    @FXML
    private Button mandelbrotButton;
    @FXML
    private Button juliaButton;
    @FXML
    private Button burningShipButton;
    @FXML
    private Button backButton;
    @FXML
    private AnchorPane anchorPane;

    private ColorPalette palette;

    FamilyController(ColorPalette choosenPalette)
    {
        this.palette = choosenPalette;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    public void changeScene(Event event)
    {
        //GO BACK
        if (event.getSource() == backButton)
        {
            try
            {
                Parent root = FXMLLoader.load(getClass().getResource("./FXML/palette.fxml"));
                Stage window = (Stage) anchorPane.getScene().getWindow();
                Scene newScene = new Scene(root);//carica il grafo della nuova scena
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
        else if (event.getSource() == mandelbrotButton)
        {
            setChoosenFractal("Mandelbrot");
        }
        else if(event.getSource() == burningShipButton)
        {
            setChoosenFractal("BurningShip");
        }
        else if(event.getSource() == juliaButton)
        {
            setChoosenFractal("Julia");
        }
    }

    private void setChoosenFractal(String choosenFractal)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./FXML/renderer.fxml"));
        FractalStrategy strategy = FractalFactory.makeFractal(choosenFractal, palette);
        RendererController controller = new RendererController(strategy);
        loader.setController(controller);
        try
        {
            Parent root = loader.load();
            Stage window = (Stage) anchorPane.getScene().getWindow();
            Scene newScene = new Scene(root);//carica il grafo della nuova scena
            ImageView destImage = new ImageView(root.snapshot(new SnapshotParameters(),new WritableImage(800, 600)));
            destImage.setTranslateX(800);
            anchorPane.getChildren().add(destImage);
            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(destImage.translateXProperty(), 0, Interpolator.EASE_BOTH);
            KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
            timeline.getKeyFrames().add(kf);
            timeline.play();
            timeline.setOnFinished(t->{
                anchorPane.getChildren().remove(destImage);
                window.setScene(newScene);
            });

        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}

