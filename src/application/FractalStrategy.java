package application;

import javafx.scene.image.WritableImage;

abstract class FractalStrategy
{
    protected int iterations=0;
    protected ColorPalette palette;

    abstract public long render(WritableImage wi, Coordinates coords);
    abstract public void init(Coordinates coords);

    FractalStrategy(ColorPalette palette)
    {
        this.palette = palette;
    }

    ColorPalette getPalette(){
        return palette;
    }

    public double getIterations()
    {
        return iterations;
    }

    void setIterations(int iterations)
    {
        this.iterations = iterations;
    }

}
