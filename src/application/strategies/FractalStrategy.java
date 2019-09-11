package application.strategies;

import application.beans.Context;
import application.processing.ColorPalette;
import javafx.scene.image.WritableImage;

/**
 * Abstract class implementing the Strategy and Bridge patterns and representing a generic fractal strategy.
 * @author Antonio Spoleto Junior
 */

public abstract class FractalStrategy
{
    protected ColorPalette palette;

    /**
     * Render a strategy getting informations from a context and writing into a WritableImage.
     * @param wi
     * @param context
     * @return
     */
    abstract public long render(WritableImage wi, Context context);

    /**
     * Initialize a context with initial parameters from a specific strategy.
     * @param context
     */
    abstract public void init(Context context);

    /**
     * Implementing the composition with the ColorPalette for the Bridge pattern.
     * @param palette
     */
    FractalStrategy(ColorPalette palette)
    {
        this.palette = palette;
    }

    /**
     * Returns the ColorPalette associated with the fractal.
     * @return
     */
    public ColorPalette getPalette(){
        return palette;
    }

}
