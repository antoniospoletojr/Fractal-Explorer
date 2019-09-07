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
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LandingController implements Initializable, Switchable
{

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    @Override
    @FXML
    public void changeScene(Event event)
    {
            try
            {
                Parent root = FXMLLoader.load(getClass().getResource("./FXML/palette.fxml")); //carica il root della nuova scena
                Stage window = (Stage) anchorPane.getScene().getWindow();
                Scene scene = new Scene(root);//carica il grafo della nuova scena
                ImageView srcImage = new ImageView(anchorPane.snapshot(new SnapshotParameters(),new WritableImage(800, 600)));
                ImageView destImage = new ImageView(root.snapshot(new SnapshotParameters(),new WritableImage(800, 600)));
                anchorPane.getChildren().addAll(destImage,srcImage);
                FadeTransition ft = new FadeTransition(Duration.millis(400), srcImage);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.play();
                ft.setOnFinished(e ->
                {
                    window.setScene(scene);
                });

            } catch (Exception e)
            {
                e.printStackTrace();
            }
    }
}