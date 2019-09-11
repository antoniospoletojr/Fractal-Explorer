package application.processing;

import application.exceptions.IncorrectSplineDataException;
import application.math.SplineInterpolator;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class ColorPalette
{
    private ArrayList<Color> scheme;
    public static double PALETTE_LENGTH = 2048;

    public ColorPalette(double[] red, double[] green, double[] blue) throws IncorrectSplineDataException
    {
        scheme = new ArrayList<>();
        double knots[] = new double[red.length];
        for (double i = 0; i <= knots.length - 1; i++)
        {
            double n = knots.length - 1;
            knots[(int) i] = i * (1 / n);
        }

        SplineInterpolator redInterpolator = new SplineInterpolator(knots, red);
        SplineInterpolator greenInterpolator = new SplineInterpolator(knots, green);
        SplineInterpolator blueInterpolator = new SplineInterpolator(knots, blue);
        for (int i = 0; i < PALETTE_LENGTH; i++)
        {
            double x = ((double) i) / PALETTE_LENGTH;
            scheme.add(Color.rgb(redInterpolator.interpolate(x), greenInterpolator.interpolate(x), blueInterpolator.interpolate(x)));
        }
    }

    public ColorPalette(String paletteName)
    {
        scheme = new ArrayList<>();
        double[] red = new double[5];
        double[] green = new double[5];
        double[] blue = new double[5];
        try
        {
            String path = URLDecoder.decode(getClass().getResource("../processing/palettes/").getPath(), "UTF-8");
            File file = new File(path + paletteName);
            Scanner scanner = new Scanner(file);
            int i = 0;
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String[] tokens = line.split("[\\s,]+");
                red[i] = Double.parseDouble(tokens[0]);
                green[i] = Double.parseDouble(tokens[1]);
                blue[i] = Double.parseDouble(tokens[2]);
                i++;
            }
            scanner.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
        }
        double knots[] = new double[5];
        for (double i = 0; i <= knots.length - 1; i++)
        {
            double n = knots.length - 1;
            knots[(int) i] = i * (1 / n);
        }
        try
        {
            SplineInterpolator redInterpolator = new SplineInterpolator(knots, red);
            SplineInterpolator greenInterpolator = new SplineInterpolator(knots, green);
            SplineInterpolator blueInterpolator = new SplineInterpolator(knots, blue);
            for (int i = 0; i < PALETTE_LENGTH; i++)
            {
                double x = ((double) i) / PALETTE_LENGTH;
                scheme.add(Color.rgb(redInterpolator.interpolate(x), greenInterpolator.interpolate(x), blueInterpolator.interpolate(x)));
            }
        } catch (IncorrectSplineDataException e)
        {
            System.out.println(e.toString());
        }
    }

    //Accepts numbers between 0 and 1
    public Color getColor(double index)
    {
        int newIndex = (int) (index * (PALETTE_LENGTH - 1));
        return scheme.get(newIndex);
    }

    public Color getColor(double index, double brightness)
    {
        int newIndex = (int) (index * (PALETTE_LENGTH - 1));
        Color matteColor = scheme.get(newIndex);
        return Color.hsb(matteColor.getHue(), matteColor.getSaturation(), 1 - brightness);

    }
}

