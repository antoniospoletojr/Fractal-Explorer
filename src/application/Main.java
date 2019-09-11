package application;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Entry point.
 * @author Antonio Spoleto Junior
 */

public class Main extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        //Empty the log file
        initLogFile();
        //Load new scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./FXML/landing.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        //Create transition
        FadeTransition ft = new FadeTransition(Duration.millis(1500), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        //Set and show scene
        stage.setTitle("Fractal Explorer");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        ft.play();
    }

    private void initLogFile()
    {
        try
        {
            new FileWriter("log", false).close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
