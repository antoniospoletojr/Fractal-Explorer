package application;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import java.util.stream.IntStream;

class MandelbrotStrategy extends FractalStrategy
{
    @Override
    public void init(Coordinates coords)
    {
        coords.setCoordinates(1, -2, 1.5, -1.5);
    }

    public void render(PixelWriter writer, Coordinates coords)
    {
        long start = System.currentTimeMillis();
        double offset = 5/(coords.getRealMax()-coords.getRealMin());
        iterations = Math.min(400, 20 + (int)offset);
        double deltaX = (coords.getRealMax() - coords.getRealMin()) / 600;
        double deltaY = (coords.getImagMax() - coords.getImagMin()) / 500;
        IntStream xStream = IntStream.range(0, 600).parallel();
        xStream.forEach((int x) ->
        {
            // We do pixels in horizontal lines always sequentially
            for (int y = 0; y < 500; y++)
            {
                Complex c = new Complex(x * deltaX + coords.getRealMin(), -y * deltaY + coords.getImagMax());
                double convergenceDegree = checkConvergence(c);//1 converge, quindi è un numero interno al set. 0 diverge, quindi è un numero esterno al set
                synchronized (writer)
                {
                    if (convergenceDegree != 1)
                    {
                        writer.setColor(x, y, palette(convergenceDegree));
                    } else
                    {
                        writer.setColor(x, y, Color.BLACK);
                    }
                }
            }
        });
        long end = System.currentTimeMillis();
        System.out.println(end-start + "ms");

    }

    private double checkConvergence(Complex c)
    {
        Complex z = new Complex();
        for (int i = 1; i <= iterations; i++)
        {
            z.square();
            z.add(c);
            //SE IL MODULO DEL NUMERO E' MAGGIORE DI DUE IL NOSTRO C NON E' NEL SET
            // √(a² + b²) <= 2.0
            // a² + b² <= 4.0
            if (z.real * z.real + z.imag * z.imag >= 4.0)
            {
                return (i - (Math.log(Math.log(z.real * z.real + z.imag * z.imag))) / Math.log(2.0))/iterations;
            }
        }
        return 1;
    }

    private Color palette(double t)
    {
        double r = 255*(0.5+0.5*Math.cos(2*Math.PI*(1.0*t+0.00)));
        double g = 255*(0.5+0.5*Math.cos(2*Math.PI*(1.0*t+0.33)));
        double b = 255*(0.5+0.5*Math.cos(2*Math.PI*(1.0*t+0.67)));
        return Color.rgb((int)r, (int)g, (int)b);

//      a + b * cos(6.28318 * (c * t + d))
    }
}

