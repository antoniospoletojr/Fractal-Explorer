package application.strategies;

import application.beans.Context;
import application.processing.ColorPalette;
import javafx.scene.image.WritableImage;

public abstract class FractalStrategy
{
    protected ColorPalette palette;

    abstract public long render(WritableImage wi, Context context);
    abstract public void init(Context context);

    FractalStrategy(ColorPalette palette)
    {
        this.palette = palette;
    }

    public ColorPalette getPalette(){
        return palette;
    }

}
