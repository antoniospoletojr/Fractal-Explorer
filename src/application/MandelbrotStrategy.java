package application;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.stream.IntStream;

class MandelbrotStrategy extends FractalStrategy
{
    @Override
    public void init(Coordinates coords)
    {
        coords.setCoordinates(1, -2, 1.5, -1.5);
        palette = new ColorPalette();
    }

    public long render(WritableImage wi, Coordinates coords)
    {
        PixelWriter writer = wi.getPixelWriter();
        double width = wi.getWidth();
        double height = wi.getHeight();
        long start = System.currentTimeMillis();
        double deltaX = (coords.getRealMax() - coords.getRealMin()) / width;
        double deltaY = (coords.getImagMax() - coords.getImagMin()) / height;
        IntStream xStream = IntStream.range(0, (int) width).parallel();
        xStream.forEach((int x) ->
        {
            // We do pixels in horizontal lines always sequentially
            for (int y = 0; y < height; y++)
            {
                Complex c = new Complex(x * deltaX + coords.getRealMin(), -y * deltaY + coords.getImagMax());
                double convergenceDegree = checkConvergence(c);//1 converge, quindi è un numero interno al set. 0 diverge, quindi è un numero esterno al set
                synchronized (writer)
                {
                    if (convergenceDegree != 0)
                    {
                        int color = Math.abs((int) (convergenceDegree*2048)%2048);
                        //System.out.println((int) (convergenceDegree*2048)%2048);
                        writer.setColor(x, y, palette.scheme.get(color));
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

    private double checkConvergence(Complex c)
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
            if (modulus > 10)
            {
                //AUMENTA L'ACCURATEZZA - ABBASSA L'ERRORE SULL'APPROSSIMAZIONE
                for (int j = 0; j < 3; j++)
                {
                    z.square();
                    z.add(c);
                    i++;
                }
                return (i + 1  - (Math.log(Math.log(modulus))) / Math.log(2))/(iterations);
            }
        }
        return 0;
    }
}
