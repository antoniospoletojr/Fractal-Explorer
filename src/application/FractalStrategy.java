package application;

import javafx.scene.image.PixelWriter;

abstract class FractalStrategy
{
    protected int iterations;
    protected Coordinates coords;

    abstract public void render(PixelWriter pw, Coordinates coords);
    abstract public void init(Coordinates coords);
}
