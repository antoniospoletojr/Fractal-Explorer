package sample;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class MandelbrotStrategy extends FractalStrategy
{
    @Override
    public void init(Coordinates coords)
    {
        coords.setCoordinates(1,-2,1.5,-1.5);
    }

    @Override
    public void render(Canvas canvas, Coordinates coords)
    {
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        double deltaX = (coords.getRealMax() - coords.getRealMin()) / canvas.getWidth();
        double deltaY = (coords.getImagMax() - coords.getImagMin()) / canvas.getHeight();
        int convergenceSteps = 50;
        Complex c = new Complex(coords.getRealMin(), coords.getImagMax());
        for (double x = 0; x < canvas.getWidth(); x++)
        {
            for (double y = 0; y < canvas.getHeight(); y++)
            {
                c.setReal(x*deltaX+coords.getRealMin());
                c.setImag(-y*deltaY+coords.getImagMax());
                double convergenceValue = checkConvergence(c, convergenceSteps);
                double convergenceRatio = convergenceValue / convergenceSteps;
                if (convergenceValue != convergenceSteps)
                {
                    ctx.setFill(Color.hsb(convergenceValue/convergenceSteps*360, 1F,1F));
                } else
                {
                    ctx.setFill(Color.BLACK); // Convergence Color
                }
                ctx.fillRect(x, y, 1, 1);
            }
        }
    }

    private double checkConvergence(Complex c, int convergenceSteps)
    {
        Complex z = new Complex();
        for (int i = 0; i < convergenceSteps; i++)
        {
            z.square();
            z.add(c);
            //SE IL MODULO DEL NUMERO E' MAGGIORE DI DUE IL NOSTRO C NON E' NEL SET
            // √(a² + b²) <= 2.0
            // a² + b² <= 4.0
            if (z.real*z.real + z.imag*z.imag >= 4.0)
            {
                return i - (Math.log(Math.log(Math.sqrt(z.real*z.real + z.imag*z.imag))))/ Math.log(2.0);
            }
        }
        return convergenceSteps;
    }
}
