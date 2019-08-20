package application;

import com.sun.scenario.animation.SplineInterpolator;
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
    }

    public void render(WritableImage wi, Coordinates coords)
    {
        PixelWriter writer = wi.getPixelWriter();
        double width = wi.getWidth();
        double height = wi.getHeight();
        ColorPalette palette = new ColorPalette();
        long start = System.currentTimeMillis();
        double offset = 3/(coords.getRealMax()-coords.getRealMin());
        iterations = Math.min(400, 22 + (int)offset);
        double deltaX = (coords.getRealMax() - coords.getRealMin()) / width;
        double deltaY = (coords.getImagMax() - coords.getImagMin()) / height;
        IntStream xStream = IntStream.range(0, (int)width).parallel();
        xStream.forEach((int x) ->
        {
            // We do pixels in horizontal lines always sequentially
            for (int y = 0; y < height; y++)
            {
                Complex c = new Complex(x * deltaX + coords.getRealMin(), -y * deltaY + coords.getImagMax());
                double convergenceDegree = checkConvergence(c);//1 converge, quindi è un numero interno al set. 0 diverge, quindi è un numero esterno al set
                synchronized (writer)
                {
                    if (convergenceDegree != 1)
                    {
                        writer.setColor(x, y, palette.scheme.get((int)(convergenceDegree*2048)));
                        //writer.setColor(x, y, palette(convergenceDegree));
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
        for (int i = 0; i <= iterations; i++)
        {
            z.square();
            z.add(c);
            //SE IL MODULO DEL NUMERO E' MAGGIORE DI DUE IL NOSTRO C NON E' NEL SET
            // √(a² + b²) <= 2.0
            // a² + b² <= 4.0
            double squared_modulus = z.real * z.real + z.imag * z.imag;
            if (squared_modulus > 100)
            {
                return (i+1 - (Math.log(Math.log(Math.sqrt(z.real * z.real + z.imag * z.imag)))) / Math.log(2.0))/iterations;
            }
        }
        return 1;
    }

//    float calcDistance( float a, float b )
//    {
//        Complex c( a, b );
//        Complex z( 0.0f, 0.0f );
//        Complex dz( 0.0f, 0.0f );
//
//        float m2;
//        for( int i=0; i<1024; i++ )
//        {
//            dz=2.0f*z*dz + 1.0f;
//            z=z*z + c;
//            m2=Complex::ModuloSquared(z);
//            if( m2 > 1e20f )
//                break;
//        }
//
//        // distance estimation: G/|G'|
//        return sqrtf( m2/Complex::ModuloSquared(dz) )*0.5f*logf(m2);
//    }

//    private Color palette(double t)
//    {
//        double r = 255*(0.5+0.5*Math.cos(2*Math.PI*(1.0*t+0.00)));
//        double g = 255*(0.5+0.5*Math.cos(2*Math.PI*(1.0*t+0.33)));
//        double b = 255*(0.5+0.5*Math.cos(2*Math.PI*(1.0*t+0.67)));
//        return Color.rgb((int)r, (int)g, (int)b);
////      a + b * cos(6.28318 * (c * t + d))
//    }
}

