package sample;
import javafx.scene.canvas.Canvas;

abstract class FractalStrategy
{
    abstract public void render(Canvas canvas, Coordinates coords);
    abstract public void init(Coordinates coords);
}
