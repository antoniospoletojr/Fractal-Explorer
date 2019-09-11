package application.strategies;

import application.beans.Context;
import application.beans.Coordinates;
import application.math.Complex;
import application.processing.ColorPalette;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.stream.IntStream;

/**
 * Julia fractal algorithm/strategy class, with the ability to change the complex number C at runtime.
 * @author Antonio Spoleto Junior
 */
public class JuliaStrategy extends FractalStrategy
{
    private double fractionalIterations[][];
    private Complex c;
    private Double max;

    public JuliaStrategy(ColorPalette palette)
    {
        super(palette);
        c = new Complex( 0,0);
    }

    /**
     * Set complex number C to iterate on from the outside.
     * @param c
     */
    public void setC(Complex c)
    {
        this.c = c;
    }

    @Override
    public void init(Context context)
    {
        context.setCoordinates(new Coordinates(1.5, -1.5, 1.5, -1.5));
        context.setIterations(20);
    }

    public long render(WritableImage writableImage, Context context)
    {
        //Initialize variables
        int width = (int)writableImage.getWidth();
        int height = (int)writableImage.getHeight();
        fractionalIterations = new double[height][width];
        PixelWriter writer = writableImage.getPixelWriter();
        long start = System.currentTimeMillis();
        double deltaX = (context.getCoordinates().getRealMax() - context.getCoordinates().getRealMin()) / width;
        double deltaY = (context.getCoordinates().getImagMax() - context.getCoordinates().getImagMin()) / height;
        max = new Double(0);
        //Create parallel stream for multithreading
        IntStream xStream = IntStream.range(0, width).parallel();
        //Do the math and save values in a matrix
        xStream.forEach((int x) ->
        {
            for (int y = 0; y < height; y++)
            {
                Complex z = new Complex(x * deltaX + context.getCoordinates().getRealMin(), -y * deltaY + context.getCoordinates().getImagMax());
                fractionalIterations[y][x] = iterate(z, context.getIterations(), context.smoothing());//0 converge, quindi e' un numero interno al set. Diverso da 0, è un numero esterno al set
            }
        });
        //Normalize between 0 and 1
        normalize(context.getIterations(),width,height);
        //Equalize
        if(context.equalization())
            equalize(width,height);
        xStream = IntStream.range(0, width).parallel();
        //Associate values with colors
        xStream.forEach((int x) ->
        {
            for (int y = 0; y < height; y++)
            {
                synchronized (writer)
                {
                    if (fractionalIterations[y][x] != 0)
                    {
                        writer.setColor(x,y, palette.getColor(fractionalIterations[y][x]));
                    } else
                    {
                        writer.setColor(x, y, Color.BLACK);
                    }
                }
            }
        });
        long end = System.currentTimeMillis();
        return (end-start);
    }

    /**
     * Normalize convergence values in the matrix between 0 and 1, in order to make use of the color palette.
     * @param iterations
     * @param width
     * @param height
     */
    private void normalize(int iterations, int width, int height)
    {
        double maxIterations = max/(double)iterations;
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                fractionalIterations[y][x] = (fractionalIterations[y][x])/(maxIterations);
    }

    /**
     * Equalize convergence values in order to get uniform coloring and cover the whole palette, even in deeper zooms.
     * @param width
     * @param height
     */
    private void equalize(int width, int height)
    {
        int histogram[] = new int[(int)ColorPalette.PALETTE_LENGTH];

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if(fractionalIterations[y][x]!=0)
                {
                    int integerIteration = (int)(fractionalIterations[y][x]*(2047));
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
                if(fractionalIterations[y][x]!=0)
                {
                    int integerIteration = (int)(fractionalIterations[y][x]*(2047));
                    fractionalIterations[y][x]=0;
                    for (int i = 0; i <= integerIteration; i++)
                    {
                        fractionalIterations[y][x] += (float)histogram[i]/(total);
                    }
                }
            }
        }
    }

    /**
     * Iterate pixel value with Julia algorithm.
     * The function being iterated is the following: f(z) = z^2+c
     * with c being fixed and z varying (as opposed to Mandelbrot)
     * @param z
     * @param iterations
     * @param smoothing
     * @return
     */
    private double iterate(Complex z, int iterations, boolean smoothing)
    {
        double modulus;
        for (int i = 0; i < iterations; i++)//Dwell limit
        {
            z.pow(2);
            z.add(c);
            //If modulus>2 our z is not in the mandelbrot set
            modulus = z.abs();
            if (modulus > 10)
            {
                double convergenceDegree = (i+1 - (Math.log(Math.log(modulus))) / Math.log(2));
                synchronized (max)
                {
                    if(convergenceDegree>max)
                        max = convergenceDegree;
                }
                if(convergenceDegree<0) convergenceDegree=0;
                if(smoothing)
                    return convergenceDegree/iterations;
                else
                    return Math.floor(convergenceDegree)/iterations;
            }
        }
        return 0;
    }
}

