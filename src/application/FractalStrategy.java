package application;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

abstract class FractalStrategy
{
    protected int iterations;
    //protected Color[] colors

    abstract public void render(WritableImage wi, Coordinates coords);
    abstract public void init(Coordinates coords);
}
