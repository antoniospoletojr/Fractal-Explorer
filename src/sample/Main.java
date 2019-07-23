package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        FadeTransition ft = new FadeTransition(Duration.millis(1500), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        stage.setTitle("Fractal Explorer");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
        ft.play();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
