package application.strategies;

import application.beans.Context;
import application.beans.Coordinates;
import application.math.Complex;
import application.processing.ColorPalette;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.stream.IntStream;

enum Root
{r1, r2, r3};

/**
 * Newton's family algorithm/strategy class.
 * @author Antonio Spoleto Junior
 */
public class NewtonStrategy extends FractalStrategy
{
    //The roots of the z^3-1
    private Complex r1;
    private Complex r2;
    private Complex r3;
    private double iterationCounts[][];
    private Root targets[][];

    public NewtonStrategy(ColorPalette palette)
    {
        super(palette);
        r1 = new Complex(1, 0);
        r2 = new Complex(-0.5, Math.sin(2 * Math.PI / 3));
        r3 = new Complex(-0.5, -Math.sin(2 * Math.PI / 3));
    }

    @Override
    public void init(Context context)
    {
        context.setCoordinates(new Coordinates(2, -2, 2, -2));
        context.setIterations(15);
    }

    public long render(WritableImage writableImage, Context context)
    {
        //Initialize variables
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        iterationCounts = new double[height][width];
        targets = new Root[height][width];
        PixelWriter writer = writableImage.getPixelWriter();
        long start = System.currentTimeMillis();
        double deltaX = (context.getCoordinates().getRealMax() - context.getCoordinates().getRealMin()) / width;
        double deltaY = (context.getCoordinates().getImagMax() - context.getCoordinates().getImagMin()) / height;
        //Create parallel stream for multithreading
        IntStream xStream = IntStream.range(0, width).parallel();
        //Do the math and save values in a matrix
        xStream.forEach((int x) ->
        {
            for (int y = 0; y < height; y++)
            {
                Complex z = new Complex(x * deltaX + context.getCoordinates().getRealMin(), -y * deltaY + context.getCoordinates().getImagMax());
                iterate(x, y, z, context.getIterations());
            }
        });
        //Equalize
        if (context.equalization()) equalize(context.getIterations(),width,height);
        //Associate values with colors
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if (targets[y][x] == null) writer.setColor(x, y, Color.BLACK);
                else
                {
                    double index = ((double) targets[y][x].ordinal() * 682f) / ColorPalette.PALETTE_LENGTH;
                    if (context.smoothing()) writer.setColor(x, y, palette.getColor(index, iterationCounts[y][x]));
                    else writer.setColor(x, y, palette.getColor(index));
                }
            }
        }
        long end = System.currentTimeMillis();
        return (end - start);
    }

    /**
     * Equalize convergence values in order to get uniform coloring and cover the whole palette, even in deeper zooms.
     * @param width
     * @param height
     */
    private void equalize(int bins, int width, int height)
    {
        int histogram[] = new int[bins];

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if(targets[y][x]!=null)
                {
                    int integerIteration = (int) (iterationCounts[y][x] * (bins-1));
                    histogram[integerIteration]++;
                }
            }
        }
        int total = 0;
        for (int i = 0; i < histogram.length; i++)
        {
            total += histogram[i];
        }
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if(targets[y][x]!=null)
                {
                    int integerIteration = (int) (iterationCounts[y][x] * bins);
                    iterationCounts[y][x] = 0;
                    for (int i = 0; i < integerIteration-1; i++)
                    {
                        iterationCounts[y][x] +=  ((float)histogram[i] / (float)(total));
                    }
                }
            }
        }
    }

    /**
     * Iterate Newton method on z till its difference with one of the roots is enough small.
     * The function being iterated is the following: f(z) = z^3-1
     * @param x
     * @param y
     * @param z
     * @param maxIterations
     */
    private void iterate(int x, int y, Complex z, int maxIterations)
    {
        iterationCounts[y][x] = 0;
        double epsilon = 0.0001f;
        while ((iterationCounts[y][x] < maxIterations) && (z.absDifference(r1) >= epsilon) && (z.absDifference(r2) >= epsilon) && (z.absDifference(r3) >= epsilon))
        {
            if (z.abs() > 0)
            {
                //Calculate new z using Newton's method
                //fz = z^3 -1
                Complex fz = new Complex(z);
                fz.pow(3);
                fz.subtract(1, 0);
                //fz' = 3z^2
                Complex dfz = new Complex(z);
                dfz.pow(2);
                dfz.multiply(3);
                //Newton's method
                fz.divide(dfz);
                z.subtract(fz);
            }
            iterationCounts[y][x]++;
        }
        if (z.absDifference(r1) < epsilon) targets[y][x] = Root.r1;
        if (z.absDifference(r2) < epsilon) targets[y][x] = Root.r2;
        if (z.absDifference(r3) < epsilon) targets[y][x] = Root.r3;
        iterationCounts[y][x] /= maxIterations;
    }
}