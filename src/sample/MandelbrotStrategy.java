package sample;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

class MandelbrotStrategy extends FractalStrategy
{
    @Override
    public void init(Coordinates coords)
    {
        coords.setCoordinates(1, -2, 1.5, -1.5);
    }

    @Override
    public void render(Canvas canvas, Coordinates coords)
    {
        double offset = 5/(coords.getRealMax()-coords.getRealMin());
        iterations = Math.min(400, 20 + (int)offset);
        System.out.println(iterations);
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        PixelWriter pw = ctx.getPixelWriter();
        double deltaX = (coords.getRealMax() - coords.getRealMin()) / canvas.getWidth();
        double deltaY = (coords.getImagMax() - coords.getImagMin()) / canvas.getHeight();
        Complex c = new Complex(coords.getRealMin(), coords.getImagMax());
        for (int x = 0; x < canvas.getWidth(); x++)
        {
            for (int y = 0; y < canvas.getHeight(); y++)
            {
                c.setReal(x * deltaX + coords.getRealMin());
                c.setImag(-y * deltaY + coords.getImagMax());
                double convergenceDegree = checkConvergence(c);//1 converge, quindi è un numero interno al set. 0 diverge, quindi è un numero esterno al set
                if (convergenceDegree != 1)
                {
                    pw.setColor(x, y, palette(convergenceDegree));
                } else
                {
                    pw.setColor(x, y, Color.BLACK);
                }
            }
        }
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

