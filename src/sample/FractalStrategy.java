package sample;
import javafx.scene.canvas.Canvas;
import java.util.ArrayList;

abstract class FractalStrategy
{
    protected int iterations;
    abstract public void render(Canvas canvas, Coordinates coords);
    abstract public void init(Coordinates coords);
}
