package application;

import javafx.scene.paint.Color;
import java.util.ArrayList;

public class ColorPalette
{
    public double[] knots = new double[5];
    public double[] yRed = {0, 32, 237, 255, 0};
    public double[] yGreen = {7, 107, 255, 170, 2};
    public double[] yBlue = {100, 203, 255, 0, 0};
    public SplineInterpolator redInterpolator;
    public SplineInterpolator greenInterpolator;
    public SplineInterpolator blueInterpolator;
    public ArrayList<Color> scheme;

    public ColorPalette()
    {
        for (double i = 0; i <= knots.length - 1; i++)
        {
            double n = knots.length - 1;
            knots[(int) i] = i * (1 / n);
        }
        try
        {
            redInterpolator = new SplineInterpolator(knots, yRed);
            greenInterpolator = new SplineInterpolator(knots, yGreen);
            blueInterpolator = new SplineInterpolator(knots, yBlue);

        } catch (IncorrectSplineDataException e)
        {
            System.out.println(e.toString());
        }

        scheme = new ArrayList<>();

        for (int i = 0; i < 2048; i++)
        {
            double x = ((double) i) / 2048;
            scheme.add(Color.rgb(redInterpolator.interpolate(x), greenInterpolator.interpolate(x), blueInterpolator.interpolate(x)));
        }
    }
}

