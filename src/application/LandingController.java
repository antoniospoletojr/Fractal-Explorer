package application;

import javafx.animation.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.net.URL;
import java.util.ResourceBundle;

public class LandingController implements Initializable, Switchable
{
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button settingsButton;
    @FXML
    private Button sailButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    @FXML
    void handle(MouseEvent event)
    {
        //        FadeTransition ft = new FadeTransition(Duration.millis(1000), landingScreen);
        //        FXMLLoader loader = new FXMLLoader(getClass().getResource("./FXML/renderer.fxml"));
        //        ft.setFromValue(1.0);
        //        ft.setToValue(0.0);
        //        ft.play();
        //        button.setDisable(true);
        //        button.setVisible(false);
        //        landingScreen.setDisable(true);
        //        ft.setOnFinished(e ->{
        //            try
        //            {
        //                Parent newScene = loader.load();//Carica la nuova scena
        //                window.getChildren().remove(anchorPane);
        //                window.getChildren().add(newScene);
        //            } catch (IOException exc)
        //            {
        //                exc.printStackTrace();
        //            }
        //
        //        });
    }

    @Override
    @FXML
    public void changeScene(Event event)
    {
        if (event.getSource() == settingsButton)
        {
            try
            {

                Node currentNode = (Node) event.getSource(); //prendi l'oggetto che ha generato l'event
                Parent root = FXMLLoader.load(getClass().getResource("./FXML/settings.fxml")); //carica il root della nuova scena
                Scene scene = new Scene(root);//carica il grafo della nuova scena

                Stage window = (Stage) currentNode.getScene().getWindow();
                window.setScene(scene);
                window.show();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
            //            FXMLLoader loader = new FXMLLoader(getClass().getResource("./FXML/settings.fxml"));
            //            Scene currentScene = button.getScene();
            //            try
            //            {
            //                Parent root = loader.load();//Carica la nuova scena
            //                root.translateXProperty().set(-currentScene.getWidth());
            //                AnchorPane window = (AnchorPane) currentScene.getRoot();
            //                window.getChildren().add(root);
            //                Timeline timeline = new Timeline();
            //                KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            //                KeyFrame kf = new KeyFrame(Duration.seconds(0.6), kv);
            //                timeline.getKeyFrames().add(kf);
            //                timeline.setOnFinished(e ->
            //                {
            //                    window.getChildren().remove(anchorPane);
            //                });
            //                timeline.play();
            //            } catch (IOException e)
            //            {
            //                e.printStackTrace();
            //            }
        } else if (event.getSource() == sailButton)
        {
            try
            {
                Node currentNode = (Node) event.getSource(); //prendi l'oggetto che ha generato l'event
                Parent root = FXMLLoader.load(getClass().getResource("./FXML/renderer.fxml")); //carica il root della nuova scena
                Scene scene = new Scene(root);//carica il grafo della nuova scena

                Stage window = (Stage) currentNode.getScene().getWindow();
                window.setScene(scene);
                window.show();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
            //            FXMLLoader loader = new FXMLLoader(getClass().getResource("./FXML/renderer.fxml"));
            //            try
            //            {
            //                Parent newScene = loader.load();//Carica la nuova scena
            //                Duration duration = Duration.millis(800);
            //                ScaleTransition scaling = new ScaleTransition(duration, landingScreen);
            //                RotateTransition rotation = new RotateTransition(duration, landingScreen);
            //                TranslateTransition transition = new TranslateTransition(duration, landingScreen);
            //                FadeTransition fading = new FadeTransition(duration, landingScreen);
            //
            //                fading.setFromValue(1);
            //                fading.setToValue(0);
            //                transition.setByX(250);
            //                transition.setByY(50);
            //                rotation.setByAngle(100);
            //                scaling.setByX(1.2);
            //                scaling.setByY(1.2);
            //
            //                fading.play();
            //                rotation.play();
            //                scaling.play();
            //                transition.play();
            //                transition.setOnFinished(e ->
            //                {
            //                    window.getChildren().remove(anchorPane);
            //                    window.getChildren().add(newScene);
            //                });
            //            } catch (IOException e)
            //            {
            //                e.printStackTrace();
            //            }
        }
    }
}