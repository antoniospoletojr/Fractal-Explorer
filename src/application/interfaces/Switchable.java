package application.interfaces;

import javafx.event.Event;

/**
 * Switchable interface used by controllers in order to invoke next scene.
 * @author Antonio Spoleto Junior
 */
public interface Switchable
{
    /**
     * Method invoked on an user event which loads a new scene graph and set the stage to its content.
     * It is implemented with different transitions by each controller.
     * @param event
     */
    void changeScene(Event event);
}