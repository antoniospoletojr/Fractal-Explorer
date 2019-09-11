package application.controllers;

import application.interfaces.Switchable;
import javafx.animation.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller associated with the landing page scene.
 * @author Antonio Spoleto Junior
 */
public class LandingController implements Initializable, Switchable
{
    @FXML
    private AnchorPane anchorPane;

    /**
     * Initialize landing scene controller.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @Override
    @FXML
    public void changeScene(Event event)
    {
            try
            {
                Parent root;
                Button button = (Button)event.getSource();
                if(button.getId().equals("infoButton"))
                    root = FXMLLoader.load(getClass().getResource("../FXML/info.fxml")); //Get new root scene
                else
                    root = FXMLLoader.load(getClass().getResource("../FXML/palette.fxml")); //Get new root scene
                Stage window = (Stage) anchorPane.getScene().getWindow(); //Load actual window
                Scene scene = new Scene(root);//Load new graph scene
                //Create snapshots to implement the transition
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