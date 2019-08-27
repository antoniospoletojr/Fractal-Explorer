package application;

import javafx.scene.image.WritableImage;

abstract class FractalStrategy
{
    protected int iterations;
    protected ColorPalette palette;

    abstract public long render(WritableImage wi, Coordinates coords);
    abstract public void init(Coordinates coords);

    void setIterations(int iterations)
    {
        this.iterations = iterations;
    }
}
