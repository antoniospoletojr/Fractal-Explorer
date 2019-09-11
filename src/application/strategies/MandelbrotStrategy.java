package application.strategies;

import application.beans.Context;
import application.beans.Coordinates;
import application.math.Complex;
import application.processing.ColorPalette;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.stream.IntStream;

public class MandelbrotStrategy extends FractalStrategy
{
    private double fractionalIterations[][];
    private Double max;
    public MandelbrotStrategy(ColorPalette palette)
    {
        super(palette);
    }

    @Override
    public void init(Context context)
    {
        context.setCoordinates(new Coordinates(1, -2, 1.5, -1.5));
        context.setIterations(30);
    }

    public long render(WritableImage writableImage, Context context)
    {
        //Inizializzazione variabili
        int width = (int)writableImage.getWidth();
        int height = (int)writableImage.getHeight();
        fractionalIterations = new double[height][width];
        PixelWriter writer = writableImage.getPixelWriter();
        long start = System.currentTimeMillis();
        double deltaX = (context.getCoordinates().getRealMax() - context.getCoordinates().getRealMin()) / width;
        double deltaY = (context.getCoordinates().getImagMax() - context.getCoordinates().getImagMin()) / height;
        max = new Double(0);
        //Calcolo in parallelo le stime dei colori del frattale
        IntStream xStream = IntStream.range(0, width).parallel();
        xStream.forEach((int x) ->
        {
            for (int y = 0; y < height; y++)
            {
                Complex c = new Complex(x * deltaX + context.getCoordinates().getRealMin(), -y * deltaY + context.getCoordinates().getImagMax());
                fractionalIterations[y][x] = iterate(c, context.getIterations(), context.smoothing());//0 converge, quindi è un numero interno al set. Diverso da 0, è un numero esterno al set
            }
        });
        normalize(context.getIterations(),width,height);
        //Equalizzo
        if(context.equalization())
            equalize(width,height);
        xStream = IntStream.range(0, width).parallel();
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

    private void normalize(int iterations, int width, int height)
    {
        double maxIterations = max/(double)iterations;
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                fractionalIterations[y][x] = (fractionalIterations[y][x])/(maxIterations);
    }

    private void equalize(int width, int height)
    {
        int histogram[] = new int[2048];

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

    private double iterate(Complex c, int iterations, boolean smoothing)
    {
        Complex z = new Complex();
        double modulus;
        for (int i = 0; i < iterations; i++)//DWELL LIMIT
        {
            z.pow(2);
            z.add(c);
            //SE IL MODULO DEL NUMERO E' MAGGIORE DI DUE IL NOSTRO C NON E' NEL SET
            // √(a² + b²) <= 2.0
            // a² + b² <= 4.0
            modulus = z.modulus();
            if (modulus > 2) //DIVERGO, NON SONO NEL SET
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
