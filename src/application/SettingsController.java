package application;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;


public class SettingsController implements Switchable
{
    @FXML
    private Button button;
    @FXML
    private AnchorPane anchorPane;

    public void changeScene(Event event)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./FXML/landing.fxml"));
        Scene currentScene = button.getScene();
        try
        {
            Parent newScene = loader.load();
            newScene.translateXProperty().set(currentScene.getWidth());
            StackPane stackPane = (StackPane)currentScene.getRoot();
            stackPane.getChildren().add(newScene);
            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(newScene.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.seconds(0.6),kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(e ->{
                stackPane.getChildren().remove(anchorPane);
            });
            timeline.play();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}