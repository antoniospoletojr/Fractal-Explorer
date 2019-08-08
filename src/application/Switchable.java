package application;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

interface Switchable
{
    void changeScene(Event event);

    default void redirectionMethod(String path, Event event)
    {
        try {
            Node currentNode = (Node)event.getSource();
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Scene scene = new Scene(root);

            Stage window = null;
            window = (Stage)currentNode.getScene().getWindow();
            window.setScene(scene);
            window.show();

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}