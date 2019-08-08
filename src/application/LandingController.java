package application;

import javafx.animation.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LandingController implements Initializable, Switchable
{
    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView landingScreen;
    @FXML
    private Button button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    @FXML
    void handle(MouseEvent event)
    {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), landingScreen);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./FXML/renderer.fxml"));
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.play();
        button.setDisable(true);
        button.setVisible(false);
        landingScreen.setDisable(true);
        ft.setOnFinished(e ->{
            try
            {
                Parent newScene = loader.load();//Carica la nuova scena
                stackPane.getChildren().remove(anchorPane);
                stackPane.getChildren().add(newScene);
            } catch (IOException exc)
            {
                exc.printStackTrace();
            }

        });
    }

    @Override @FXML
    public void changeScene(Event event)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./FXML/settings.fxml"));
        try
        {
            Parent newScene = loader.load();//Carica la nuova scena
            Duration duration = Duration.millis(800);
            ScaleTransition scaling = new ScaleTransition(duration, landingScreen);
            RotateTransition rotation = new RotateTransition(duration, landingScreen);
            TranslateTransition transition = new TranslateTransition(duration, landingScreen);
            FadeTransition fading = new FadeTransition(duration, landingScreen);

            fading.setFromValue(1);
            fading.setToValue(0);
            transition.setByX(250);
            transition.setByY(50);
            rotation.setByAngle(100);
            scaling.setByX(1.2);
            scaling.setByY(1.2);

            fading.play();
            rotation.play();
            scaling.play();
            transition.play();
            transition.setOnFinished(e ->{
                stackPane.getChildren().remove(anchorPane);
                stackPane.getChildren().add(newScene);
            });
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

