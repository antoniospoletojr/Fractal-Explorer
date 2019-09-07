package application;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.stream.IntStream;

class MandelbrotStrategy extends FractalStrategy
{
    private double fractionalIterations[][];
    private Double min;
    private Double max;

    MandelbrotStrategy(ColorPalette palette)
    {
        super(palette);
        iterations = 30;
    }

    @Override
    public void init(Coordinates coords)
    {
        coords.setCoordinates(1, -2, 1.5, -1.5);
    }

    public long render(WritableImage writableImage, Coordinates coords)
    {
        //Inizializzazione variabili
        int width = (int)writableImage.getWidth();
        int height = (int)writableImage.getHeight();
        fractionalIterations = new double[height][width];
        min = Double.MAX_VALUE;
        max = new Double(0);
        PixelWriter writer = writableImage.getPixelWriter();
        long start = System.currentTimeMillis();
        double deltaX = (coords.getRealMax() - coords.getRealMin()) / width;
        double deltaY = (coords.getImagMax() - coords.getImagMin()) / height;
        //Calcolo in parallelo le stime dei colori del frattale
        IntStream xStream = IntStream.range(0, width).parallel();
        xStream.forEach((int x) ->
        {
            for (int y = 0; y < height; y++)
            {
                Complex c = new Complex(x * deltaX + coords.getRealMin(), -y * deltaY + coords.getImagMax());
                fractionalIterations[y][x] = iterate(c);//0 converge, quindi è un numero interno al set. Diverso da 0, è un numero esterno al set
            }
        });
        //Normalize
        normalize();
        //Equalizzo
        //equalize();
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
                        //writer.setColor(x, y, palette.scheme.get((int)(fractionalIterations[y][x]*2047)));
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

    private void normalize()
    {

        for (int i = 0; i < fractionalIterations.length; i++)
        {
            for (int j = 0; j < fractionalIterations[0].length; j++)
            {
                if(fractionalIterations[i][j]!=0) //Se è un pixel NON nel set
                {
                    fractionalIterations[i][j] = (fractionalIterations[i][j] - min)/(max-min);

                }
            }
        }
    }

    private void equalize()
    {
        int histogram[] = new int[iterations];

        for (int x = 0; x < 600; x++)
        {
            for (int y = 0; y < 500; y++)
            {
                if(fractionalIterations[y][x]!=0)
                {
                    int integerIteration = (int)(fractionalIterations[y][x]*(iterations-1));
                    histogram[integerIteration]++;
                }
            }
        }
        int total = 0;
        for (int i = 0; i < histogram.length; i++)
        {
            total += histogram[i];
        }
        for (int x = 0; x < 600; x++)
        {
            for (int y = 0; y < 500; y++)
            {
                if(fractionalIterations[y][x]!=0)
                {
                    int integerIteration = (int)(fractionalIterations[y][x]*(iterations-1));
                    fractionalIterations[y][x]=0;
                    for (int i = 0; i <= integerIteration; i++)
                    {
                        fractionalIterations[y][x] += (float)histogram[i]/(total);
                    }
                }
            }
        }
    }

    private double iterate(Complex c)
    {
        Complex z = new Complex();
        double modulus;
        for (int i = 0; i < iterations; i++)//DWELL LIMIT
        {
            z.square();
            z.add(c);
            //SE IL MODULO DEL NUMERO E' MAGGIORE DI DUE IL NOSTRO C NON E' NEL SET
            // √(a² + b²) <= 2.0
            // a² + b² <= 4.0
            modulus = z.modulus();
            if (modulus > 10) //DIVERGO, NON SONO NEL SET
            {
                //AUMENTA L'ACCURATEZZA - ABBASSA L'ERRORE SULL'APPROSSIMAZIONE
//                    for (int j = 0; j < 3; j++)
//                    {
//                        z.square();
//                        z.add(c);
//                        i++;
//                    }
                double convergenceDegree = (i + 1 - (Math.log(Math.log(modulus))) / Math.log(2));
                synchronized(max)
                {
                    if(convergenceDegree>max)
                    {
                        max = convergenceDegree;
                    }
                }
                synchronized (min)
                {
                    if(convergenceDegree<min && convergenceDegree!=0)
                    {
                        min = convergenceDegree;
                    }
                }
                return convergenceDegree;
            }
        }
        return 0;
    }
}
